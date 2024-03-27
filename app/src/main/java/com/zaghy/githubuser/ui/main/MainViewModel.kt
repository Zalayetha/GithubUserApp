package com.zaghy.githubuser.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zaghy.githubuser.data.response.ItemsItem
import com.zaghy.githubuser.data.response.UserListResponse
import com.zaghy.githubuser.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel(){
    companion object{
        private const val TAG = "MainViewModel"
    }

    private val _userListItems = MutableLiveData<List<ItemsItem>>()
    val userListItems:LiveData<List<ItemsItem>> = _userListItems

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    init {
//        _isLoading.value = true
        findUserGithub()
    }
    fun findUserGithub(userName: String = "a"){
         _isLoading.value = true
        val client = ApiConfig.getApiService().getUserByWord(userName)
        client.enqueue(object: Callback<UserListResponse>{
            override fun onResponse(
                call: Call<UserListResponse>,
                response: Response<UserListResponse>
            ) {
                _isLoading.value = false
                if(response.isSuccessful){
                    _userListItems.value = response.body()?.items
                }else{
                    Log.e(TAG,"onFailure ${response.message()}")
                    Log.e(TAG,"status : ${response.code()}")
                }
            }

            override fun onFailure(call: Call<UserListResponse>, t: Throwable) {
                Log.e(TAG,"OnFailure ${t.message.toString()}")
            }

        })
    }
}