package com.zaghy.githubuser.data.remote.retrofit

import com.zaghy.githubuser.data.remote.response.ItemsItem
import com.zaghy.githubuser.data.remote.response.UserDetailResponse
import com.zaghy.githubuser.data.remote.response.UserListResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("search/users")
    suspend fun getUserByWord(@Query("q") user: String): UserListResponse

    @GET("users/{username}")
    suspend fun getDetailUser(@Path("username") username: String): UserDetailResponse

    @GET("users/{username}/followers")
    suspend fun getFollowersUser(@Path("username") username: String): List<ItemsItem>

    @GET("users/{username}/following")
    suspend fun getFollowingUser(@Path("username") username: String): List<ItemsItem>
}