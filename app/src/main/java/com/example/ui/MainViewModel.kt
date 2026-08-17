package com.example.ui

import android.app.Application
import android.content.Context
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.local.DpiHistoryItem
import com.example.data.local.SensitivityPreset
import com.example.data.local.UserProfile
import com.example.data.repository.GamingRepository
import com.example.model.DeviceInfoHelper
import com.example.model.DeviceSpecs
import com.example.util.PingUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class AppScreen(val titleEn: String, val titleHi: String) {
    HOME("Home", "होम"),
    SENSITIVITY("Sensitivity", "सेंसिटिविटी"),
    DPI_CALCULATOR("DPI Calculator", "DPI कैलकुलेटर"),
    PROFILE("Profile", "प्रोफ़ाइल")
}

data class CurrentSensiState(
    val name: String = "⚡ Custom Headshot Setup",
    val brand: String = "Universal",
    val gameMode: String = "One-Tap / BR",
    val general: Int = 98,
    val redDot: Int = 94,
    val scope2x: Int = 88,
    val scope4x: Int = 84,
    val sniperScope: Int = 60,
    val freeLook: Int = 75,
    val fireButtonSize: Int = 45,
    val recommendedDpi: Int = 440,
    val notes: String = "High accuracy drag setting"
)

data class DpiCalculatorState(
    val baseWidthDp: Int = 360,
    val targetDpi: Int = 440,
    val refreshRateHz: Int = 90,
    val deviceName: String = "",
    val touchSensitivityMultiplier: Float = 1.22f,
    val estimatedSwipeDistanceCm: Float = 4.2f,
    val isSafeDpi: Boolean = true
)

data class TouchTestState(
    val isTesting: Boolean = false,
    val touchLatencyMs: Long = 0,
    val swipeSpeedPxPerSec: Float = 0f,
    val totalTouches: Int = 0,
    val accuracyRating: String = "Ready"
)

