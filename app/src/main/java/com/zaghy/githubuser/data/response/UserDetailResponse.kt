package com.zaghy.githubuser.data.response

import com.google.gson.annotations.SerializedName

data class UserDetailResponse(

	@field:SerializedName("bio")
	val bio: String,

	@field:SerializedName("login")
	val login: String,


	@field:SerializedName("followers")
	val followers: Int,

	@field:SerializedName("avatar_url")
	val avatarUrl: String,

	val htmlUrl: String,

	@field:SerializedName("following")
	val following: Int,

	@field:SerializedName("name")
	val name: String,

)
