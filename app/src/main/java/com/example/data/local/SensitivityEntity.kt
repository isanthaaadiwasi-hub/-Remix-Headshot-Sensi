package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sensitivity_presets")
data class SensitivityPreset(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val brand: String = "Custom",
    val gameMode: String = "General / BR",
    val general: Int = 95,
    val redDot: Int = 90,
    val scope2x: Int = 85,
    val scope4x: Int = 80,
    val sniperScope: Int = 65,
    val freeLook: Int = 75,
    val fireButtonSize: Int = 45,
    val recommendedDpi: Int = 411,
    val isFavorite: Boolean = false,
    val isPreset: Boolean = false,
    val notes: String = "",
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "dpi_history")
data class DpiHistoryItem(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val deviceName: String,
    val defaultWidthDp: Int,
    val customDpi: Int,
    val sensitivityMultiplier: Float,
    val refreshRateHz: Int,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "user_profile")
data class UserProfile(
    @PrimaryKey
    val id: Int = 1,
    val gamerTag: String = "SHADOW_KILLER",
    val rank: String = "Grandmaster",
    val favoriteWeapon: String = "M1887 & Desert Eagle",
    val playstyle: String = "One-Tap Rusher",
    val customDpi: Int = 440,
    val isHindiLanguage: Boolean = false,
    val soundEffects: Boolean = true
)