data class PingState(
    val isTesting: Boolean = false,
    val pingMs: Long = -1,
    val pingStatus: String = "Idle"
)

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: GamingRepository
    val deviceSpecs: DeviceSpecs

    val presets: StateFlow<List<SensitivityPreset>>
    val dpiHistory: StateFlow<List<DpiHistoryItem>>
    val userProfile: StateFlow<UserProfile?>

    private val _currentScreen = MutableStateFlow(AppScreen.HOME)
    val currentScreen: StateFlow<AppScreen> = _currentScreen.asStateFlow()

    private val _sensiState = MutableStateFlow(CurrentSensiState())
    val sensiState: StateFlow<CurrentSensiState> = _sensiState.asStateFlow()

    private val _dpiState = MutableStateFlow(DpiCalculatorState())
    val dpiState: StateFlow<DpiCalculatorState> = _dpiState.asStateFlow()

    private val _touchTest = MutableStateFlow(TouchTestState())
    val touchTest: StateFlow<TouchTestState> = _touchTest.asStateFlow()

    private val _pingState = MutableStateFlow(PingState())
    val pingState: StateFlow<PingState> = _pingState.asStateFlow()

    private val _isHindi = MutableStateFlow(false)
    val isHindi: StateFlow<Boolean> = _isHindi.asStateFlow()

    init {
        val db = AppDatabase.getDatabase(application, viewModelScope)
        repository = GamingRepository(db.sensitivityDao())
        deviceSpecs = DeviceInfoHelper.getDeviceSpecs(application)

        presets = repository.allPresets.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        dpiHistory = repository.dpiHistory.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        userProfile = repository.userProfile.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UserProfile()
        )

        viewModelScope.launch(Dispatchers.IO) {
            repository.seedDatabaseIfEmpty()
        }

        // Initialize DPI State with actual device properties
        val baseWidth = deviceSpecs.smallestWidthDp.coerceAtLeast(320)
        val initialDpi = (baseWidth * 1.15f).toInt().coerceIn(360, 600)
        updateDpiCalculation(baseWidth, initialDpi, deviceSpecs.refreshRateHz, "${deviceSpecs.manufacturer} ${deviceSpecs.model}")
    }

    fun navigateTo(screen: AppScreen) {
        _currentScreen.value = screen
    }

    fun toggleLanguage() {
        val newLang = !_isHindi.value
        _isHindi.value = newLang
        viewModelScope.launch {
            userProfile.value?.let {
                repository.saveProfile(it.copy(isHindiLanguage = newLang))
            }
        }
    }

    fun updateSensiValues(
        general: Int? = null,
        redDot: Int? = null,
        scope2x: Int? = null,
        scope4x: Int? = null,
        sniperScope: Int? = null,
        freeLook: Int? = null,
        fireButtonSize: Int? = null,
        recommendedDpi: Int? = null
    ) {
        _sensiState.value = _sensiState.value.copy(
            general = general ?: _sensiState.value.general,
            redDot = redDot ?: _sensiState.value.redDot,
            scope2x = scope2x ?: _sensiState.value.scope2x,
            scope4x = scope4x ?: _sensiState.value.scope4x,
            sniperScope = sniperScope ?: _sensiState.value.sniperScope,
            freeLook = freeLook ?: _sensiState.value.freeLook,
            fireButtonSize = fireButtonSize ?: _sensiState.value.fireButtonSize,
            recommendedDpi = recommendedDpi ?: _sensiState.value.recommendedDpi
        )
    }

    fun applyPreset(preset: SensitivityPreset) {
        _sensiState.value = CurrentSensiState(
            name = preset.name,
            brand = preset.brand,
            gameMode = preset.gameMode,
            general = preset.general,
            redDot = preset.redDot,
            scope2x = preset.scope2x,
            scope4x = preset.scope4x,
            sniperScope = preset.sniperScope,
            freeLook = preset.freeLook,
            fireButtonSize = preset.fireButtonSize,
            recommendedDpi = preset.recommendedDpi,
            notes = preset.notes
        )
        // Also update DPI target if appropriate
        updateDpiCalculation(
            _dpiState.value.baseWidthDp,
            preset.recommendedDpi,
            _dpiState.value.refreshRateHz,
            _dpiState.value.deviceName
        )
        vibratePhone()
    }

    fun generateAutoPreset(ramGb: Int, refreshRate: Int, playstyle: String) {
        val general = when {
            ramGb <= 3 -> 100
            ramGb <= 6 -> 96
            else -> 92
        }
        val redDot = (general - 4).coerceIn(80, 100)
        val scope2x = (general - 10).coerceIn(70, 95)
        val scope4x = (general - 14).coerceIn(65, 90)
        val sniper = when (playstyle) {
            "Sniper" -> 55
            "One-Tap" -> 62
            else -> 50
        }
        val freeLook = if (refreshRate >= 90) 75 else 85
        val buttonSize = if (ramGb <= 4) 48 else 42
        val dpi = when {
            ramGb <= 3 -> 392
            ramGb <= 6 -> 440
            else -> 480
        }

        _sensiState.value = CurrentSensiState(
            name = "⚡ AI Auto-Tuned for ${deviceSpecs.model}",
            brand = deviceSpecs.manufacturer,
            gameMode = playstyle,
            general = general,
            redDot = redDot,
            scope2x = scope2x,
            scope4x = scope4x,
            sniperScope = sniper,
            freeLook = freeLook,
            fireButtonSize = buttonSize,
            recommendedDpi = dpi,
            notes = "Calibrated for $ramGb GB RAM & ${refreshRate}Hz screen."
        )
        vibratePhone()
    }

    fun saveCurrentPreset(name: String, notes: String, brand: String = "Custom") {
        viewModelScope.launch {
            val preset = SensitivityPreset(
                name = name.ifBlank { "Custom Headshot Sensi" },
                brand = brand,
                gameMode = _sensiState.value.gameMode,
                general = _sensiState.value.general,
                redDot = _sensiState.value.redDot,
                scope2x = _sensiState.value.scope2x,
                scope4x = _sensiState.value.scope4x,
                sniperScope = _sensiState.value.sniperScope,
                freeLook = _sensiState.value.freeLook,
                fireButtonSize = _sensiState.value.fireButtonSize,
                recommendedDpi = _sensiState.value.recommendedDpi,
                notes = notes,
                isFavorite = false,
                isPreset = false
            )
            repository.savePreset(preset)
            vibratePhone()
        }
    }

    fun deletePreset(preset: SensitivityPreset) {
        viewModelScope.launch {
            repository.deletePreset(preset)
        }
    }

    fun toggleFavorite(preset: SensitivityPreset) {
        viewModelScope.launch {
            repository.toggleFavorite(preset.id, preset.isFavorite)
        }
    }

    fun updateDpiCalculation(baseWidth: Int, targetDpi: Int, refreshRate: Int, deviceName: String) {
        val multiplier = if (baseWidth > 0) targetDpi.toFloat() / baseWidth.toFloat() else 1.0f
        val swipeDistance = 5.0f / multiplier
        val isSafe = targetDpi in 320..600

        _dpiState.value = DpiCalculatorState(
            baseWidthDp = baseWidth,
            targetDpi = targetDpi,
            refreshRateHz = refreshRate,
            deviceName = deviceName.ifBlank { "${deviceSpecs.manufacturer} ${deviceSpecs.model}" },
            touchSensitivityMultiplier = multiplier,
            estimatedSwipeDistanceCm = swipeDistance,
            isSafeDpi = isSafe
        )
    }

    fun saveDpiToHistory() {
        val current = _dpiState.value
        viewModelScope.launch {
            repository.addDpiHistory(
                DpiHistoryItem(
                    deviceName = current.deviceName,
                    defaultWidthDp = current.baseWidthDp,
                    customDpi = current.targetDpi,
                    sensitivityMultiplier = current.touchSensitivityMultiplier,
                    refreshRateHz = current.refreshRateHz
                )
            )
            vibratePhone()
        }
    }

    fun clearDpiHistory() {
        viewModelScope.launch {
            repository.clearDpiHistory()
        }
    }

    fun updateUserProfile(gamerTag: String, rank: String, favoriteWeapon: String, playstyle: String) {
        viewModelScope.launch {
            val updated = (userProfile.value ?: UserProfile()).copy(
                gamerTag = gamerTag,
                rank = rank,
                favoriteWeapon = favoriteWeapon,
                playstyle = playstyle
            )
            repository.saveProfile(updated)
            vibratePhone()
        }
    }

    fun runNetworkPingTest() {
        _pingState.value = PingState(isTesting = true, pingMs = -1, pingStatus = "Testing...")
        viewModelScope.launch {
            val latency = PingUtils.measureNetworkPing()
            val status = when {
                latency < 0 -> "Offline / Timeout ❌"
                latency < 40 -> "Pro Esports Grade 🟢"
                latency < 80 -> "Good for Ranked 🟡"
                else -> "High Latency 🔴"
            }
            _pingState.value = PingState(isTesting = false, pingMs = latency, pingStatus = status)
        }
    }

    fun recordTouchGesture(dragDistancePx: Float, durationMs: Long) {
        val speed = if (durationMs > 0) (dragDistancePx / durationMs) * 1000f else 0f
        val currentCount = _touchTest.value.totalTouches + 1
        val rating = when {
            speed > 3500 -> "⚡ Ultra Fast Flick (God Tier)"
            speed > 2000 -> "🎯 High Speed Drag (Pro)"
            speed > 1000 -> "🔥 Balanced Smooth Drag"
            else -> "🐢 Slow Micro-Adjustment"
        }
        _touchTest.value = TouchTestState(
            isTesting = false,
            touchLatencyMs = durationMs,
            swipeSpeedPxPerSec = speed,
            totalTouches = currentCount,
            accuracyRating = rating
        )
    }

    fun vibratePhone() {
        try {
            val context = getApplication<Application>()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
                vibratorManager?.defaultVibrator?.vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_CLICK))
            } else {
                @Suppress("DEPRECATION")
                val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
                @Suppress("DEPRECATION")
                vibrator?.vibrate(30)
            }
        } catch (_: Exception) {}
    }
}
