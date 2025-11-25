package pl.edu.ug.neuromapa.screens.add.components

import SelectionItem
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pl.edu.ug.neuromapa.ui.getAppTypography

@Composable
fun FormSelection(
    title: String,
    items: List<SelectionItem>,
    selectedItems: Set<String>,
    onSelectionChange: (Set<String>) -> Unit,
    showDescription: Boolean = false,
    description: String? = null,
) {

    // Categories / sensory properties / excellences by 3 items in each row
    val chunkedItems = remember(items) { items.chunked(3) }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(15.dp),
    )
    {

        // Form title
        Text(
            text = title,
            style = getAppTypography().titleMedium,
        )

        // Clickable icons
        chunkedItems.forEach { rowItems ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 15.dp),   // Padding between rows
                horizontalArrangement = Arrangement.spacedBy(15.dp)
            ) {

                for (item in rowItems) {

                    val isSelected = selectedItems.contains(item.name)
                    val iconToShow = if (isSelected) item.iconDark else item.iconLight

                    Box(
                        modifier = Modifier.weight(1f),
                        contentAlignment = Alignment.TopCenter
                    ) {
                        CategoryItem(
                            icon = iconToShow,
                            iconDescription = item.description,
                            name = item.name,
                            onClick = {
                                val newSelection = if (selectedItems.contains(item.name)) {
                                    selectedItems - item.name
                                } else {
                                    selectedItems + item.name
                                }
                                onSelectionChange(newSelection)
                            }
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