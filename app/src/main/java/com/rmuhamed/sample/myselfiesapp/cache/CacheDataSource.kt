package com.rmuhamed.sample.myselfiesapp.cache

object CacheDataSource {

    private val internalValues = HashMap<String, Any>()


    fun <T : Any> save(key: String, value: T) {
        internalValues.put(key, value)
    }

    fun retrieve(key: String): Any? = internalValues.get(key)
}