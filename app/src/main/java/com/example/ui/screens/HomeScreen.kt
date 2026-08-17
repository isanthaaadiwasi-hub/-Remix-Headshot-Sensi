package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DeviceUnknown
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.R
import com.example.data.local.SensitivityPreset
import com.example.ui.AppScreen
import com.example.ui.MainViewModel
import com.example.ui.components.CyberCard
import com.example.ui.components.GlowButton
import com.example.ui.theme.BackgroundDark
import com.example.ui.theme.BorderCyan
import com.example.ui.theme.BorderCrimson
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.FireGradientEnd
import com.example.ui.theme.FireGradientStart
import com.example.ui.theme.GamerAmber
import com.example.ui.theme.MatrixGreen
import com.example.ui.theme.NeonCrimson
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.SurfaceDark
import com.example.ui.theme.SurfaceElevatedDark
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.util.ShareUtils

@Composable
fun HomeScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val isHindi by viewModel.isHindi.collectAsStateWithLifecycle()
    val presets by viewModel.presets.collectAsStateWithLifecycle()
    val currentSensi by viewModel.sensiState.collectAsStateWithLifecycle()
    val profile by viewModel.userProfile.collectAsStateWithLifecycle()
    val specs = viewModel.deviceSpecs

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundDark)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(8.dp))
            // Hero Banner Card with Gamer Avatar
            HeroBannerCard(
                gamerTag = profile?.gamerTag ?: "PRO_WARRIOR",
                deviceModel = "${specs.manufacturer} ${specs.model}",
                isHindi = isHindi,
                onLanguageToggle = { viewModel.toggleLanguage() }
            )
        }

        // Live Device Stats HUD
        item {
            DeviceStatsHUD(specs = specs, isHindi = isHindi)
        }

        // 1-Tap Auto Headshot Sensi Booster
        item {
            CyberCard(
                borderColor = CyberCyan,
                backgroundColor = SurfaceElevatedDark
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Bolt,
                                contentDescription = null,
                                tint = GamerAmber,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (isHindi) "⚡ 1-टैप ऑटो हेडशॉट ट्यूनर" else "⚡ 1-Tap Auto Headshot Tuner",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = MatrixGreen.copy(alpha = 0.2f),
                            border = androidx.compose.foundation.BorderStroke(1.dp, MatrixGreen.copy(alpha = 0.5f))
                        ) {
                            Text(
                                text = "${specs.totalRamGb}GB RAM",
                                color = MatrixGreen,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = if (isHindi)
                            "आपके डिवाइस के रिज़ॉल्यूशन (${specs.screenWidthPx}x${specs.screenHeightPx}) और ${specs.refreshRateHz}Hz रिफ्रेश रेट के अनुसार बेस्ट हेडशॉट सेटिंग्स बनाएं।"
                        else
                            "Calculates the optimal drag sensitivity & fire button size specifically calibrated for your ${specs.model} (${specs.refreshRateHz}Hz).",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )

                    Spacer(modifier = Modifier.height(14.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        GlowButton(
                            text = if (isHindi) "वन-टैप ड्रैग जेनरेट करें" else "Generate One-Tap Sensi",
                            onClick = {
                                viewModel.generateAutoPreset(specs.totalRamGb, specs.refreshRateHz, "One-Tap")
                                viewModel.navigateTo(AppScreen.SENSITIVITY)
                            },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("btn_auto_generate"),
                            icon = Icons.Default.AutoAwesome
                        )
                    }
                }
            }
        }

        // Quick Navigation Tiles
        item {
            Text(
                text = if (isHindi) "🚀 क्विक टूल्स" else "🚀 Quick Gaming Tools",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                QuickActionTile(
                    title = if (isHindi) "सेंसिटिविटी" else "Sensi Controls",
                    sub = if (isHindi) "6 स्लाइडर ट्यूनिंग" else "6 Sliders & Scopes",
                    icon = Icons.Default.Tune,
                    color = CyberCyan,
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.navigateTo(AppScreen.SENSITIVITY) }
                )

                QuickActionTile(
                    title = if (isHindi) "DPI कैलकुलेटर" else "DPI Calculator",
                    sub = if (isHindi) "स्मॉलेस्ट विड्थ" else "Smallest Width DP",
                    icon = Icons.Default.Calculate,
                    color = NeonCrimson,
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.navigateTo(AppScreen.DPI_CALCULATOR) }
                )
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                QuickActionTile(
                    title = if (isHindi) "टच स्पीड टेस्ट" else "Touch Speed Test",
                    sub = if (isHindi) "ड्रैग रिस्पॉन्स लेटेंसी" else "Drag Latency & Velocity",
                    icon = Icons.Default.TouchApp,
                    color = GamerAmber,
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.navigateTo(AppScreen.DPI_CALCULATOR) }
                )

                QuickActionTile(
                    title = if (isHindi) "गेमर प्रोफ़ाइल" else "Gamer Profile",
                    sub = if (isHindi) "पिंग और डिवाइस डायग्नोस्टिक" else "Ping & Device Telemetry",
                    icon = Icons.Default.Speed,
                    color = MatrixGreen,
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.navigateTo(AppScreen.PROFILE) }
                )
            }
        }

        // Top Pro Presets Carousel
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (isHindi) "🔥 टॉप प्रो सेटिंग्स" else "🔥 Pro Esports Presets",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = if (isHindi) "सभी देखें" else "View All",
                    style = MaterialTheme.typography.labelMedium,
                    color = CyberCyan,
                    modifier = Modifier.clickable { viewModel.navigateTo(AppScreen.SENSITIVITY) }
                )
            }
        }

        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(presets.take(4)) { preset ->
                    PresetCarouselCard(
                        preset = preset,
                        onApply = {
                            viewModel.applyPreset(preset)
                            viewModel.navigateTo(AppScreen.SENSITIVITY)
                        },
                        onShare = {
                            ShareUtils.sharePreset(context, preset)
                        }
                    )
                }
            }
        }

        // Drag Headshot Pro Guide & Tricks (Bilingual)
        item {
            CyberCard(
                borderColor = BorderCrimson,
                backgroundColor = SurfaceElevatedDark
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.LocalFireDepartment,
                            contentDescription = null,
                            tint = NeonCrimson,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isHindi) "🎯 प्रो ड्रैग हेडशॉट सीक्रेट्स" else "🎯 Pro Drag Headshot Secrets",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = if (isHindi)
                            "1. 'J-Shape Drag' ट्रिक: क्लोज रेंज में फायर बटन को नीचे खींचकर तेजी से ऊपर ले जाएं।\n2. रेड डॉट 90%+ रखें ताकि एनिमी की बॉडी पर एम लॉक होने से पहले सीधे हेड पर चला जाए।\n3. फायर बटन साइज 40-45% रखें जिससे ड्रैग करने के लिए ज्यादा स्क्रीन स्पेस मिले।"
                        else
                            "1. Rotation Drag: For close range M1887/Shotgun fights, drag fire button in a 'J' curve.\n2. Straight Drag: For mid-range SMG spray, drag straight up towards the enemy head.\n3. Keep Fire Button size between 40% - 48% for maximum thumb swipe travel.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary,
                        lineHeight = 20.sp
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun HeroBannerCard(
    gamerTag: String,
    deviceModel: String,
    isHindi: Boolean,
    onLanguageToggle: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(
                Brush.horizontalGradient(
                    listOf(
                        Color(0xFF0D1B2A),
                        Color(0xFF1B263B),
                        Color(0xFF003566)
                    )
                )
            )
            .border(1.dp, BorderCyan, RoundedCornerShape(20.dp))
    ) {
        // Banner background image
        Image(
            painter = painterResource(id = R.drawable.gamer_hero_banner),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f)),
            alpha = 0.4f
        )

        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = CyberCyan.copy(alpha = 0.2f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CyberCyan.copy(alpha = 0.6f))
                ) {
                    Text(
                        text = "VIP SENSI ENGINE 2026",
                        style = MaterialTheme.typography.labelSmall,
                        color = CyberCyan,
                        fontWeight = FontWeight.Black,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = gamerTag,
                    style = MaterialTheme.typography.headlineSmall,
                    color = TextPrimary,
                    fontWeight = FontWeight.Black
                )

                Text(
                    text = "📱 $deviceModel",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )
            }

            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Language toggle chip
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = SurfaceDark.copy(alpha = 0.8f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, BorderCyan),
                    modifier = Modifier.clickable { onLanguageToggle() }
                ) {
                    Text(
                        text = if (isHindi) "🌐 हिंदी / ENG" else "🌐 ENG / हिंदी",
                        style = MaterialTheme.typography.labelMedium,
                        color = CyberCyan,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))
                // Gaming avatar icon
                Image(
                    painter = painterResource(id = R.drawable.game_sensi_icon),
                    contentDescription = "Avatar",
                    modifier = Modifier
                        .size(54.dp)
                        .clip(CircleShape)
                        .border(2.dp, CyberCyan, CircleShape)
                )
            }
        }
    }
}

