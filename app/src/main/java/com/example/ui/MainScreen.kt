package com.example.ui

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.outlined.Calculate
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Speed
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.screens.DpiCalculatorScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.SensitivityScreen
import com.example.ui.theme.BackgroundDark
import com.example.ui.theme.BorderCyan
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.SurfaceDark
import com.example.ui.theme.SurfaceElevatedDark
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.util.ShareUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: MainViewModel = viewModel()
) {
    val context = LocalContext.current
    val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()
    val isHindi by viewModel.isHindi.collectAsStateWithLifecycle()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = BackgroundDark,
        contentWindowInsets = WindowInsets.safeDrawing,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = when (currentScreen) {
                            AppScreen.HOME -> if (isHindi) "हेडशॉट सेंसि टूल" else "Headshot Sensi"
                            AppScreen.SENSITIVITY -> if (isHindi) "सेंसिटिविटी सेटिंग्स" else "Sensitivity Lab"
                            AppScreen.DPI_CALCULATOR -> if (isHindi) "DPI कैलकुलेटर" else "DPI & Touch Tool"
                            AppScreen.PROFILE -> if (isHindi) "गेमर प्रोफ़ाइल" else "Gamer Profile"
                        },
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Black,
                        color = TextPrimary
                    )
                },
                actions = {
                    IconButton(
                        onClick = { viewModel.toggleLanguage() },
                        modifier = Modifier.testTag("action_lang_toggle")
                    ) {
                        Text(
                            text = if (isHindi) "ENG" else "हिं",
                            color = CyberCyan,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.labelLarge
                        )
                    }

                    IconButton(
                        onClick = {
                            val activeSensi = viewModel.sensiState.value
                            ShareUtils.sharePreset(
                                context,
                                com.example.data.local.SensitivityPreset(
                                    name = activeSensi.name,
                                    brand = activeSensi.brand,
                                    gameMode = activeSensi.gameMode,
                                    general = activeSensi.general,
                                    redDot = activeSensi.redDot,
                                    scope2x = activeSensi.scope2x,
                                    scope4x = activeSensi.scope4x,
                                    sniperScope = activeSensi.sniperScope,
                                    freeLook = activeSensi.freeLook,
                                    fireButtonSize = activeSensi.fireButtonSize,
                                    recommendedDpi = activeSensi.recommendedDpi,
                                    notes = activeSensi.notes
                                )
                            )
                        },
                        modifier = Modifier.testTag("action_share_top")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Share",
                            tint = CyberCyan
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = SurfaceDark,
                    titleContentColor = TextPrimary
                )
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = SurfaceDark,
                contentColor = TextPrimary,
                modifier = Modifier
                    .windowInsetsPadding(WindowInsets.navigationBars)
                    .border(1.dp, BorderCyan.copy(alpha = 0.2f))
            ) {
                NavigationBarItem(
                    selected = currentScreen == AppScreen.HOME,
                    onClick = { viewModel.navigateTo(AppScreen.HOME) },
                    icon = {
                        Icon(
                            imageVector = if (currentScreen == AppScreen.HOME) Icons.Filled.Home else Icons.Outlined.Home,
                            contentDescription = "Home"
                        )
                    },
                    label = {
                        Text(
                            text = if (isHindi) AppScreen.HOME.titleHi else AppScreen.HOME.titleEn,
                            fontWeight = if (currentScreen == AppScreen.HOME) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 11.sp
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = BackgroundDark,
                        selectedTextColor = CyberCyan,
                        indicatorColor = CyberCyan,
                        unselectedIconColor = TextMuted,
                        unselectedTextColor = TextMuted
                    ),
                    modifier = Modifier.testTag("nav_home")
                )

                NavigationBarItem(
                    selected = currentScreen == AppScreen.SENSITIVITY,
                    onClick = { viewModel.navigateTo(AppScreen.SENSITIVITY) },
                    icon = {
                        Icon(
                            imageVector = if (currentScreen == AppScreen.SENSITIVITY) Icons.Filled.Tune else Icons.Outlined.Tune,
                            contentDescription = "Sensitivity"
                        )
                    },
                    label = {
                        Text(
                            text = if (isHindi) AppScreen.SENSITIVITY.titleHi else AppScreen.SENSITIVITY.titleEn,
                            fontWeight = if (currentScreen == AppScreen.SENSITIVITY) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 11.sp
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = BackgroundDark,
                        selectedTextColor = CyberCyan,
                        indicatorColor = CyberCyan,
                        unselectedIconColor = TextMuted,
                        unselectedTextColor = TextMuted
                    ),
                    modifier = Modifier.testTag("nav_sensi")
                )

                NavigationBarItem(
                    selected = currentScreen == AppScreen.DPI_CALCULATOR,
                    onClick = { viewModel.navigateTo(AppScreen.DPI_CALCULATOR) },
                    icon = {
                        Icon(
                            imageVector = if (currentScreen == AppScreen.DPI_CALCULATOR) Icons.Filled.Calculate else Icons.Outlined.Calculate,
                            contentDescription = "DPI"
                        )
                    },
                    label = {
                        Text(
                            text = if (isHindi) AppScreen.DPI_CALCULATOR.titleHi else AppScreen.DPI_CALCULATOR.titleEn,
                            fontWeight = if (currentScreen == AppScreen.DPI_CALCULATOR) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 11.sp
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = BackgroundDark,
                        selectedTextColor = CyberCyan,
                        indicatorColor = CyberCyan,
                        unselectedIconColor = TextMuted,
                        unselectedTextColor = TextMuted
                    ),
                    modifier = Modifier.testTag("nav_dpi")
                )

                NavigationBarItem(
                    selected = currentScreen == AppScreen.PROFILE,
                    onClick = { viewModel.navigateTo(AppScreen.PROFILE) },
                    icon = {
                        Icon(
                            imageVector = if (currentScreen == AppScreen.PROFILE) Icons.Filled.Person else Icons.Outlined.Person,
                            contentDescription = "Profile"
                        )
                    },
                    label = {
                        Text(
                            text = if (isHindi) AppScreen.PROFILE.titleHi else AppScreen.PROFILE.titleEn,
                            fontWeight = if (currentScreen == AppScreen.PROFILE) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 11.sp
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = BackgroundDark,
                        selectedTextColor = CyberCyan,
                        indicatorColor = CyberCyan,
                        unselectedIconColor = TextMuted,
                        unselectedTextColor = TextMuted
                    ),
                    modifier = Modifier.testTag("nav_profile")
                )
            }
        }
    ) { innerPadding ->
        Crossfade(
            targetState = currentScreen,
            modifier = Modifier.padding(innerPadding),
            label = "ScreenTransition"
        ) { screen ->
            when (screen) {
                AppScreen.HOME -> HomeScreen(viewModel = viewModel)
                AppScreen.SENSITIVITY -> SensitivityScreen(viewModel = viewModel)
                AppScreen.DPI_CALCULATOR -> DpiCalculatorScreen(viewModel = viewModel)
                AppScreen.PROFILE -> ProfileScreen(viewModel = viewModel)
            }
        }
    }
}
