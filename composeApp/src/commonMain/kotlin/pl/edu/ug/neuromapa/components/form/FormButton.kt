package pl.edu.ug.neuromapa.components.form

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import pl.edu.ug.neuromapa.ui.Primary
import pl.edu.ug.neuromapa.ui.SurfaceDim
import pl.edu.ug.neuromapa.ui.getAppTypography
import pl.edu.ug.neuromapa.ui.onPrimary
import pl.edu.ug.neuromapa.ui.settings.globalComponentCornerRadius
import pl.edu.ug.neuromapa.ui.settings.globalComponentMediumPadding
import pl.edu.ug.neuromapa.ui.settings.globalComponentWidePadding

@Composable
fun FormButton(
    text: String,
    isPrimary: Boolean,
    modifier: Modifier = Modifier,
) {

    // This is a blueprint of a button that is being used in AddScreen.kt
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(globalComponentCornerRadius))
            .background(if (isPrimary) Primary else SurfaceDim)
            .padding(vertical = globalComponentMediumPadding, horizontal = globalComponentWidePadding),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = text,
            style = getAppTypography().titleSmall,
            color = onPrimary,
        )
    }

}