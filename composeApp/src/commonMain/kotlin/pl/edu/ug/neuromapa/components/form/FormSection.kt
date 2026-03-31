package pl.edu.ug.neuromapa.components.form

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import pl.edu.ug.neuromapa.ui.getAppTypography
import pl.edu.ug.neuromapa.ui.settings.globalComponentCornerRadius
import pl.edu.ug.neuromapa.ui.settings.globalComponentNarrowPadding

@Composable
fun FormSection(
    title: String,
    placeholder: String,
    value: String,
    onValueChange: (String) -> Unit
) {

    // Wrapper
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
    )
    {

        // Field title
        Text(
            text = title,
            style = getAppTypography().titleMedium,
        )

        // Field wrapper
        Row(
            modifier = Modifier
                .padding(vertical = globalComponentNarrowPadding)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        )
        {

            // Field
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(globalComponentCornerRadius))
                    .padding(vertical = globalComponentNarrowPadding)
                    .weight(1f),
                contentAlignment = Alignment.CenterStart
            )
            {

                // Show placeholder if no value is input
                if (value.isEmpty()) {
                    Text(
                        text = placeholder,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                        style = getAppTypography().bodySmall,
                    )
                }

                // Fill the field with whatever the user input
                BasicTextField(
                    value = value,
                    onValueChange = onValueChange,
                    textStyle = getAppTypography().bodySmall.copy(
                        color = MaterialTheme.colorScheme.onBackground
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = false
                )

            }

        }

    }

}