package pl.edu.ug.neuromapa.screens.add.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import pl.edu.ug.neuromapa.screens.add.settings.cornerRadius
import pl.edu.ug.neuromapa.screens.add.settings.mediumPadding
import pl.edu.ug.neuromapa.screens.add.settings.widePadding
import pl.edu.ug.neuromapa.ui.Primary
import pl.edu.ug.neuromapa.ui.SurfaceDim
import pl.edu.ug.neuromapa.ui.getAppTypography
import pl.edu.ug.neuromapa.ui.onPrimary

@Composable
fun FormButton(
    text: String,
    isPrimary: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {

    // This is a blueprint of a button that is being used in AddScreen.kt
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(cornerRadius))
            .clickable(onClick = onClick)
            .background(if (isPrimary) Primary else SurfaceDim)
            .padding(vertical = mediumPadding, horizontal = widePadding),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = text,
            style = getAppTypography().titleSmall,
            color = onPrimary,
        )
    }

}