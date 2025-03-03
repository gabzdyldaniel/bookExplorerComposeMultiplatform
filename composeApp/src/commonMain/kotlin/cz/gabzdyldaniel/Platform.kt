package cz.gabzdyldaniel

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform