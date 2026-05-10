package pl.edu.ug.neuromapa.data.auth

object ProfilePhotoPicker {
    var launch: (() -> Unit)? = null
    var onResult: ((imageBytes: ByteArray?, errorMessage: String?) -> Unit)? = null
}
