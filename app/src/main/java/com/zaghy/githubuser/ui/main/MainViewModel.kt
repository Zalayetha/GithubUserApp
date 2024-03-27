package com.zaghy.githubuser.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.zaghy.githubuser.data.response.ItemsItem
import com.zaghy.githubuser.data.response.UserListResponse
import com.zaghy.githubuser.data.retrofit.ApiConfig
import com.zaghy.githubuser.datastore.SettingsPreferences
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(private val pref: SettingsPreferences) : ViewModel() {
    companion object {
        private const val TAG = "MainViewModel"
    }

    private val _userListItems = MutableLiveData<List<ItemsItem>>()
    val userListItems: LiveData<List<ItemsItem>> = _userListItems

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

//    for check night and day mode
    private val _isCheckedMode = MutableLiveData<Boolean>()
    val isCheckedMode:LiveData<Boolean> = _isCheckedMode

//    for check is filtered by favorite user or not
    private val _isCheckedFavorite = MutableLiveData<Boolean>()
    val isCheckedFavorite:LiveData<Boolean> = _isCheckedFavorite

    init {
        _isCheckedMode.value = false
        _isCheckedFavorite.value = false
        findUserGithub()
    }

    fun findUserGithub(userName: String = "a") {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUserByWord(userName)
        client.enqueue(object : Callback<UserListResponse> {
            override fun onResponse(
                call: Call<UserListResponse>,
                response: Response<UserListResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _userListItems.value = response.body()?.items
                } else {
                    Log.e(TAG, "onFailure ${response.message()}")
                    Log.e(TAG, "status : ${response.code()}")
                }
            }

            override fun onFailure(call: Call<UserListResponse>, t: Throwable) {
                Log.e(TAG, "OnFailure ${t.message.toString()}")
            }

        })
    }

    fun getThemeSettings(): LiveData<Boolean> {
        return pref.getThemeSetting().asLiveData()
    }

    private fun saveThemeSetting(isDarkModeActive: Boolean) {
        viewModelScope.launch {
            pref.saveThemeSetting(isDarkModeActive)
        }
    }

    fun toggleSwitchMode(status:Boolean){
        _isCheckedMode.value = !status
        saveThemeSetting(_isCheckedMode.value ?: false)
    }
    fun toggleSwitchFavorite(status:Boolean){
        _isCheckedFavorite.value = !status

    }
}