package de.sorin.springbootkotlin.service

import com.google.common.hash.Hashing
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import de.sorin.springbootkotlin.repository.UrlShortenRepository
import java.nio.charset.StandardCharsets

@Service
class UrlShortenService(val urlShortenRepository: UrlShortenRepository) {

    private val log = LoggerFactory.getLogger(UrlShortenService::class.java)

    fun generateShortUrl(localUrl: String, longUrl: String): String {
        //Generate the hash
        var id = Hashing.murmur3_32().hashString(longUrl, StandardCharsets.UTF_8).toString()
        // Unorthodox method but limit it to 6 character
        id = id.take(6)
        //save the url with the id in the redis DB
        urlShortenRepository.saveUrl(id, longUrl)
        val baseUrl = formatLocalUrl(localUrl)
        val shortenedUrl = "$baseUrl$id"
        log.info("New shortened Url is: $shortenedUrl")
        return shortenedUrl
    }

    private fun formatLocalUrl(localUrl: String): String {
        val addressComponents = localUrl.split("/")
        val sb = StringBuilder()
        for (i in 0 until addressComponents.size - 1) {
            sb.append(addressComponents[i])
        }
        sb.append('/')
        return sb.toString()
    }
}