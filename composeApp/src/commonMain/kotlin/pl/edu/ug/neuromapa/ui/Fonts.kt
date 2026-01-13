package pl.edu.ug.neuromapa.ui

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun getAppTypography(): Typography {

    val defaultFont = FontFamily.Default

    return Typography(
        titleLarge = TextStyle(
            fontFamily = defaultFont,
            fontWeight = FontWeight.Bold,
            fontSize = 36.sp,
            lineHeight = 40.sp
        ),
        titleMedium = TextStyle(
            fontFamily = defaultFont,
            fontWeight = FontWeight.Bold,
            fontSize = 32.sp,
            lineHeight = 32.sp
        ),
        titleSmall = TextStyle(
            fontFamily = defaultFont,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            lineHeight = 20.sp
        ),
        bodySmall = TextStyle(
            fontFamily = defaultFont,
            fontWeight = FontWeight.Normal,
            fontSize = 15.sp,
            lineHeight = 30.sp
        ),
        labelSmall = TextStyle(
            fontFamily = defaultFont,
            fontWeight = FontWeight.Bold,
            fontSize = 10.sp,
            lineHeight = 10.sp,
        )
    )
}