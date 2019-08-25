package com.rmuhamed.sample.myselfiesapp.repository

import com.rmuhamed.sample.myselfiesapp.cache.CacheDataSource
import com.rmuhamed.sample.myselfiesapp.cache.CacheDataSourceKeys
import com.rmuhamed.sample.myselfiesapp.model.AuthenticatedUser

open class BasicRepository(private val cacheDataSource: CacheDataSource) : IRepository {

    override fun getAuthenticatedCustomer() =
        cacheDataSource.retrieve(CacheDataSourceKeys.AUTHENTICATED_USER) as AuthenticatedUser
}