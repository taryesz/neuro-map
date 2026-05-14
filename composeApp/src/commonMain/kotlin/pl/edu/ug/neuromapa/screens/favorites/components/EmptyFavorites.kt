import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pl.edu.ug.neuromapa.ui.getAppTypography
import androidx.compose.material3.Text
import androidx.compose.ui.text.style.TextAlign
import neuromapa.composeapp.generated.resources.Res
import neuromapa.composeapp.generated.resources.favorites_screen_empty_body_text
import neuromapa.composeapp.generated.resources.favorites_screen_empty_body_title
import neuromapa.composeapp.generated.resources.favorites_screen_go_to_map_button_text
import org.jetbrains.compose.resources.stringResource
import pl.edu.ug.neuromapa.components.form.FormButton
import pl.edu.ug.neuromapa.screens.add.settings.cornerRadius
import pl.edu.ug.neuromapa.ui.animations.bounceClick
import pl.edu.ug.neuromapa.ui.settings.globalComponentMediumPadding
import pl.edu.ug.neuromapa.ui.settings.globalComponentWidePadding

@Composable
fun EmptyFavorites(
    onGoToMap: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.FavoriteBorder,
            contentDescription = null,
            modifier = Modifier.size(100.dp),
            tint = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)
        )

        Spacer(modifier = Modifier.height(globalComponentMediumPadding))

        Text(
            text = stringResource(Res.string.favorites_screen_empty_body_title),
            style = getAppTypography().titleMedium,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(globalComponentWidePadding))

        Text(
            text = stringResource(Res.string.favorites_screen_empty_body_text),
            style = getAppTypography().bodySmall,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(vertical = globalComponentMediumPadding)
        )

        Spacer(modifier = Modifier.height(globalComponentWidePadding))

        Box(
            modifier = Modifier.bounceClick {
                onGoToMap()
            }
        ) {
            FormButton(
                text = stringResource(Res.string.favorites_screen_go_to_map_button_text),
                isPrimary = true
            )
        }

    }
}