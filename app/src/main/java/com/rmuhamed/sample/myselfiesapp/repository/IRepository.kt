package com.rmuhamed.sample.myselfiesapp.repository

import com.rmuhamed.sample.myselfiesapp.model.AuthenticatedUser

interface IRepository {

    fun getAuthenticatedCustomer(): AuthenticatedUser
}
