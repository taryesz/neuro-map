package pl.edu.ug.neuromapa.screens.account.auth.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import neuromapa.composeapp.generated.resources.Res
import neuromapa.composeapp.generated.resources.logo_neuromap_alttext
import neuromapa.composeapp.generated.resources.logo_neuromap_dark
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import pl.edu.ug.neuromapa.ui.getAppTypography
import pl.edu.ug.neuromapa.ui.settings.globalComponentLogoImageHeight
import pl.edu.ug.neuromapa.ui.settings.globalComponentWidePadding

@Composable
fun AuthFormHeader(
    title: String,
) {

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(globalComponentWidePadding)
    ) {

        // Logo at the top of the screen
        Image(
            painter = painterResource(Res.drawable.logo_neuromap_dark),
            contentDescription = stringResource(Res.string.logo_neuromap_alttext),
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .height(globalComponentLogoImageHeight)
                .fillMaxWidth()
        )

        // Section title ("Sign up" / "Sign in")
        Text(
            text = title,
            color = MaterialTheme.colorScheme.onBackground,
            style = getAppTypography().titleMedium,
        )

    }

}