package pl.edu.ug.neuromapa.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.Font
import neuromapa.composeapp.generated.resources.Res
import neuromapa.composeapp.generated.resources.inter_18pt_bold
import neuromapa.composeapp.generated.resources.inter_18pt_regular
import neuromapa.composeapp.generated.resources.inter_18pt_semibold

@Composable
fun getInterFontFamily(): FontFamily {
    return FontFamily(
        Font(resource = Res.font.inter_18pt_bold, weight = FontWeight.Bold),
        Font(resource = Res.font.inter_18pt_semibold, weight = FontWeight.SemiBold),
        Font(resource = Res.font.inter_18pt_regular, weight = FontWeight.Normal),
    )
}

@Composable
fun getAppTypography(): Typography {
    val inter = getInterFontFamily()

    return Typography(
        titleLarge = TextStyle(fontFamily = inter,
            fontWeight = FontWeight.Bold,
            fontSize = 36.sp,
            lineHeight = 40.sp
        ),
        titleMedium = TextStyle(fontFamily = inter,
            fontWeight = FontWeight.Bold,
            fontSize = 32.sp,
            lineHeight = 32.sp
        ),
        titleSmall = TextStyle(fontFamily = inter,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            lineHeight = 20.sp
        ),
        bodySmall = TextStyle(fontFamily = inter,
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
            lineHeight = 30.sp
        ),
        labelSmall = TextStyle(fontFamily = inter,
            fontWeight = FontWeight.Bold,
            fontSize = 10.sp,
            lineHeight = 10.sp,
        )
    )

}