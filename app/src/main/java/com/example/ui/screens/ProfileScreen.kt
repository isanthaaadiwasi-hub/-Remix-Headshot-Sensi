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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Gamepad
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.NetworkCheck
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.R
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
import com.example.ui.theme.GoldGradientEnd
import com.example.ui.theme.GoldGradientStart
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
fun ProfileScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val isHindi by viewModel.isHindi.collectAsStateWithLifecycle()
    val profile by viewModel.userProfile.collectAsStateWithLifecycle()
    val pingState by viewModel.pingState.collectAsStateWithLifecycle()
    val presets by viewModel.presets.collectAsStateWithLifecycle()
    val specs = viewModel.deviceSpecs

    var showEditProfileDialog by remember { mutableStateOf(false) }
    var gamerTagInput by remember { mutableStateOf(profile?.gamerTag ?: "SHADOW_WARRIOR") }
    var rankInput by remember { mutableStateOf(profile?.rank ?: "Grandmaster") }
    var weaponInput by remember { mutableStateOf(profile?.favoriteWeapon ?: "M1887 & Desert Eagle") }
    var playstyleInput by remember { mutableStateOf(profile?.playstyle ?: "One-Tap Rusher") }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundDark)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(8.dp))
            // Profile Title
            Text(
                text = if (isHindi) "👤 गेमर प्रोफ़ाइल और डायग्नोस्टिक्स" else "👤 Gamer Profile & Telemetry",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Black,
                color = TextPrimary
            )
        }

        // Gamer Card with Avatar Image
        item {
            CyberCard(
                borderColor = CyberCyan,
                backgroundColor = SurfaceElevatedDark
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Gamer Avatar Image
                        Image(
                            painter = painterResource(id = R.drawable.game_sensi_icon),
                            contentDescription = "Avatar",
                            modifier = Modifier
                                .size(76.dp)
                                .clip(CircleShape)
                                .border(2.dp, CyberCyan, CircleShape)
                        )

                        Spacer(modifier = Modifier.width(14.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = profile?.gamerTag ?: "SHADOW_KILLER",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Black,
                                    color = TextPrimary
                                )
                                IconButton(
                                    onClick = {
                                        gamerTagInput = profile?.gamerTag ?: ""
                                        rankInput = profile?.rank ?: ""
                                        weaponInput = profile?.favoriteWeapon ?: ""
                                        playstyleInput = profile?.playstyle ?: ""
                                        showEditProfileDialog = true
                                    },
                                    modifier = Modifier.size(24.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Edit,
                                        contentDescription = "Edit",
                                        tint = CyberCyan,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(4.dp))

                            // Rank Badge
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = GamerAmber.copy(alpha = 0.2f),
                                border = androidx.compose.foundation.BorderStroke(1.dp, GamerAmber)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = null,
                                        tint = GamerAmber,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = profile?.rank ?: "Grandmaster Tier",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = GamerAmber,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Fav: ${profile?.favoriteWeapon ?: "M1887 & Deagle"}",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Stats summary row
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(SurfaceDark)
                            .padding(10.dp),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        ProfileStatItem(
                            label = if (isHindi) "प्रीसेट्स" else "Presets",
                            value = "${presets.size}"
                        )
                        ProfileStatItem(
                            label = if (isHindi) "प्लेस्टाइल" else "Playstyle",
                            value = profile?.playstyle ?: "One-Tap"
                        )
                        ProfileStatItem(
                            label = if (isHindi) "डिस्प्ले" else "Screen",
                            value = "${specs.refreshRateHz}Hz"
                        )
                    }
                }
            }
        }

        // Live Real-Time Network Ping Tester
        item {
            CyberCard(
                borderColor = if (pingState.pingMs in 1..60) MatrixGreen else CyberCyan,
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
                                imageVector = Icons.Default.Wifi,
                                contentDescription = null,
                                tint = CyberCyan,
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (isHindi) "📶 लाइव गेमिंग पिंग टेस्टर" else "📶 Live Gaming Ping Benchmark",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                        }

                        if (pingState.isTesting) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = CyberCyan,
                                strokeWidth = 2.dp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = if (pingState.pingMs >= 0) "${pingState.pingMs} ms" else "Not Tested",
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Black,
                                color = when {
                                    pingState.pingMs < 0 -> TextMuted
                                    pingState.pingMs < 50 -> MatrixGreen
                                    pingState.pingMs < 90 -> GamerAmber
                                    else -> NeonCrimson
                                }
                            )
                            Text(
                                text = pingState.pingStatus,
                                style = MaterialTheme.typography.labelSmall,
                                color = TextSecondary
                            )
                        }

                        Button(
                            onClick = { viewModel.runNetworkPingTest() },
                            colors = ButtonDefaults.buttonColors(containerColor = CyberCyan),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.testTag("btn_ping_test")
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.NetworkCheck, contentDescription = null, tint = BackgroundDark, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = if (isHindi) "टेस्ट पिंग" else "Test Ping",
                                    color = BackgroundDark,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }

        // Full Device Diagnostics HUD
        item {
            CyberCard(
                borderColor = BorderCyan.copy(alpha = 0.3f),
                backgroundColor = SurfaceElevatedDark
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.PhoneAndroid,
                            contentDescription = null,
                            tint = CyberCyan,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isHindi) "📱 डिवाइस हार्डवेयर स्पेक्स" else "📱 Full Device Telemetry",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    DiagnosticsRow("Device Model", "${specs.manufacturer} ${specs.model}")
                    DiagnosticsRow("Screen Resolution", "${specs.screenWidthPx} x ${specs.screenHeightPx} px")
                    DiagnosticsRow("Screen Density (DPI)", "${specs.densityDpi} DPI (Scale: ${specs.densityScale}x)")
                    DiagnosticsRow("Smallest Width", "${specs.smallestWidthDp} dp")
                    DiagnosticsRow("Display Refresh Rate", "${specs.refreshRateHz} Hz High-Smoothness")
                    DiagnosticsRow("Approx System RAM", "${specs.totalRamGb} GB RAM")
                    DiagnosticsRow("Android OS Version", "Android ${specs.androidVersion} (API ${specs.sdkInt})")
                }
            }
        }

        // Share & Export All Configs Card
        item {
            CyberCard(
                borderColor = NeonCrimson.copy(alpha = 0.4f),
                backgroundColor = SurfaceDark
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = null,
                            tint = NeonCrimson,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isHindi) "🚀 स्क्वाड के साथ सभी सेटिंग्स शेयर करें" else "🚀 Share Full Config with Squad",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = if (isHindi) "अपनी सभी हेडशॉट सेंसिटिविटी, DPI और डिवाइस सेटिंग्स को एक क्लिक में शेयर करें।" else "Export complete VIP sensitivity codes and DPI setup directly to your gaming squad.",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    GlowButton(
                        text = if (isHindi) "स्क्वाड को शेयर करें" else "Share Config Package",
                        onClick = {
                            val activeSensi = viewModel.sensiState.value
                            val exportText = """
                                🎮 ━━━━━━━━━━━━━━━━━━━━━━━━━ 🎮
                                🔥 VIP HEADSHOT SENSI BACKUP 🔥
                                👤 Gamer Tag: ${profile?.gamerTag ?: "Pro Gamer"}
                                📱 Device: ${specs.manufacturer} ${specs.model}
                                ━━━━━━━━━━━━━━━━━━━━━━━━━
                                ⚡ SENSI SLIDERS:
                                • General: ${activeSensi.general}%
                                • Red Dot: ${activeSensi.redDot}%
                                • 2X Scope: ${activeSensi.scope2x}%
                                • 4X Scope: ${activeSensi.scope4x}%
                                • Sniper: ${activeSensi.sniperScope}%
                                • Free Look: ${activeSensi.freeLook}%
                                • Fire Button Size: ${activeSensi.fireButtonSize}%
                                ━━━━━━━━━━━━━━━━━━━━━━━━━
                                📐 DPI (Smallest Width): ${activeSensi.recommendedDpi} dp
                                🚀 Calibrated for ${specs.refreshRateHz}Hz & ${specs.totalRamGb}GB RAM
                                ━━━━━━━━━━━━━━━━━━━━━━━━━
                                Generated by Headshot Sensi & DPI Tool 🚀
                            """.trimIndent()

                            ShareUtils.copyToClipboard(context, "Full Config", exportText)
                            val sendIntent = android.content.Intent().apply {
                                action = android.content.Intent.ACTION_SEND
                                putExtra(android.content.Intent.EXTRA_TEXT, exportText)
                                type = "text/plain"
                            }
                            context.startActivity(android.content.Intent.createChooser(sendIntent, "Share with Squad"))
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("btn_export_all"),
                        icon = Icons.Default.Share
                    )
                }
            }
        }

        // App Settings & Preferences (Language, Haptic)
        item {
            CyberCard(
                borderColor = BorderCyan.copy(alpha = 0.25f),
                backgroundColor = SurfaceElevatedDark
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = if (isHindi) "⚙️ सेटिंग्स और भाषा" else "⚙️ Preferences & App Info",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Language, contentDescription = null, tint = CyberCyan, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(text = if (isHindi) "हिंदी भाषा (Hindi Tips)" else "Hindi Language Mode", color = TextPrimary)
                        }
                        Switch(
                            checked = isHindi,
                            onCheckedChange = { viewModel.toggleLanguage() },
                            colors = SwitchDefaults.colors(checkedThumbColor = CyberCyan, checkedTrackColor = SurfaceCard)
                        )
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    // Edit Profile Dialog
    if (showEditProfileDialog) {
        AlertDialog(
            onDismissRequest = { showEditProfileDialog = false },
            title = {
                Text(
                    text = if (isHindi) "गेमर प्रोफ़ाइल एडिट करें" else "Edit Gamer Profile",
                    style = MaterialTheme.typography.titleLarge,
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = gamerTagInput,
                        onValueChange = { gamerTagInput = it },
                        label = { Text("Gamer Tag") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = CyberCyan,
                            unfocusedBorderColor = BorderCyan.copy(alpha = 0.3f),
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = rankInput,
                        onValueChange = { rankInput = it },
                        label = { Text("Rank / Tier (e.g. Grandmaster, Heroic)") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = CyberCyan,
                            unfocusedBorderColor = BorderCyan.copy(alpha = 0.3f),
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = weaponInput,
                        onValueChange = { weaponInput = it },
                        label = { Text("Favorite Weapons") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = CyberCyan,
                            unfocusedBorderColor = BorderCyan.copy(alpha = 0.3f),
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = playstyleInput,
                        onValueChange = { playstyleInput = it },
                        label = { Text("Playstyle (e.g. One-Tap Rusher, Sniper)") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = CyberCyan,
                            unfocusedBorderColor = BorderCyan.copy(alpha = 0.3f),
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.updateUserProfile(gamerTagInput, rankInput, weaponInput, playstyleInput)
                        showEditProfileDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = CyberCyan)
                ) {
                    Text("Save", color = BackgroundDark, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditProfileDialog = false }) {
                    Text("Cancel", color = TextSecondary)
                }
            },
            containerColor = SurfaceDark,
            shape = RoundedCornerShape(16.dp)
        )
    }
}

@Composable
fun ProfileStatItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = label, style = MaterialTheme.typography.labelSmall, color = TextMuted)
        Text(text = value, style = MaterialTheme.typography.titleMedium, color = CyberCyan, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun DiagnosticsRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
        Text(text = value, style = MaterialTheme.typography.bodySmall, color = TextPrimary, fontWeight = FontWeight.SemiBold)
    }
}
