package com.example.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.MainViewModel
import com.example.ui.components.CyberCard
import com.example.ui.components.GlowButton
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
import kotlin.math.sqrt

@Composable
fun DpiCalculatorScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val isHindi by viewModel.isHindi.collectAsStateWithLifecycle()
    val dpiState by viewModel.dpiState.collectAsStateWithLifecycle()
    val touchTest by viewModel.touchTest.collectAsStateWithLifecycle()
    val dpiHistory by viewModel.dpiHistory.collectAsStateWithLifecycle()
    val specs = viewModel.deviceSpecs

    var customDpiSlider by remember { mutableStateOf(dpiState.targetDpi.toFloat()) }
    val dragPoints = remember { mutableStateListOf<Offset>() }
    var dragStartTime by remember { mutableStateOf(0L) }
    var totalDragDistance by remember { mutableStateOf(0f) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundDark)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(8.dp))
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = if (isHindi) "📐 DPI और विड्थ कैलकुलेटर" else "📐 DPI & Smallest Width Tool",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Black,
                        color = TextPrimary
                    )
                    Text(
                        text = if (isHindi) "स्मूथ मूवमेंट और फास्ट ड्रैग के लिए" else "Optimize touch resolution & swipe distance",
                        style = MaterialTheme.typography.titleSmall,
                        color = CyberCyan
                    )
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = CyberCyan.copy(alpha = 0.2f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CyberCyan)
                ) {
                    Text(
                        text = "Base: ${specs.smallestWidthDp} dp",
                        color = CyberCyan,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
        }

        // DPI Calculator Card
        item {
            CyberCard(
                borderColor = if (customDpiSlider > 560f) NeonCrimson else CyberCyan,
                backgroundColor = SurfaceDark
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (isHindi) "टारगेट DPI (Smallest Width)" else "Target DPI (Smallest Width)",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = (if (customDpiSlider > 560f) NeonCrimson else CyberCyan).copy(alpha = 0.2f),
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (customDpiSlider > 560f) NeonCrimson else CyberCyan
                            )
                        ) {
                            Text(
                                text = "${customDpiSlider.toInt()} dp",
                                color = if (customDpiSlider > 560f) NeonCrimson else CyberCyan,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Black,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Slider(
                        value = customDpiSlider,
                        onValueChange = {
                            customDpiSlider = it
                            viewModel.updateDpiCalculation(
                                specs.smallestWidthDp,
                                it.toInt(),
                                specs.refreshRateHz,
                                "${specs.manufacturer} ${specs.model}"
                            )
                        },
                        valueRange = 320f..650f,
                        colors = SliderDefaults.colors(
                            thumbColor = if (customDpiSlider > 560f) NeonCrimson else CyberCyan,
                            activeTrackColor = if (customDpiSlider > 560f) NeonCrimson else CyberCyan,
                            inactiveTrackColor = SurfaceCard
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("slider_dpi")
                    )

                    // Quick DPI Preset Buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        DpiPresetButton(
                            label = "Default (360)",
                            dpi = 360,
                            isSelected = customDpiSlider.toInt() == 360,
                            onClick = {
                                customDpiSlider = 360f
                                viewModel.updateDpiCalculation(specs.smallestWidthDp, 360, specs.refreshRateHz, "")
                            },
                            modifier = Modifier.weight(1f)
                        )
                        DpiPresetButton(
                            label = "Balanced (411)",
                            dpi = 411,
                            isSelected = customDpiSlider.toInt() == 411,
                            onClick = {
                                customDpiSlider = 411f
                                viewModel.updateDpiCalculation(specs.smallestWidthDp, 411, specs.refreshRateHz, "")
                            },
                            modifier = Modifier.weight(1f)
                        )
                        DpiPresetButton(
                            label = "Fast Sensi (460)",
                            dpi = 460,
                            isSelected = customDpiSlider.toInt() == 460,
                            onClick = {
                                customDpiSlider = 460f
                                viewModel.updateDpiCalculation(specs.smallestWidthDp, 460, specs.refreshRateHz, "")
                            },
                            modifier = Modifier.weight(1f)
                        )
                        DpiPresetButton(
                            label = "Extreme (520)",
                            dpi = 520,
                            isSelected = customDpiSlider.toInt() == 520,
                            onClick = {
                                customDpiSlider = 520f
                                viewModel.updateDpiCalculation(specs.smallestWidthDp, 520, specs.refreshRateHz, "")
                            },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Multiplier & Swipe Distance Info Box
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(SurfaceElevatedDark)
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = if (isHindi) "स्पीड बूस्ट" else "Speed Multiplier",
                                style = MaterialTheme.typography.labelSmall,
                                color = TextMuted
                            )
                            Text(
                                text = "${String.format("%.2f", dpiState.touchSensitivityMultiplier)}x",
                                style = MaterialTheme.typography.titleMedium,
                                color = MatrixGreen,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = if (isHindi) "360° स्वाइप दूरी" else "360° Swipe Travel",
                                style = MaterialTheme.typography.labelSmall,
                                color = TextMuted
                            )
                            Text(
                                text = "${String.format("%.1f", dpiState.estimatedSwipeDistanceCm)} cm",
                                style = MaterialTheme.typography.titleMedium,
                                color = CyberCyan,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = if (isHindi) "सुरक्षा स्थिति" else "Safety Status",
                                style = MaterialTheme.typography.labelSmall,
                                color = TextMuted
                            )
                            Text(
                                text = if (customDpiSlider > 560f) "⚠️ High DPI" else "✅ Safe Range",
                                style = MaterialTheme.typography.titleMedium,
                                color = if (customDpiSlider > 560f) NeonCrimson else MatrixGreen,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    if (customDpiSlider > 560f) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Warning, contentDescription = null, tint = NeonCrimson, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (isHindi) "चेतावनी: 560+ DPI पर कुछ फोन में कीबोर्ड या रीस्टार्ट इशू आ सकता है।" else "Caution: DPI over 560 may cause keyboard scaling issues on some phones.",
                                style = MaterialTheme.typography.labelSmall,
                                color = NeonCrimson
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    // Save & Share DPI config buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        GlowButton(
                            text = if (isHindi) "DPI सेव करें" else "Save DPI to History",
                            onClick = { viewModel.saveDpiToHistory() },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("btn_save_dpi"),
                            icon = Icons.Default.Save
                        )

                        Button(
                            onClick = {
                                ShareUtils.shareCustomDpi(
                                    context,
                                    "${specs.manufacturer} ${specs.model}",
                                    customDpiSlider.toInt(),
                                    dpiState.touchSensitivityMultiplier
                                )
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp)
                                .testTag("btn_share_dpi"),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = SurfaceElevatedDark),
                            border = androidx.compose.foundation.BorderStroke(1.dp, CyberCyan)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Share, contentDescription = null, tint = CyberCyan, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (isHindi) "शेयर करें" else "Share DPI",
                                    color = CyberCyan,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }

        // Interactive Touch Speed & Drag Reaction Pad
        item {
            CyberCard(
                borderColor = GamerAmber,
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
                                imageVector = Icons.Default.TouchApp,
                                contentDescription = null,
                                tint = GamerAmber,
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (isHindi) "⚡ टच रिस्पॉन्स और ड्रैग स्पीड टेस्ट" else "⚡ Touch Speed & Drag Test Pad",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = GamerAmber.copy(alpha = 0.2f)
                        ) {
                            Text(
                                text = "${touchTest.totalTouches} Swipes",
                                color = GamerAmber,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = if (isHindi) "नीचे दिए गए पैड पर ऊपर की ओर तेजी से ड्रैग/स्वाइप करें:" else "Swipe quickly upwards inside the pad to test your drag velocity:",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Interactive Drawing & Touch Capture Canvas
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(170.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(BackgroundDark)
                            .border(1.dp, GamerAmber.copy(alpha = 0.4f), RoundedCornerShape(14.dp))
                            .pointerInput(Unit) {
                                detectDragGestures(
                                    onDragStart = { offset ->
                                        dragPoints.clear()
                                        dragPoints.add(offset)
                                        dragStartTime = System.currentTimeMillis()
                                        totalDragDistance = 0f
                                    },
                                    onDrag = { change, dragAmount ->
                                        change.consume()
                                        val newPoint = change.position
                                        dragPoints.add(newPoint)
                                        val dist = sqrt(dragAmount.x * dragAmount.x + dragAmount.y * dragAmount.y)
                                        totalDragDistance += dist
                                    },
                                    onDragEnd = {
                                        val duration = System.currentTimeMillis() - dragStartTime
                                        viewModel.recordTouchGesture(totalDragDistance, duration)
                                    }
                                )
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            // Draw grid lines
                            val gridSpacing = 30.dp.toPx()
                            for (x in 0..(size.width / gridSpacing).toInt()) {
                                drawLine(
                                    color = Color.DarkGray.copy(alpha = 0.3f),
                                    start = Offset(x * gridSpacing, 0f),
                                    end = Offset(x * gridSpacing, size.height),
                                    strokeWidth = 1f
                                )
                            }
                            for (y in 0..(size.height / gridSpacing).toInt()) {
                                drawLine(
                                    color = Color.DarkGray.copy(alpha = 0.3f),
                                    start = Offset(0f, y * gridSpacing),
                                    end = Offset(size.width, y * gridSpacing),
                                    strokeWidth = 1f
                                )
                            }

                            // Draw user drag path
                            if (dragPoints.size > 1) {
                                for (i in 0 until dragPoints.size - 1) {
                                    drawLine(
                                        color = CyberCyan,
                                        start = dragPoints[i],
                                        end = dragPoints[i + 1],
                                        strokeWidth = 6.dp.toPx(),
                                        cap = StrokeCap.Round
                                    )
                                }
                            }
                        }

                        if (dragPoints.isEmpty()) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    imageVector = Icons.Default.TouchApp,
                                    contentDescription = null,
                                    tint = CyberCyan.copy(alpha = 0.7f),
                                    modifier = Modifier.size(32.dp)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = if (isHindi) "👆 यहां ड्रैग स्वाइप करें" else "👆 Drag / Swipe Up Here",
                                    color = TextSecondary,
                                    style = MaterialTheme.typography.labelMedium
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Results
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(text = if (isHindi) "स्वाइप स्पीड" else "Swipe Velocity", style = MaterialTheme.typography.labelSmall, color = TextMuted)
                            Text(
                                text = "${touchTest.swipeSpeedPxPerSec.toInt()} px/s",
                                style = MaterialTheme.typography.titleMedium,
                                color = CyberCyan,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Column {
                            Text(text = if (isHindi) "ड्रैग ड्यूरेशन" else "Drag Duration", style = MaterialTheme.typography.labelSmall, color = TextMuted)
                            Text(
                                text = "${touchTest.touchLatencyMs} ms",
                                style = MaterialTheme.typography.titleMedium,
                                color = GamerAmber,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(text = if (isHindi) "एक्यूरेसी रेटिंग" else "Drag Tier", style = MaterialTheme.typography.labelSmall, color = TextMuted)
                            Text(
                                text = touchTest.accuracyRating,
                                style = MaterialTheme.typography.titleMedium,
                                color = MatrixGreen,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        // Developer Options Tutorial Guide (Bilingual)
        item {
            CyberCard(
                borderColor = BorderCyan.copy(alpha = 0.4f),
                backgroundColor = SurfaceDark
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Info, contentDescription = null, tint = CyberCyan, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isHindi) "📖 फोन में DPI (Smallest Width) कैसे बदलें?" else "📖 How to change DPI on Android?",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = if (isHindi)
                            "1. Settings > About Phone में जाएं और 'Build Number' पर 7 बार टैप करें।\n2. Developer Options ऑन हो जाएगा।\n3. Settings > System / Additional Settings > Developer Options खोलें।\n4. नीचे स्क्रॉल करके 'Smallest Width' या 'Minimum Width' खोजें।\n5. अपना पसंदीदा DPI वैल्यू (उदा. ${customDpiSlider.toInt()}) दर्ज करें और OK दबाएं।"
                        else
                            "1. Go to Settings > About Phone and tap 'Build Number' 7 times to enable Developer Options.\n2. Open Settings > Additional Settings > Developer Options.\n3. Scroll down to find 'Smallest Width' (or Minimum Width).\n4. Change the value to your desired DPI (e.g. ${customDpiSlider.toInt()}) and save.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary,
                        lineHeight = 20.sp
                    )
                }
            }
        }

        // DPI History from Room DB
        if (dpiHistory.isNotEmpty()) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isHindi) "🕒 DPI हिस्ट्री (Room DB)" else "🕒 Saved DPI History (Room DB)",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    IconButton(onClick = { viewModel.clearDpiHistory() }) {
                        Icon(Icons.Default.Delete, contentDescription = "Clear", tint = TextMuted, modifier = Modifier.size(18.dp))
                    }
                }
            }

            items(dpiHistory) { historyItem ->
                CyberCard(
                    borderColor = BorderCyan.copy(alpha = 0.2f),
                    backgroundColor = SurfaceElevatedDark
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "DPI: ${historyItem.customDpi} dp (${String.format("%.2fx", historyItem.sensitivityMultiplier)})",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Text(
                                text = "Base: ${historyItem.defaultWidthDp} dp | ${historyItem.refreshRateHz} Hz",
                                style = MaterialTheme.typography.labelSmall,
                                color = TextMuted
                            )
                        }

                        Button(
                            onClick = {
                                customDpiSlider = historyItem.customDpi.toFloat()
                                viewModel.updateDpiCalculation(
                                    historyItem.defaultWidthDp,
                                    historyItem.customDpi,
                                    historyItem.refreshRateHz,
                                    historyItem.deviceName
                                )
                            },
                            shape = RoundedCornerShape(6.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = CyberCyan.copy(alpha = 0.2f)),
                            border = androidx.compose.foundation.BorderStroke(1.dp, CyberCyan)
                        ) {
                            Text("Use", color = CyberCyan, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun DpiPresetButton(
    label: String,
    dpi: Int,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = if (isSelected) CyberCyan else SurfaceElevatedDark,
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (isSelected) CyberCyan else BorderCyan.copy(alpha = 0.3f)
        ),
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "$dpi",
                color = if (isSelected) BackgroundDark else TextPrimary,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Black
            )
            Text(
                text = label.substringBefore(" ("),
                color = if (isSelected) BackgroundDark else TextMuted,
                style = MaterialTheme.typography.labelSmall,
                fontSize = 9.sp,
                maxLines = 1
            )
        }
    }
}
