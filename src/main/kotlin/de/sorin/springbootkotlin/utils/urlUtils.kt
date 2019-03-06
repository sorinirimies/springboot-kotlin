package de.sorin.springbootkotlin.utils

import java.util.regex.Pattern

private val URL_PATTERN =
    Pattern.compile("^(http:\\/\\/www\\.|https:\\/\\/www\\.|http:\\/\\/|https:\\/\\/)?[a-z0-9]+([\\-\\.]{1}[a-z0-9]+)*\\.[a-z]{2,5}(:[0-9]{1,5})?(\\/.*)?$")

fun validateUrl(url: String) = URL_PATTERN.matcher(url).matches()