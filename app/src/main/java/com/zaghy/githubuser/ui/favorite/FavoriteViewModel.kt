package com.zaghy.githubuser.ui.favorite

import androidx.lifecycle.ViewModel
import com.zaghy.githubuser.data.repository.GithubUserRepository

class FavoriteViewModel(private val githubUserRepository: GithubUserRepository):ViewModel() {

    fun getAllFavoriteUser() = githubUserRepository.getAllFavoriteUser()
}