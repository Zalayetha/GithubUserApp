package com.zaghy.githubuser.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.zaghy.githubuser.data.Result
import com.zaghy.githubuser.data.local.entity.FavoriteUser
import com.zaghy.githubuser.data.local.room.FavoriteUserDao
import com.zaghy.githubuser.data.remote.response.ItemsItem
import com.zaghy.githubuser.data.remote.response.UserListResponse
import com.zaghy.githubuser.data.remote.retrofit.ApiConfig
import com.zaghy.githubuser.data.remote.retrofit.ApiService
import com.zaghy.githubuser.utils.AppExecutors
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GithubUserRepository private constructor(
    private val apiService: ApiService,
    private val favoriteUserDao: FavoriteUserDao,
    private val appExecutors: AppExecutors
) {

    private val resultUserGithub = MediatorLiveData<Result<List<ItemsItem>>>()

    companion object{
        @Volatile
        private var instance:GithubUserRepository? = null
        fun getInstance(
            apiService: ApiService,
            favoriteUserDao: FavoriteUserDao,
            appExecutors: AppExecutors
        ):GithubUserRepository = instance ?: synchronized(this){
            instance ?: GithubUserRepository(apiService,favoriteUserDao,appExecutors).also {
                instance = it
            }
        }

        private const val TAG = "GithubUserRepository"
    }

//    find user github from api service
    fun findUserGithub(userName: String = "a"):LiveData<Result<List<ItemsItem>>> {
        resultUserGithub.value = Result.Loading
        val client = ApiConfig.getApiService().getUserByWord(userName)
        client.enqueue(object : Callback<UserListResponse> {
            override fun onResponse(
                call: Call<UserListResponse>,
                response: Response<UserListResponse>
            ) {
                resultUserGithub.value = Result.Loading
                if (response.isSuccessful) {
                    val githubUser = response.body()?.items ?: emptyList()
                    resultUserGithub.postValue(Result.Success(githubUser))
                } else {
                    Log.e(TAG, "onFailure ${response.message()}")
                    Log.e(TAG, "status : ${response.code()}")
                }
            }

            override fun onFailure(call: Call<UserListResponse>, t: Throwable) {
                resultUserGithub.value = Result.Error(t.message.toString())
            }

        })
        return resultUserGithub
    }

//    add favorite user to local database
    fun addFavoriteGithubUser(data:FavoriteUser){
        val favoriteUser = FavoriteUser(
            data.username,
            data.avatarUrl
        )
        favoriteUserDao.insertFavoriteUser(favoriteUser)
    }

}