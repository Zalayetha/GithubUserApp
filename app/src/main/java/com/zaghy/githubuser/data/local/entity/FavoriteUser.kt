package com.zaghy.githubuser.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class FavoriteUser (
    @PrimaryKey(autoGenerate = false)
    var username:String = "",
    @ColumnInfo("avatarUrl")
    var avatarUrl:String? = null
)