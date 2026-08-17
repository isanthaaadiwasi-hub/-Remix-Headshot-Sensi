package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [SensitivityPreset::class, DpiHistoryItem::class, UserProfile::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun sensitivityDao(): SensitivityDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "gaming_sensi_database"
                )
                .addCallback(DatabaseCallback(scope))
                .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateInitialData(database.sensitivityDao())
                    }
                }
            }
        }

        suspend fun populateInitialData(dao: SensitivityDao) {
            val defaultPresets = listOf(
                SensitivityPreset(
                    name = "⚡ One-Tap Drag King (M1887 & Deagle)",
                    brand = "Universal Pro",
                    gameMode = "One-Tap / Clash Squad",
                    general = 98,
                    redDot = 94,
                    scope2x = 88,
                    scope4x = 84,
                    sniperScope = 60,
                    freeLook = 80,
                    fireButtonSize = 42,
                    recommendedDpi = 440,
                    isFavorite = true,
                    isPreset = true,
                    notes = "Best for quick upward swipe drag headshots with Shotguns and Desert Eagle."
                ),
                SensitivityPreset(
                    name = "🎯 Samsung Galaxy Pro (S & A Series)",
                    brand = "Samsung",
                    gameMode = "Battle Royale Ranked",
                    general = 96,
                    redDot = 92,
                    scope2x = 86,
                    scope4x = 82,
                    sniperScope = 58,
                    freeLook = 70,
                    fireButtonSize = 46,
                    recommendedDpi = 411,
                    isFavorite = true,
                    isPreset = true,
                    notes = "Optimized for Samsung OneUI touch sampling rate and 120Hz AMOLED displays."
                ),
                SensitivityPreset(
                    name = "🔥 Xiaomi / Redmi / Poco Turbo",
                    brand = "Xiaomi",
                    gameMode = "High FPS Rush",
                    general = 100,
                    redDot = 98,
                    scope2x = 90,
                    scope4x = 85,
                    sniperScope = 62,
                    freeLook = 85,
                    fireButtonSize = 44,
                    recommendedDpi = 449,
                    isFavorite = true,
                    isPreset = true,
                    notes = "Engineered for MIUI/HyperOS touch boost and aggressive recoil control."
                ),
                SensitivityPreset(
                    name = "⚡ Realme / Oppo / OnePlus Smooth",
                    brand = "Realme",
                    gameMode = "All Rounder",
                    general = 95,
                    redDot = 90,
                    scope2x = 85,
                    scope4x = 80,
                    sniperScope = 55,
                    freeLook = 75,
                    fireButtonSize = 48,
                    recommendedDpi = 420,
                    isFavorite = false,
                    isPreset = true,
                    notes = "Balanced smooth drag and 360 camera movement for ColorOS/OxygenOS."
                ),
                SensitivityPreset(
                    name = "🦅 AWM / Kar98k Sniper God",
                    brand = "Sniper Pro",
                    gameMode = "Long Range Precision",
                    general = 88,
                    redDot = 82,
                    scope2x = 78,
                    scope4x = 75,
                    sniperScope = 48,
                    freeLook = 65,
                    fireButtonSize = 52,
                    recommendedDpi = 392,
                    isFavorite = false,
                    isPreset = true,
                    notes = "Maximum crosshair stability for fast-switch double sniper flick shots."
                ),
                SensitivityPreset(
                    name = "🚀 iPhone / iOS Ultra Responsive",
                    brand = "Apple",
                    gameMode = "Ultra Low Latency",
                    general = 92,
                    redDot = 88,
                    scope2x = 82,
                    scope4x = 78,
                    sniperScope = 50,
                    freeLook = 68,
                    fireButtonSize = 40,
                    recommendedDpi = 414,
                    isFavorite = false,
                    isPreset = true,
                    notes = "Calibrated for high touch polling rate and precision cursor acceleration."
                ),
                SensitivityPreset(
                    name = "💥 Vivo & iQOO Monster Touch",
                    brand = "Vivo",
                    gameMode = "Ultra Touch Rate",
                    general = 97,
                    redDot = 95,
                    scope2x = 89,
                    scope4x = 83,
                    sniperScope = 60,
                    freeLook = 82,
                    fireButtonSize = 45,
                    recommendedDpi = 460,
                    isFavorite = false,
                    isPreset = true,
                    notes = "Tuned for FuntouchOS game acceleration mode and fast micro-drags."
                )
            )

            dao.insertPresets(defaultPresets)
            dao.saveUserProfile(UserProfile())
        }
    }
}
