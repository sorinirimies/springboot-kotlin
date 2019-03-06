package de.sorin.springbootkotlin.repository

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Repository

@Repository
class UrlShortenRepository {
    @Autowired
    lateinit var redis: StringRedisTemplate

    fun saveUrl(id: String, url: String) = redis.opsForValue().set(id, url)

    fun getSavedUrl(id: String): String? = redis.opsForValue().get(id)

}