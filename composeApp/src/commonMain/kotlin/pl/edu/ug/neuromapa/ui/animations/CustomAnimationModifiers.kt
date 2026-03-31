package pl.edu.ug.neuromapa.ui.animations

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import kotlinx.coroutines.launch
import pl.edu.ug.neuromapa.ui.settings.globalButtonBackgroundAlphaWhenNotSelected
import pl.edu.ug.neuromapa.ui.settings.globalButtonBackgroundAlphaWhenSelected
import pl.edu.ug.neuromapa.ui.settings.globalButtonScaleWhenNotPressed
import pl.edu.ug.neuromapa.ui.settings.globalButtonScaleWhenPressed

fun Modifier.bounceClick(onClick: () -> Unit): Modifier = composed {

    // This will allow us to control the animations to the full extent
    val scale = remember { Animatable(1f) }
    val scope = rememberCoroutineScope()

    // This new modifier is a combination of:
    this
        .scale(scale.value)
        .pointerInput(Unit) {
            detectTapGestures(
                onPress = {
                    scope.launch { scale.animateTo(globalButtonScaleWhenPressed) }
                    tryAwaitRelease()
                    scope.launch { scale.animateTo(globalButtonScaleWhenNotPressed) }
                },
                onTap = {
                    onClick()
                }
            )
        }
}

fun Modifier.animatedSelectionBackground(
    isSelected: Boolean,
    backgroundColor: Color,
): Modifier = composed {

    val alpha by animateFloatAsState(
        targetValue = if (isSelected) globalButtonBackgroundAlphaWhenSelected else globalButtonBackgroundAlphaWhenNotSelected,
        label = "selectionAlphaAnim"
    )

    this
        .background(backgroundColor.copy(alpha = alpha))
}