package com.zaghy.githubuser.data.retrofit

import com.zaghy.githubuser.data.response.ItemsItem
import com.zaghy.githubuser.data.response.UserDetailResponse
import com.zaghy.githubuser.data.response.UserFollowersResponse
import com.zaghy.githubuser.data.response.UserFollowingResponse
import com.zaghy.githubuser.data.response.UserListResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @GET("search/users")
    fun getUserByWord(@Query("q") user:String):Call<UserListResponse>

    @GET("users/{username}")
    fun getDetailUser(@Path("username") username:String):Call<UserDetailResponse>

    @GET("users/{username}/followers")
    fun getFollowersUser(@Path("username") username: String):Call<List<ItemsItem>>

    @GET("users/{username}/following")
    fun getFollowingUser(@Path("username") username:String):Call<List<ItemsItem>>
}