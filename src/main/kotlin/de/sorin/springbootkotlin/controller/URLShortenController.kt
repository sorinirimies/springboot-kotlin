package de.sorin.springbootkotlin.controller

import de.sorin.springbootkotlin.repository.UrlShortenRepository
import de.sorin.springbootkotlin.model.UrlShortenRequest
import de.sorin.springbootkotlin.service.UrlShortenService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import de.sorin.springbootkotlin.utils.validateUrl
import java.io.IOException
import java.net.URISyntaxException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.validation.Valid


@RestController
class URLShortenController(
    @Autowired val urlShortenService: UrlShortenService,
    @Autowired val urlShortenRepository: UrlShortenRepository
) {

    private val log = LoggerFactory.getLogger(URLShortenController::class.java)

    @RequestMapping(value = ["/{id}"], method = [RequestMethod.GET])
    @Throws(Exception::class)
    fun redirectUrl(@PathVariable id: String, resp: HttpServletResponse) {
        log.info("Redirect Url is: $id")
        val url = urlShortenRepository.getSavedUrl(id)
        if (url != null)
            resp.sendRedirect(url)
        else
            resp.sendError(HttpServletResponse.SC_NOT_FOUND)
    }

    @RequestMapping(method = [RequestMethod.POST], consumes = ["application/json"])
    @Throws(IOException::class, URISyntaxException::class, Exception::class)
    fun shortenUrl(@RequestBody @Valid urlShortenRequest: UrlShortenRequest, req: HttpServletRequest): ResponseEntity<String> {
        log.info("Url to shorten is: ${urlShortenRequest.url}")
        val longUrl = urlShortenRequest.url
        return if (validateUrl(longUrl)) {
            val localUrl = req.requestURL.toString()
            val shortUrl = urlShortenService.generateShortUrl(localUrl, longUrl)
            ResponseEntity(shortUrl, HttpStatus.OK)
        } else
            ResponseEntity(HttpStatus.BAD_REQUEST)
    }
}