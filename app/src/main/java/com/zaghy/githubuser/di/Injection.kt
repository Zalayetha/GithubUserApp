package com.zaghy.githubuser.di

import android.content.Context
import com.zaghy.githubuser.data.local.room.FavoriteUserDatabase
import com.zaghy.githubuser.data.remote.retrofit.ApiConfig
import com.zaghy.githubuser.data.repository.GithubUserRepository
import com.zaghy.githubuser.utils.AppExecutors

object Injection {
    fun provideRepository(context: Context): GithubUserRepository {
        val apiService = ApiConfig.getApiService()
        val database = FavoriteUserDatabase.getInstance(context)
        val dao = database.favoriteUserDao()
        val appExecutors = AppExecutors()
        return GithubUserRepository.getInstance(apiService, dao, appExecutors)
    }
}