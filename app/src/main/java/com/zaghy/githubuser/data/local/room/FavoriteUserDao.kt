package com.zaghy.githubuser.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.zaghy.githubuser.data.local.entity.FavoriteUser

@Dao
interface FavoriteUserDao {
    @Query("SELECT * FROM FavoriteUser WHERE username = :username")
    fun checkFavoriteUserByUsername(username: String): LiveData<FavoriteUser>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
     suspend fun insertFavoriteUser(data:FavoriteUser)

    @Query("SELECT * FROM FavoriteUser")
    fun getAllFavoriteUser():LiveData<List<FavoriteUser>>

    @Delete
    suspend fun deleteFavoriteUser(data:FavoriteUser)
}