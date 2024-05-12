package com.example.donappt5.views

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.donappt5.R


val provider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)

val fontName = GoogleFont("Roboto")

val fontFamily = FontFamily(
    Font(googleFont = fontName, fontProvider = provider)
)

@Immutable
data class CustomColors(
    val primary: Color,
    val onPrimary: Color,
    val primaryDark: Color,
    val onPrimaryDark: Color,
    val primaryLight: Color,
    val onPrimaryLight: Color,
    val secondary: Color,
    val onSecondary: Color,
    val secondaryLight: Color,
    val onSecondaryLight: Color,
    val secondaryDark: Color,
    val onSecondaryDark: Color,
    val background: Color,
    val onBackground: Color,
    val surface: Color,
    val onSurface: Color
)

@Immutable
data class CustomTypography(
    val body: TextStyle,
    val title: TextStyle,
    val header: TextStyle
)


@Immutable
data class CustomElevation(
    val default: Dp,
    val pressed: Dp
)

val LocalCustomColors = staticCompositionLocalOf {
    CustomColors(
        primary = Color.Unspecified,
        onPrimary = Color.Unspecified,
        primaryDark = Color.Unspecified,
        onPrimaryDark = Color.Unspecified,
        primaryLight = Color.Unspecified,
        onPrimaryLight = Color.Unspecified,
        secondary = Color.Unspecified,
        onSecondary = Color.Unspecified,
        secondaryLight = Color.Unspecified,
        onSecondaryLight = Color.Unspecified,
        secondaryDark = Color.Unspecified,
        onSecondaryDark = Color.Unspecified,
        background = Color.Unspecified,
        onBackground = Color.Unspecified,
        surface = Color.Unspecified,
        onSurface = Color.Unspecified,
    )
}
val LocalCustomTypography = staticCompositionLocalOf {
    CustomTypography(
        body = TextStyle.Default,
        title = TextStyle.Default,
        header = TextStyle.Default
    )
}

val LocalCustomElevation = staticCompositionLocalOf {
    CustomElevation(
        default = Dp.Unspecified,
        pressed = Dp.Unspecified
    )
}

@Composable
fun CustomTheme(
    /* ... */
    content: @Composable () -> Unit
) {
    val customColors = CustomColors(
        primary = Color(0xFF67AB8D),
        onPrimary = Color(0xFFFFFFFF),
        primaryDark = Color(0xFF3A7354),
        onPrimaryDark = Color(0xFFFFFFFF),
        primaryLight = Color(0xFF8ABEAA),
        onPrimaryLight = Color(0xFFFFFFFF),
        secondary = Color(0xFF97C3F3),
        onSecondary = Color(0xFF000000),
        secondaryLight = Color(0x5597C3F3),
        onSecondaryLight = Color(0xFF000000),
        secondaryDark = Color(0xFF3787E5),
        onSecondaryDark = Color(0xFFFFFFFF),
        background = Color(0xFFF9FAFF),
        onBackground = Color(0xFF3787E5),
        surface = Color(0xFFFFFFFF),
        onSurface = Color(0xFF000000),
    )
    val customTypography = CustomTypography(
        body = TextStyle(fontSize = 16.sp, fontFamily = fontFamily),
        title = TextStyle(fontSize = 20.sp, fontFamily = fontFamily),
        header = TextStyle(fontSize = 24.sp, fontFamily = fontFamily)
    )
    val customElevation = CustomElevation(
        default = 4.dp,
        pressed = 8.dp
    )
    CompositionLocalProvider(
        LocalCustomColors provides customColors,
        LocalCustomTypography provides customTypography,
        LocalCustomElevation provides customElevation,
        content = content
    )
}

// Use with eg. CustomTheme.elevation.small
object CustomTheme {
    val colors: CustomColors
        @Composable
        get() = LocalCustomColors.current
    val typography: CustomTypography
        @Composable
        get() = LocalCustomTypography.current
    val elevation: CustomElevation
        @Composable
        get() = LocalCustomElevation.current
}