@Composable
fun DeviceStatsHUD(
    specs: com.example.model.DeviceSpecs,
    isHindi: Boolean
) {
    CyberCard(
        borderColor = BorderCyan.copy(alpha = 0.3f),
        backgroundColor = SurfaceDark
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.PhoneAndroid,
                        contentDescription = null,
                        tint = CyberCyan,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isHindi) "डिवाइस स्पेक्स और डिस्प्ले" else "Live Device Diagnostics",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                }
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = MatrixGreen.copy(alpha = 0.15f)
                ) {
                    Text(
                        text = "${specs.refreshRateHz} Hz Display",
                        color = MatrixGreen,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                StatPill(
                    label = if (isHindi) "रिज़ॉल्यूशन" else "Resolution",
                    value = "${specs.screenWidthPx}x${specs.screenHeightPx}"
                )
                StatPill(
                    label = if (isHindi) "डेंसिटी" else "Density",
                    value = "${specs.densityDpi} DPI"
                )
                StatPill(
                    label = if (isHindi) "डिफ़ॉल्ट विड्थ" else "Base Width",
                    value = "${specs.smallestWidthDp} dp"
                )
                StatPill(
                    label = "Android",
                    value = "v${specs.androidVersion}"
                )
            }
        }
    }
}

@Composable
fun StatPill(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = TextMuted
        )
        Text(
            text = value,
            style = MaterialTheme.typography.labelMedium,
            color = TextPrimary,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun QuickActionTile(
    title: String,
    sub: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .height(105.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(SurfaceElevatedDark)
            .border(1.dp, color.copy(alpha = 0.35f), RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .padding(14.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(color.copy(alpha = 0.18f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = color,
                    modifier = Modifier.size(20.dp)
                )
            }

            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
                Text(
                    text = sub,
                    style = MaterialTheme.typography.labelSmall,
                    color = TextMuted,
                    fontSize = 11.sp
                )
            }
        }
    }
}

@Composable
fun PresetCarouselCard(
    preset: SensitivityPreset,
    onApply: () -> Unit,
    onShare: () -> Unit
) {
    Box(
        modifier = Modifier
            .width(260.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(SurfaceElevatedDark)
            .border(1.dp, BorderCyan, RoundedCornerShape(16.dp))
            .padding(14.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = CyberCyan.copy(alpha = 0.2f)
                ) {
                    Text(
                        text = preset.brand,
                        color = CyberCyan,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }

                IconButton(
                    onClick = onShare,
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Share",
                        tint = TextSecondary,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = preset.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                MiniSensiStat("General", "${preset.general}%")
                MiniSensiStat("Red Dot", "${preset.redDot}%")
                MiniSensiStat("2X Scope", "${preset.scope2x}%")
                MiniSensiStat("DPI", "${preset.recommendedDpi}")
            }

            Spacer(modifier = Modifier.height(12.dp))
            GlowButton(
                text = "Apply Sensi",
                onClick = onApply,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(38.dp)
            )
        }
    }
}

@Composable
fun MiniSensiStat(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = label, style = MaterialTheme.typography.labelSmall, color = TextMuted, fontSize = 9.sp)
        Text(text = value, style = MaterialTheme.typography.labelMedium, color = CyberCyan, fontWeight = FontWeight.Bold)
    }
}
