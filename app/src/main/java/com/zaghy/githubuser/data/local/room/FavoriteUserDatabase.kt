package com.zaghy.githubuser.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.zaghy.githubuser.data.local.entity.FavoriteUser

@Database(entities = [FavoriteUser::class], version = 1)
abstract class FavoriteUserDatabase: RoomDatabase() {
    abstract fun favoriteUserDao():FavoriteUserDao

    companion object {
        @Volatile
        private var instance: FavoriteUserDatabase? = null
        fun getInstance(context: Context): FavoriteUserDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    FavoriteUserDatabase::class.java,
                    "favorite.db"
                ).build()
            }
    }
}