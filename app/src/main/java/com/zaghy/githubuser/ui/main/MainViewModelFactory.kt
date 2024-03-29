package com.zaghy.githubuser.ui.main

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.zaghy.githubuser.data.datastore.SettingsPreferences
import com.zaghy.githubuser.data.datastore.dataStore
import com.zaghy.githubuser.data.repository.GithubUserRepository
import com.zaghy.githubuser.di.Injection

class MainViewModelFactory(private val preferences: SettingsPreferences,private val githubUserRepository: GithubUserRepository):
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(preferences,githubUserRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object{
        @Volatile
        private var instance:MainViewModelFactory? = null
        fun getInstance(context:Context):MainViewModelFactory =
            instance ?: synchronized(this){
                instance ?: MainViewModelFactory(SettingsPreferences.getInstance(context.dataStore),Injection.provideRepository(context))
            }.also {
                instance = it
            }
    }
}