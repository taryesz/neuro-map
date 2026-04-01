package pl.edu.ug.neuromapa.screens.account.auth.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.style.TextDecoration
import pl.edu.ug.neuromapa.ui.getAppTypography
import pl.edu.ug.neuromapa.ui.onBackground

@Composable
fun AuthSwitch(
    text: String,
    linkText: String,
    onLinkClick: () -> Unit
) {

    val haptic = LocalHapticFeedback.current

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
    ) {

        Text(
            text = text,
            color = onBackground,
            style = getAppTypography().bodySmall,
        )

        Text(
            text = linkText,
            color = onBackground,
            style = getAppTypography().bodySmall,
            modifier = Modifier
                .clickable(
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        onLinkClick()
                    },
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                ),
            textDecoration = TextDecoration.Underline
        )

    }

}