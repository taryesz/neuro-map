package pl.edu.ug.neuromapa.screens.favorites.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.painterResource
import pl.edu.ug.neuromapa.screens.favorites.settings.bodyWidePadding
import pl.edu.ug.neuromapa.screens.favorites.settings.buttonCornerRadius
import pl.edu.ug.neuromapa.screens.place.models.Place // ВИКОРИСТОВУЄМО ТИП PLACE

@Composable
fun FavoriteCard(
    place: Place,
    modifier: Modifier = Modifier,
) {
    // ⭐️ Колір затемнення #00000080 — це чорний із alpha 50% (80/FF = 0.5)
    val scrimColor = Color(0x80000000)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(160.dp)
            .padding(horizontal = bodyWidePadding)
            .clip(RoundedCornerShape(buttonCornerRadius))
    ) {
        // 1. ФОНОВЕ ЗОБРАЖЕННЯ НА ВСЮ ШИРИНУ
        Image(
            painter = painterResource(place.photo),
            contentDescription = "Favorite place photo ${place.name}",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize() // ⭐️ Займає 100% площі
        )

        // 2. ЗАТЕМНЕННЯ (SCrim) - ТІЛЬКИ НА 2/3 ПРАВОЇ ЧАСТИНИ
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(0.67f) // ⭐️ Займає 67% ширини
                .background(scrimColor) // Напівпрозорий чорний колір
                .align(Alignment.CenterEnd) // Вирівнюємо праворуч
        )

        // 3. КОНТЕНТ (ТЕКСТ) - Розміщуємо поверх затемнення
        Column(
            modifier = Modifier
                // ⭐️ КОЛОНКА ТЕЖ ПОВИННА БУТИ 2/3 ШИРИНИ ТА ПРАВОРУЧ, щоб лежати над скремом
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