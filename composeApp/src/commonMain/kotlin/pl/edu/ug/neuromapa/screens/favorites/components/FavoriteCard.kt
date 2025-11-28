package pl.edu.ug.neuromapa.screens.favorites.components

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import pl.edu.ug.neuromapa.screens.favorites.settings.bodyWidePadding
import pl.edu.ug.neuromapa.screens.favorites.settings.buttonCornerRadius
import pl.edu.ug.neuromapa.screens.favorites.settings.scrimColor
import pl.edu.ug.neuromapa.screens.place.models.Place
import kotlin.math.abs

@Composable
fun FavoriteCard(
    place: Place,
    onRemove: (Place) -> Unit,
    onNavigate: (Place) -> Unit,
    modifier: Modifier = Modifier,
    onClick: (Place) -> Unit
) {
    val offsetX = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()

    val maxOffset = 220f

    val actionsOpened = remember { mutableStateOf(false) }

    LaunchedEffect(actionsOpened.value) {
        if (actionsOpened.value) {
            delay(4000)
            scope.launch { offsetX.animateTo(0f) }
            actionsOpened.value = false
        }
    }


    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(160.dp)
            .padding(horizontal = bodyWidePadding)
    ) {

        if (offsetX.value != 0f) {
            DismissBackground(
                offset = offsetX.value,
                onConfirmDelete = {
                    actionsOpened.value = false
                    onRemove(place)
                    scope.launch { offsetX.animateTo(0f) }
                },
                onConfirmNavigate = {
                    actionsOpened.value = false
                    onNavigate(place)
                    scope.launch { offsetX.animateTo(0f) }
                }
            )
        }

        Box(
            modifier = Modifier
                .offset { IntOffset(offsetX.value.toInt(), 0) }
                .draggable(
                    orientation = Orientation.Horizontal,
                    state = rememberDraggableState { delta ->
                        val newOffset = offsetX.value + delta
                        if (newOffset in -maxOffset..maxOffset) {
                            scope.launch { offsetX.snapTo(newOffset) }
                        }
                        actionsOpened.value = false
                    },
                    onDragStopped = {
                        if (abs(offsetX.value) < maxOffset * 0.25f) {
                            scope.launch { offsetX.animateTo(0f) }
                        }else{
                                actionsOpened.value = true
                            }
                    }
                )
                .clip(RoundedCornerShape(buttonCornerRadius))
                .clickable {
                    if (offsetX.value != 0f) {
                        actionsOpened.value = false
                        scope.launch { offsetX.animateTo(0f) }
                    } else {
                        onClick(place)
                    }
                }
        ) {
            Image(
                painter = painterResource(place.photo),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(0.67f)
                    .background(scrimColor)
                    .align(Alignment.CenterEnd)
            )

            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(0.67f)
                    .align(Alignment.CenterEnd)
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = place.name,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    lineHeight = 22.sp,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
