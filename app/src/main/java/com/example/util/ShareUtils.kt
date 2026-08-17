package com.example.util

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.example.data.local.SensitivityPreset

object ShareUtils {
    fun formatPresetShareText(preset: SensitivityPreset): String {
        return """
            🎮 ━━━ HEADSHOT SENSI CONFIG ━━━ 🎮
            🔥 Preset Name: ${preset.name}
            📱 Device/Brand: ${preset.brand}
            🎯 Game Style: ${preset.gameMode}
            ━━━━━━━━━━━━━━━━━━━━━━━━
            ⚙️ SENSITIVITY SETTINGS:
            • General: ${preset.general}%
            • Red Dot: ${preset.redDot}%
            • 2X Scope: ${preset.scope2x}%
            • 4X Scope: ${preset.scope4x}%
            • Sniper Scope: ${preset.sniperScope}%
            • Free Look: ${preset.freeLook}%
            ━━━━━━━━━━━━━━━━━━━━━━━━
            📐 DPI & BUTTON:
            • Recommended DPI (Smallest Width): ${preset.recommendedDpi} dp
            • Fire Button Size: ${preset.fireButtonSize}%
            ${if (preset.notes.isNotEmpty()) "💡 Tip: ${preset.notes}" else ""}
            ━━━━━━━━━━━━━━━━━━━━━━━━
            Generated with Headshot Sensi & DPI Tool 🚀
        """.trimIndent()
    }

    fun sharePreset(context: Context, preset: SensitivityPreset) {
        val shareText = formatPresetShareText(preset)
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, shareText)
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, "Share Sensi Settings to Squad")
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(shareIntent)
    }

    fun copyToClipboard(context: Context, label: String, text: String) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText(label, text)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(context, "Copied to Clipboard! 📋", Toast.LENGTH_SHORT).show()
    }

    fun shareCustomDpi(context: Context, deviceName: String, dpi: Int, multiplier: Float) {
        val text = """
            📐 ━━━ GAMING DPI CONFIG ━━━ 📐
            📱 Device: $deviceName
            ⚡ Custom Smallest Width (DPI): $dpi dp
            🚀 Sensitivity Multiplier: ${String.format("%.2fx", multiplier)}
            💡 Apply in: Settings > Developer Options > Smallest Width
            ━━━━━━━━━━━━━━━━━━━━━━━━
            Generated with Headshot Sensi & DPI Tool 🚀
        """.trimIndent()

        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, text)
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, "Share DPI Configuration")
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(shareIntent)
    }
}
