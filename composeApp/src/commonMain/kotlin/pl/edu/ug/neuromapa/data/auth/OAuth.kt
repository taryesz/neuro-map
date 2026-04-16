package pl.edu.ug.neuromapa.data.auth

object OAuthLauncher {
    var launch: ((provider: String) -> Unit)? = null
}

object OAuthResultHandler {
    var handle: ((accessToken: String, error: String?) -> Unit)? = null
}
