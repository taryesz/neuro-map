package pl.edu.ug.neuromapa.data

object OAuthLauncher {
    var launch: ((provider: String) -> Unit)? = null
}

object OAuthResultHandler {
    var handle: ((accessToken: String) -> Unit)? = null
}
