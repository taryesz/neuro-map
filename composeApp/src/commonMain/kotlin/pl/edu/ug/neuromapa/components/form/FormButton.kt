package pl.edu.ug.neuromapa.components.form

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import pl.edu.ug.neuromapa.ui.getAppTypography
import pl.edu.ug.neuromapa.ui.settings.globalComponentCornerRadius
import pl.edu.ug.neuromapa.ui.settings.globalComponentMediumPadding
import pl.edu.ug.neuromapa.ui.settings.globalComponentWidePadding

@Composable
fun FormButton(
    text: String? = null,
    isPrimary: Boolean = true,
    modifier: Modifier = Modifier,
    icon: DrawableResource? = null,
    containerColor: Color? = null
) {

    if (icon != null) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(globalComponentCornerRadius))
                .background(Color.White)
                .padding(horizontal = globalComponentWidePadding),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(icon),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .clip(RoundedCornerShape(globalComponentCornerRadius))
            )
        }
    }
    else if (text != null) {

        Row(
            modifier = modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(globalComponentCornerRadius))
                .background(
                    containerColor ?: if (isPrimary) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceBright)
                .padding(vertical = globalComponentMediumPadding, horizontal = globalComponentWidePadding),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = text,
                style = getAppTypography().titleSmall,
                color = MaterialTheme.colorScheme.onPrimary,
            )
        }
    }
}