package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface SensitivityDao {
    @Query("SELECT * FROM sensitivity_presets ORDER BY isFavorite DESC, timestamp DESC")
    fun getAllPresets(): Flow<List<SensitivityPreset>>

    @Query("SELECT * FROM sensitivity_presets WHERE id = :id")
    suspend fun getPresetById(id: Long): SensitivityPreset?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPreset(preset: SensitivityPreset): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPresets(presets: List<SensitivityPreset>)

    @Update
    suspend fun updatePreset(preset: SensitivityPreset)

    @Delete
    suspend fun deletePreset(preset: SensitivityPreset)

    @Query("DELETE FROM sensitivity_presets WHERE id = :id")
    suspend fun deletePresetById(id: Long)

    @Query("UPDATE sensitivity_presets SET isFavorite = :isFav WHERE id = :id")
    suspend fun updateFavorite(id: Long, isFav: Boolean)

    // DPI History
    @Query("SELECT * FROM dpi_history ORDER BY timestamp DESC LIMIT 20")
    fun getDpiHistory(): Flow<List<DpiHistoryItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDpiHistory(item: DpiHistoryItem): Long

    @Query("DELETE FROM dpi_history")
    suspend fun clearDpiHistory()

    @Query("SELECT COUNT(*) FROM sensitivity_presets")
    suspend fun getPresetsCount(): Int

    // User Profile
    @Query("SELECT * FROM user_profile WHERE id = 1")
    fun getUserProfile(): Flow<UserProfile?>

    @Query("SELECT * FROM user_profile WHERE id = 1")
    suspend fun getUserProfileSync(): UserProfile?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveUserProfile(profile: UserProfile)
}
