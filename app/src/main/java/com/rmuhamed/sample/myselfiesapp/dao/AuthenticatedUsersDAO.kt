package com.rmuhamed.sample.myselfiesapp.dao

import androidx.room.*
import com.rmuhamed.sample.myselfiesapp.model.AuthenticatedUser

@Dao
interface AuthenticatedUsersDAO {

    @Query("SELECT * From AuthenticatedUser WHERE accessToken = :accessToken")
    fun retrieveUserBy(accessToken: String): AuthenticatedUser

    @Transaction
    fun setLoggedInUser(authenticatedUser: AuthenticatedUser) {
        deleteUsers()
        insertUser(authenticatedUser)
    }

    @Insert
    fun insertUser(authenticatedUser: AuthenticatedUser)

    @Update
    fun updateUser(authenticatedUser: AuthenticatedUser)

    @Query("DELETE FROM AuthenticatedUser")
    fun deleteUsers()
}