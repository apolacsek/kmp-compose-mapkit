package io.supercharge.example

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
