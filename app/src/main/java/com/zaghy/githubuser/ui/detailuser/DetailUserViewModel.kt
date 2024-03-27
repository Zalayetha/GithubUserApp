package com.zaghy.githubuser.ui.detailuser


import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zaghy.githubuser.data.remote.response.ItemsItem
import com.zaghy.githubuser.data.remote.response.UserDetailResponse
import com.zaghy.githubuser.data.remote.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailUserViewModel : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading = _isLoading

    private val _detailUser = MutableLiveData<UserDetailResponse>()
    val detailUser = _detailUser

    private val _followersData = MutableLiveData<List<ItemsItem>>()
    val followersData = _followersData

    private val _followingData = MutableLiveData<List<ItemsItem>>()
    val followingData = _followingData

    private val _username = MutableLiveData<String>()
    val username = _username

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage = _errorMessage

    private val _hasError = MutableLiveData<Boolean>()
    val hasError = _hasError

    companion object {
        private const val TAG = "detail_user_view_model"
    }

    init {
        _username.value = ""
    }

    fun setUsername(username: String) {
        _username.value = username
    }

    fun findDetailUserGithub(username: String) {
        _isLoading.value = true
        _hasError.value = false
        val client = ApiConfig.getApiService().getDetailUser(username)
        client.enqueue(object : Callback<UserDetailResponse> {
            override fun onResponse(
                call: Call<UserDetailResponse>,
                response: Response<UserDetailResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _detailUser.value = response.body()
                } else {
                    Log.e(TAG, "status : ${response.code()}")
                    Log.e(TAG, "OnFailure ${response.message()}")
                }
            }

            override fun onFailure(call: Call<UserDetailResponse>, t: Throwable) {
                Log.e(TAG, "OnFailure ${t.message.toString()}")
                _hasError.value = true
                _errorMessage.value = t.message.toString()

            }

        })
    }

    fun getFollowers(username: String) {
        _isLoading.value = true
        _hasError.value = false
        val client = ApiConfig.getApiService().getFollowersUser(username)
        client.enqueue(object : Callback<List<ItemsItem>> {
            override fun onResponse(
                call: Call<List<ItemsItem>>,
                response: Response<List<ItemsItem>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _followersData.value = response.body()
                } else {
                    Log.e(TAG, "status : ${response.code()}")
                    Log.e(TAG, "OnFailure ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                Log.e(TAG, "OnFailure ${t.message.toString()}")
                _hasError.value = true
                _errorMessage.value = t.message.toString()
            }

        })
    }

    fun getFollowing(username: String) {
        _isLoading.value = true
        _hasError.value = false
        val client = ApiConfig.getApiService().getFollowingUser(username)
        client.enqueue(object : Callback<List<ItemsItem>> {
            override fun onResponse(
                call: Call<List<ItemsItem>>,
                response: Response<List<ItemsItem>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _followingData.value = response.body()
                } else {
                    Log.e(TAG, "status : ${response.code()}")
                    Log.e(TAG, "OnFailure ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                Log.e(TAG, "OnFailure ${t.message.toString()}")
                _hasError.value = true
                _errorMessage.value = t.message.toString()
            }

        })
    }


}