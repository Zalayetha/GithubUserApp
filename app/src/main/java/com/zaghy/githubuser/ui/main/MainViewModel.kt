package com.zaghy.githubuser.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.zaghy.githubuser.data.datastore.SettingsPreferences
import com.zaghy.githubuser.data.remote.response.ItemsItem
import com.zaghy.githubuser.data.repository.GithubUserRepository

class MainViewModel(private val pref:SettingsPreferences,private val githubUserRepository: GithubUserRepository) : ViewModel() {
    private val _userListItems = MutableLiveData<List<ItemsItem>>()
    val userListItems: LiveData<List<ItemsItem>> = _userListItems

    fun findUserGithub(username:String) = githubUserRepository.findUserGithub(username)

    fun getThemeSettings(): LiveData<Boolean> {
        return pref.getThemeSetting().asLiveData()
    }


    fun setUserListItems(data:List<ItemsItem>){
        _userListItems.value = data
    }
}