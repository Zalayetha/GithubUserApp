package com.zaghy.githubuser.ui.favorite

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.zaghy.githubuser.data.repository.GithubUserRepository
import com.zaghy.githubuser.di.Injection

class FavoriteViewModelFactory(private val githubUserRepository: GithubUserRepository) : ViewModelProvider.NewInstanceFactory(){
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavoriteViewModel::class.java)) {
            return FavoriteViewModel(githubUserRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }
    companion object{
        @Volatile
        private var instance: FavoriteViewModelFactory? = null
        fun getInstance(context: Context): FavoriteViewModelFactory =
            instance ?: synchronized(this){
                instance ?: FavoriteViewModelFactory(Injection.provideRepository(context))
            }.also {
                instance = it
            }
    }
}