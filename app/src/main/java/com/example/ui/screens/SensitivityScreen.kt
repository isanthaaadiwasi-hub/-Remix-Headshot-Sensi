package com.example.ui.screens

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
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.CenterFocusStrong
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.FilterCenterFocus
import androidx.compose.material.icons.filled.Gamepad
import androidx.compose.material.icons.filled.LockReset
import androidx.compose.material.icons.filled.RadioButtonChecked
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.ZoomIn
import androidx.compose.material.icons.filled.ZoomOutMap
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.local.SensitivityPreset
import com.example.ui.MainViewModel
import com.example.ui.components.CyberCard
import com.example.ui.components.GlowButton
import com.example.ui.components.LiveCrosshairHud
import com.example.ui.components.SensiSlider
import com.example.ui.theme.BackgroundDark
import com.example.ui.theme.BorderCyan
import com.example.ui.theme.BorderCrimson
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.ElectricBlue
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
fun SensitivityScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val isHindi by viewModel.isHindi.collectAsStateWithLifecycle()
    val presets by viewModel.presets.collectAsStateWithLifecycle()
    val sensiState by viewModel.sensiState.collectAsStateWithLifecycle()

    var selectedBrandFilter by remember { mutableStateOf("All") }
    var showSaveDialog by remember { mutableStateOf(false) }
    var presetNameInput by remember { mutableStateOf("") }
    var presetNotesInput by remember { mutableStateOf("") }

    val brands = listOf("All", "Samsung", "Xiaomi", "Realme", "Apple", "Vivo", "Custom")

    val filteredPresets = if (selectedBrandFilter == "All") {
        presets
    } else if (selectedBrandFilter == "Custom") {
        presets.filter { !it.isPreset }
    } else {
        presets.filter { it.brand.contains(selectedBrandFilter, ignoreCase = true) }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundDark)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(8.dp))
            // Screen Title & Live HUD
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = if (isHindi) "🎯 प्रो सेंसिटिविटी सेटअप" else "🎯 Pro Sensitivity Lab",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Black,
                        color = TextPrimary
                    )
                    Text(
                        text = sensiState.name,
                        style = MaterialTheme.typography.titleSmall,
                        color = CyberCyan
                    )
                }

                // Interactive HUD Crosshair Box
                LiveCrosshairHud(
                    generalSensi = sensiState.general,
                    redDotSensi = sensiState.redDot
                )
            }
        }

        // Brand Presets Filter Bar
        item {
            Column {
                Text(
                    text = if (isHindi) "ब्रांड और प्रीसेट्स चुनें:" else "Select Device Presets:",
                    style = MaterialTheme.typography.labelMedium,
                    color = TextSecondary
                )
                Spacer(modifier = Modifier.height(8.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(brands) { brand ->
                        val isSelected = selectedBrandFilter == brand
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = if (isSelected) CyberCyan else SurfaceElevatedDark,
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (isSelected) CyberCyan else BorderCyan.copy(alpha = 0.3f)
                            ),
                            modifier = Modifier.clickable { selectedBrandFilter = brand }
                        ) {
                            Text(
                                text = brand,
                                color = if (isSelected) BackgroundDark else TextPrimary,
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                            )
                        }
                    }
                }
            }
        }

        // Presets Quick Selector Carousel
        if (filteredPresets.isNotEmpty()) {
            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(filteredPresets) { preset ->
                        PresetChipCard(
                            preset = preset,
                            isSelected = sensiState.name == preset.name,
                            onSelect = { viewModel.applyPreset(preset) },
                            onToggleFav = { viewModel.toggleFavorite(preset) }
                        )
                    }
                }
            }
        }

        // 6 Main Sensitivity Sliders
        item {
            CyberCard(
                borderColor = CyberCyan,
                backgroundColor = SurfaceDark
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = if (isHindi) "⚙️ स्कोप और ड्रैग सेंसिटिविटी" else "⚙️ Sensi Calibration Sliders",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // General
                    SensiSlider(
                        label = if (isHindi) "जनरल (General)" else "General Sensitivity",
                        subLabel = if (isHindi) "कैमरा 360 और स्क्रीन स्वाइप स्पीड" else "Camera rotation & 360 drag speed",
                        value = sensiState.general,
                        onValueChange = { viewModel.updateSensiValues(general = it) },
                        icon = Icons.Default.ZoomOutMap,
                        accentColor = CyberCyan
                    )

                    // Red Dot
                    SensiSlider(
                        label = if (isHindi) "रेड डॉट (Red Dot)" else "Red Dot Sight",
                        subLabel = if (isHindi) "क्लोज रेंज वन-टैप हेडशॉट एक्यूरेसी" else "Close combat one-tap lock accuracy",
                        value = sensiState.redDot,
                        onValueChange = { viewModel.updateSensiValues(redDot = it) },
                        icon = Icons.Default.RadioButtonChecked,
                        accentColor = NeonCrimson
                    )

                    // 2X Scope
                    SensiSlider(
                        label = if (isHindi) "2X स्कोप (2X Scope)" else "2X Scope",
                        subLabel = if (isHindi) "मिड-रेंज स्प्रे कंट्रोल" else "Mid-range spray & tracking",
                        value = sensiState.scope2x,
                        onValueChange = { viewModel.updateSensiValues(scope2x = it) },
                        icon = Icons.Default.ZoomIn,
                        accentColor = GamerAmber
                    )

                    // 4X Scope
                    SensiSlider(
                        label = if (isHindi) "4X स्कोप (4X Scope)" else "4X Scope",
                        subLabel = if (isHindi) "लॉन्ग रेंज राइफल स्टेबिलिटी" else "Long range AR head tracking",
                        value = sensiState.scope4x,
                        onValueChange = { viewModel.updateSensiValues(scope4x = it) },
                        icon = Icons.Default.CenterFocusStrong,
                        accentColor = MatrixGreen
                    )

                    // Sniper Scope
                    SensiSlider(
                        label = if (isHindi) "स्नाइपर स्कोप (Sniper Scope)" else "Sniper Scope (AWM/M82B)",
                        subLabel = if (isHindi) "डबल स्नाइपर क्विक-स्विच फ्लिक" else "Fast-switch sniper flick shots",
                        value = sensiState.sniperScope,
                        onValueChange = { viewModel.updateSensiValues(sniperScope = it) },
                        icon = Icons.Default.FilterCenterFocus,
                        accentColor = CyberCyan
                    )

                    // Free Look
                    SensiSlider(
                        label = if (isHindi) "फ्री लुक (Free Look 360)" else "Free Look (Eye Button)",
                        subLabel = if (isHindi) "रनिंग के दौरान चारों तरफ देखना" else "Surrounding awareness while sprinting",
                        value = sensiState.freeLook,
                        onValueChange = { viewModel.updateSensiValues(freeLook = it) },
                        icon = Icons.Default.Visibility,
                        accentColor = ElectricBlue
                    )

                    // Fire Button Size
                    SensiSlider(
                        label = if (isHindi) "फायर बटन साइज (Fire Button)" else "Fire Button Size",
                        subLabel = if (isHindi) "परफेक्ट ड्रैग हेडशॉट बटन साइज" else "HUD fire button touch area %",
                        value = sensiState.fireButtonSize,
                        onValueChange = { viewModel.updateSensiValues(fireButtonSize = it) },
                        icon = Icons.Default.Gamepad,
                        accentColor = GamerAmber,
                        maxValue = 100
                    )
                }
            }
        }

        // Action Buttons (Save, Copy, Share, Reset)
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    GlowButton(
                        text = if (isHindi) "प्रीसेट सेव करें" else "Save Preset",
                        onClick = {
                            presetNameInput = sensiState.name
                            presetNotesInput = sensiState.notes
                            showSaveDialog = true
                        },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("btn_save_preset"),
                        icon = Icons.Default.Save
                    )

                    Button(
                        onClick = {
                            val tempPreset = SensitivityPreset(
                                name = sensiState.name,
                                brand = sensiState.brand,
                                gameMode = sensiState.gameMode,
                                general = sensiState.general,
                                redDot = sensiState.redDot,
                                scope2x = sensiState.scope2x,
                                scope4x = sensiState.scope4x,
                                sniperScope = sensiState.sniperScope,
                                freeLook = sensiState.freeLook,
                                fireButtonSize = sensiState.fireButtonSize,
                                recommendedDpi = sensiState.recommendedDpi,
                                notes = sensiState.notes
                            )
                            ShareUtils.sharePreset(context, tempPreset)
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp)
                            .testTag("btn_share_sensi"),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = SurfaceElevatedDark),
                        border = androidx.compose.foundation.BorderStroke(1.dp, CyberCyan)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Share, contentDescription = null, tint = CyberCyan, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (isHindi) "शेयर करें" else "Share Config",
                                color = CyberCyan,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = {
                            val code = "GEN:${sensiState.general}|RED:${sensiState.redDot}|2X:${sensiState.scope2x}|4X:${sensiState.scope4x}|SNIP:${sensiState.sniperScope}|FREE:${sensiState.freeLook}|DPI:${sensiState.recommendedDpi}"
                            ShareUtils.copyToClipboard(context, "Sensi Code", code)
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp)
                            .testTag("btn_copy_code"),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = SurfaceDark),
                        border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.ContentCopy, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (isHindi) "कोड कॉपी करें" else "Copy Code",
                                color = TextPrimary,
                                style = MaterialTheme.typography.labelMedium
                            )
                        }
                    }

                    Button(
                        onClick = {
                            viewModel.updateSensiValues(
                                general = 95,
                                redDot = 90,
                                scope2x = 85,
                                scope4x = 80,
                                sniperScope = 60,
                                freeLook = 75,
                                fireButtonSize = 45,
                                recommendedDpi = 411
                            )
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = SurfaceDark),
                        border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.LockReset, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (isHindi) "रीसेट" else "Reset",
                                color = TextSecondary,
                                style = MaterialTheme.typography.labelMedium
                            )
                        }
                    }
                }
            }
        }

        // Saved Custom Presets List
        val customPresets = presets.filter { !it.isPreset }
        if (customPresets.isNotEmpty()) {
            item {
                Text(
                    text = if (isHindi) "📁 आपके सेव किए गए कस्टम प्रीसेट्स (Local Storage)" else "📁 Your Saved Custom Presets (Local Storage)",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            }

            items(customPresets) { customPreset ->
                CyberCard(
                    borderColor = BorderCyan.copy(alpha = 0.3f),
                    backgroundColor = SurfaceElevatedDark
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = customPreset.name,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            if (customPreset.notes.isNotEmpty()) {
                                Text(
                                    text = customPreset.notes,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = TextMuted
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Gen: ${customPreset.general}% | Red Dot: ${customPreset.redDot}% | DPI: ${customPreset.recommendedDpi}",
                                style = MaterialTheme.typography.labelSmall,
                                color = CyberCyan
                            )
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(
                                onClick = { viewModel.applyPreset(customPreset) }
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = CyberCyan.copy(alpha = 0.2f),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, CyberCyan)
                                ) {
                                    Text(
                                        text = "Apply",
                                        color = CyberCyan,
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }

                            IconButton(
                                onClick = { viewModel.deletePreset(customPreset) }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Delete",
                                    tint = NeonCrimson.copy(alpha = 0.8f),
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    // Save Preset Dialog
    if (showSaveDialog) {
        AlertDialog(
            onDismissRequest = { showSaveDialog = false },
            title = {
                Text(
                    text = if (isHindi) "कस्टम प्रीसेट सेव करें" else "Save Custom Preset",
                    style = MaterialTheme.typography.titleLarge,
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = presetNameInput,
                        onValueChange = { presetNameInput = it },
                        label = { Text("Preset Name") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = CyberCyan,
                            unfocusedBorderColor = BorderSubtle,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = presetNotesInput,
                        onValueChange = { presetNotesInput = it },
                        label = { Text("Notes / Weapon Details") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = CyberCyan,
                            unfocusedBorderColor = BorderSubtle,
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
                        viewModel.saveCurrentPreset(presetNameInput, presetNotesInput)
                        showSaveDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = CyberCyan)
                ) {
                    Text("Save", color = BackgroundDark, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showSaveDialog = false }) {
                    Text("Cancel", color = TextSecondary)
                }
            },
            containerColor = SurfaceDark,
            shape = RoundedCornerShape(16.dp)
        )
    }
}

val BorderSubtle = Color(0xFF334155)

@Composable
fun PresetChipCard(
    preset: SensitivityPreset,
    isSelected: Boolean,
    onSelect: () -> Unit,
    onToggleFav: () -> Unit
) {
    Box(
        modifier = Modifier
            .width(180.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(if (isSelected) SurfaceCard else SurfaceElevatedDark)
            .border(
                1.dp,
                if (isSelected) CyberCyan else BorderCyan.copy(alpha = 0.25f),
                RoundedCornerShape(12.dp)
            )
            .clickable { onSelect() }
            .padding(10.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = preset.brand,
                    style = MaterialTheme.typography.labelSmall,
                    color = CyberCyan,
                    fontWeight = FontWeight.Bold
                )

                Icon(
                    imageVector = if (preset.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = null,
                    tint = if (preset.isFavorite) NeonCrimson else TextMuted,
                    modifier = Modifier
                        .size(16.dp)
                        .clickable { onToggleFav() }
                )
            }

            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = preset.name,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Gen: ${preset.general}% | DPI: ${preset.recommendedDpi}",
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondary,
                fontSize = 10.sp
            )
        }
    }
}
