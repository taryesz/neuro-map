package pl.edu.ug.neuromapa

import androidx.compose.ui.window.ComposeUIViewController
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.readBytes
import kotlinx.cinterop.usePinned
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
import platform.darwin.NSObject

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
                picker.dismissViewControllerAnimated(true, null)

                val result = didFinishPicking.firstOrNull() as? PHPickerResult
                if (result == null) {
                    ProfilePhotoPicker.onResult?.invoke(null, "Nie wybrano zdjęcia.")
                    return
                }

                result.itemProvider.loadDataRepresentationForTypeIdentifier(
                    typeIdentifier = "public.jpeg"
                ) { data, _ ->
                    val imageData = data as? NSData
                    if (imageData != null) {
                        val bytes = imageData.toByteArray()
                        ProfilePhotoPicker.onResult?.invoke(bytes, null)
                    } else {
                        result.itemProvider.loadDataRepresentationForTypeIdentifier(
                            typeIdentifier = "public.heic"
                        ) { heicData, _ ->
                            val heicBytes = (heicData as? NSData)?.toByteArray()
                            ProfilePhotoPicker.onResult?.invoke(
                                heicBytes,
                                if (heicBytes == null) "Nie udało się załadować zdjęcia." else null
                            )
                        }
                    }
                }
            }
        }

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
