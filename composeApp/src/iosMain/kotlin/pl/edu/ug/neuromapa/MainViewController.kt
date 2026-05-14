package pl.edu.ug.neuromapa

import androidx.compose.ui.window.ComposeUIViewController
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.readBytes
import kotlinx.cinterop.useContents
import pl.edu.ug.neuromapa.data.auth.OAuthLauncher
import pl.edu.ug.neuromapa.data.auth.ProfilePhotoPicker
import pl.edu.ug.neuromapa.data.auth.SessionStorage
import pl.edu.ug.neuromapa.data.auth.StoredSession
import platform.Foundation.NSData
import platform.Foundation.NSURL
import platform.Foundation.NSUserDefaults
import platform.PhotosUI.PHPickerConfiguration
import platform.PhotosUI.PHPickerFilter
import platform.PhotosUI.PHPickerResult
import platform.PhotosUI.PHPickerViewController
import platform.PhotosUI.PHPickerViewControllerDelegateProtocol
import platform.UIKit.UIApplication
import platform.UIKit.UIImage
import platform.UIKit.UIImageJPEGRepresentation
import platform.darwin.NSObject
import platform.darwin.dispatch_async
import platform.darwin.dispatch_get_main_queue

private var currentPickerDelegate: PHPickerViewControllerDelegateProtocol? = null

@OptIn(ExperimentalForeignApi::class)
fun MainViewController() = ComposeUIViewController {

    val defaults = NSUserDefaults.standardUserDefaults

    SessionStorage.save = { token, refreshToken, email, userId, name, birthDate, photoUrl ->
        defaults.setObject(token, forKey = "token")
        defaults.setObject(refreshToken, forKey = "refreshToken")
        defaults.setObject(email, forKey = "email")
        defaults.setObject(userId, forKey = "userId")
        defaults.setObject(name, forKey = "name")
        defaults.setObject(birthDate, forKey = "birthDate")
        defaults.setObject(photoUrl, forKey = "photoUrl")
    }

    SessionStorage.load = {
        val token = defaults.stringForKey("token")
        val refreshToken = defaults.stringForKey("refreshToken")
        val email = defaults.stringForKey("email")
        val userId = defaults.stringForKey("userId")
        val name = defaults.stringForKey("name")
        val birthDate = defaults.stringForKey("birthDate")
        val photoUrl = defaults.stringForKey("photoUrl")

        if (token != null && email != null && userId != null) {
            StoredSession(token, refreshToken, email, userId, name, birthDate, photoUrl)
        } else {
            null
        }
    }

    SessionStorage.clear = {
        defaults.removeObjectForKey("token")
        defaults.removeObjectForKey("refreshToken")
        defaults.removeObjectForKey("email")
        defaults.removeObjectForKey("userId")
        defaults.removeObjectForKey("name")
        defaults.removeObjectForKey("birthDate")
        defaults.removeObjectForKey("photoUrl")
    }

    OAuthLauncher.launch = { provider ->
        val urlString = "https://mvcxlvcjcvcrjftbcvxp.supabase.co/auth/v1/authorize?provider=$provider&redirect_to=neuromapa://auth/callback"
        val url = NSURL(string = urlString)
        if (url != null) {
            UIApplication.sharedApplication.openURL(
                url = url,
                options = emptyMap<Any?, Any>(),
                completionHandler = null
            )
        }
    }

    ProfilePhotoPicker.launch = launch@{
        val topController = UIApplication.sharedApplication.keyWindow?.rootViewController
            ?: return@launch

        val config = PHPickerConfiguration()
        config.filter = PHPickerFilter.imagesFilter
        config.selectionLimit = 1

        val picker = PHPickerViewController(configuration = config)

        val delegate = object : NSObject(), PHPickerViewControllerDelegateProtocol {
            override fun picker(
                picker: PHPickerViewController,
                didFinishPicking: List<*>
            ) {
                currentPickerDelegate = null
                picker.dismissViewControllerAnimated(true, null)

                val result = didFinishPicking.firstOrNull() as? PHPickerResult
                if (result == null) {
                    ProfilePhotoPicker.onResult?.invoke(null, "Nie wybrano zdjęcia.")
                    return
                }

                val itemProvider = result.itemProvider
                val registeredTypes = itemProvider.registeredTypeIdentifiers.filterIsInstance<String>()
                val imageType = registeredTypes.firstOrNull {
                    it.contains("image") || it.contains("jpeg") || it.contains("png") || it.contains("heic")
                }

                if (imageType != null) {
                    itemProvider.loadDataRepresentationForTypeIdentifier(imageType) { data, _ ->
                        val nsData = data as? NSData
                        dispatch_async(dispatch_get_main_queue()) {
                            if (nsData != null) {
                                val rawImage = UIImage(data = nsData)
                                val resizedImage = rawImage?.resizeToLimit(1000.0)
                                val jpegData = resizedImage?.let { UIImageJPEGRepresentation(it, 0.5) }
                                val jpegBytes = jpegData?.toByteArray()

                                ProfilePhotoPicker.onResult?.invoke(
                                    jpegBytes,
                                    if (jpegBytes == null) "Błąd konwersji zdjęcia." else null
                                )
                            } else {
                                ProfilePhotoPicker.onResult?.invoke(null, "Nie udało się załadować danych zdjęcia.")
                            }
                        }
                    }
                } else {
                    ProfilePhotoPicker.onResult?.invoke(null, "Wybrany plik nie jest obsługiwanym obrazem.")
                }
            }
        }

        currentPickerDelegate = delegate
        picker.delegate = delegate
        topController.presentViewController(picker, animated = true, completion = null)
    }

    App()
}

@OptIn(ExperimentalForeignApi::class)
private fun NSData.toByteArray(): ByteArray {
    val length = this.length.toInt()
    if (length == 0) return ByteArray(0)
    val bytes = this.bytes ?: return ByteArray(0)
    return bytes.readBytes(length)
}

@OptIn(ExperimentalForeignApi::class)
private fun UIImage.resizeToLimit(maxDimension: Double): UIImage {
    val width = size.useContents { width }
    val height = size.useContents { height }

    if (width <= maxDimension && height <= maxDimension) return this

    val ratio = width / height
    val (newWidth, newHeight) = if (width > height) {
        maxDimension to (maxDimension / ratio)
    } else {
        (maxDimension * ratio) to maxDimension
    }

    platform.UIKit.UIGraphicsBeginImageContextWithOptions(
        platform.CoreGraphics.CGSizeMake(newWidth, newHeight),
        false,
        1.0
    )
    drawInRect(platform.CoreGraphics.CGRectMake(0.0, 0.0, newWidth, newHeight))
    val resizedImage = platform.UIKit.UIGraphicsGetImageFromCurrentImageContext()
    platform.UIKit.UIGraphicsEndImageContext()

    return resizedImage ?: this
}