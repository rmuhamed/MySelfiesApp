package com.rmuhamed.sample.myselfiesapp.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.rmuhamed.sample.myselfiesapp.dao.AuthenticatedUsersDAO
import com.rmuhamed.sample.myselfiesapp.model.AuthenticatedUser

@Database(entities = [AuthenticatedUser::class], version = 1)
abstract class MySelfiesDatabase : RoomDatabase() {
    abstract fun authenticatedUsersDao(): AuthenticatedUsersDAO
}