package pl.edu.ug.neuromapa.components.form

import pl.edu.ug.neuromapa.screens.add.data.SelectionItem
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import pl.edu.ug.neuromapa.ui.animations.bounceClick
import pl.edu.ug.neuromapa.ui.getAppTypography
import pl.edu.ug.neuromapa.ui.settings.globalComponentMediumPadding
import pl.edu.ug.neuromapa.ui.settings.globalComponentMediumSpacing

@Composable
fun FormSelection(
    title: String,
    items: List<SelectionItem>,
    selectedItems: Set<String>,
    onSelectionChange: (Set<String>) -> Unit,
    showDescription: Boolean = false,
    description: String? = null,
    isSingleSelection: Boolean = false
) {

    // Divide categories / sensory properties / excellences by 3 items per row
    val chunkedItems = remember(items) { items.chunked(3) }

    // Wrapper
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(globalComponentMediumSpacing),
    )
    {

        // Filed title
        Text(
            text = title,
            style = getAppTypography().titleMedium,
        )

        // Buttons
        chunkedItems.forEach { rowItems ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = globalComponentMediumPadding),   // Padding between rows
                horizontalArrangement = Arrangement.spacedBy(globalComponentMediumSpacing)
            ) {

                for (item in rowItems) {

                    val isSelected = selectedItems.contains(item.name)
                    val iconToShow = if (isSelected) item.iconDark else item.iconLight

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .bounceClick(onClick = {
                                val newSelection = if (isSingleSelection) {

                                    // Only one option can be clicked and chosen at a time
                                    if (selectedItems.contains(item.name)) {
                                        emptySet()  // If it was already chosen, unchoose it
                                    } else {
                                        // If a new option was chosen, overwrite the old one with the new one
                                        setOf(item.name)
                                    }
                                } else {
                                    // Multiple options can be clicked and chosen
                                    if (selectedItems.contains(item.name)) {
                                        selectedItems - item.name
                                    } else {
                                        selectedItems + item.name
                                    }
                                }
                                onSelectionChange(newSelection)
                            }),
                        contentAlignment = Alignment.TopCenter
                    ) {
                        SelectionItem(
                            icon = iconToShow,
                            iconDescription = item.description,
                            name = item.name,
                        )
                    }
                }

                // Fill empty spaces in a row with invisible items
                val missingItems = 3 - rowItems.size
                repeat(missingItems) {
                    Spacer(modifier = Modifier.weight(1f))
                }

            }
        }

        // Optional description
        if(showDescription) {
            if (description != null) {
                Text(
                    text = description,
                    style = getAppTypography().bodySmall
                )
            }
        }

    }
}