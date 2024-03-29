package com.zaghy.githubuser.ui.detailuser

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.zaghy.githubuser.data.repository.GithubUserRepository
import com.zaghy.githubuser.di.Injection

class DetailUserViewModelFactory(private val githubUserRepository: GithubUserRepository):ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailUserViewModel::class.java)) {
            return DetailUserViewModel(githubUserRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }
    companion object{
        @Volatile
        private var instance: DetailUserViewModelFactory? = null
        fun getInstance(context: Context): DetailUserViewModelFactory =
            instance ?: synchronized(this){
                instance ?: DetailUserViewModelFactory(Injection.provideRepository(context))
            }.also {
                instance = it
            }
    }
}