package com.zaghy.githubuser.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.liveData
import com.zaghy.githubuser.data.Result
import com.zaghy.githubuser.data.local.entity.FavoriteUser
import com.zaghy.githubuser.data.local.room.FavoriteUserDao
import com.zaghy.githubuser.data.remote.response.ItemsItem
import com.zaghy.githubuser.data.remote.response.UserDetailResponse
import com.zaghy.githubuser.data.remote.retrofit.ApiService

class GithubUserRepository private constructor(
    private val apiService: ApiService,
    private val favoriteUserDao: FavoriteUserDao,
) {

    private val resultFavoriteUserGithubByUsername = MediatorLiveData<Result<FavoriteUser>?>()
    private val resultAllFavoriteUserGithub = MediatorLiveData<Result<List<FavoriteUser>>>()
    companion object {
        @Volatile
        private var instance: GithubUserRepository? = null
        fun getInstance(
            apiService: ApiService,
            favoriteUserDao: FavoriteUserDao,
        ): GithubUserRepository = instance ?: synchronized(this) {
            instance ?: GithubUserRepository(apiService, favoriteUserDao).also {
                instance = it
            }
        }

        private const val TAG = "GithubUserRepository"
    }

    //    find user github from api service
    fun findUserGithub(username: String): LiveData<Result<List<ItemsItem>>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getUserByWord(username)
            emit(Result.Success(response.items))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    //    get followers from api service
    fun getFollowers(username: String): LiveData<Result<List<ItemsItem>>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getFollowersUser(username)
            emit(Result.Success(response))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }

    }

    //    get Following from api service
    fun getFollowing(username: String): LiveData<Result<List<ItemsItem>>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getFollowingUser(username)
            emit(Result.Success(response))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }

    }

    //    get detail user github
    fun findDetailUserGithub(username: String): LiveData<Result<UserDetailResponse>> = liveData {
        emit(Result.Loading)
        try{
            val response = apiService.getDetailUser(username)
            emit(Result.Success(response))
        }catch (e:Exception){
            emit(Result.Error(e.message.toString()))
        }
    }

    //    add favorite user to local database
    suspend fun addFavoriteGithubUser(data: FavoriteUser) {
        val favoriteUser = FavoriteUser(
            data.username,
            data.avatarUrl
        )
        favoriteUserDao.insertFavoriteUser(favoriteUser)
    }


    //    Check Favorite User in Local Database
    fun getFavoriteGithubUserByUsername(username: String?): LiveData<Result<FavoriteUser>?> {
        val favoriteUserLocalData = favoriteUserDao.checkFavoriteUserByUsername(username ?: "")

        resultFavoriteUserGithubByUsername.addSource(favoriteUserLocalData) { favoriteUser ->
            if (favoriteUser != null) {
                resultFavoriteUserGithubByUsername.value = Result.Success(favoriteUser)
                Log.d(TAG, "User found: ${favoriteUser.username}")
            } else {
                resultFavoriteUserGithubByUsername.value = null
                Log.d(TAG, "No user found")
            }
        }

        return resultFavoriteUserGithubByUsername
    }

    //    Get All Favorite User in local database
    fun getAllFavoriteUser(): LiveData<Result<List<FavoriteUser>>> {
        val favoriteUserLocalData = favoriteUserDao.getAllFavoriteUser()
        resultAllFavoriteUserGithub.addSource(favoriteUserLocalData) { newData: List<FavoriteUser> ->
            resultAllFavoriteUserGithub.value = Result.Success(newData)

        }
        return resultAllFavoriteUserGithub
    }

    suspend fun deleteFavoriteUser(data: FavoriteUser) {
        val favoriteUser = FavoriteUser(
            data.username,
            data.avatarUrl
        )
        favoriteUserDao.deleteFavoriteUser(favoriteUser)
    }


}