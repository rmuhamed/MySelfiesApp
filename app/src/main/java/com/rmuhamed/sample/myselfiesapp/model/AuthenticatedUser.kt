package com.rmuhamed.sample.myselfiesapp.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "AuthenticatedUser")
data class AuthenticatedUser(
    @PrimaryKey @ColumnInfo(name = "accessToken") val accessToken: String,
    @ColumnInfo(name = "userName") val userName: String
);
