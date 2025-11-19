package pl.edu.ug.neuromapa

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform