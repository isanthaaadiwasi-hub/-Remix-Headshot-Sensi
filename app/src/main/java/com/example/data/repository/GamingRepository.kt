package com.example.data.repository

import com.example.data.local.DpiHistoryItem
import com.example.data.local.SensitivityDao
import com.example.data.local.SensitivityPreset
import com.example.data.local.UserProfile
import kotlinx.coroutines.flow.Flow

class GamingRepository(private val dao: SensitivityDao) {

    val allPresets: Flow<List<SensitivityPreset>> = dao.getAllPresets()
    val dpiHistory: Flow<List<DpiHistoryItem>> = dao.getDpiHistory()
    val userProfile: Flow<UserProfile?> = dao.getUserProfile()

    suspend fun savePreset(preset: SensitivityPreset): Long {
        return dao.insertPreset(preset)
    }

    suspend fun updatePreset(preset: SensitivityPreset) {
        dao.updatePreset(preset)
    }

    suspend fun deletePreset(preset: SensitivityPreset) {
        dao.deletePreset(preset)
    }

    suspend fun deletePresetById(id: Long) {
        dao.deletePresetById(id)
    }

    suspend fun toggleFavorite(id: Long, currentFav: Boolean) {
        dao.updateFavorite(id, !currentFav)
    }

    suspend fun addDpiHistory(item: DpiHistoryItem): Long {
        return dao.insertDpiHistory(item)
    }

    suspend fun clearDpiHistory() {
        dao.clearDpiHistory()
    }

    suspend fun saveProfile(profile: UserProfile) {
        dao.saveUserProfile(profile)
    }

    suspend fun seedDatabaseIfEmpty() {
        if (dao.getUserProfileSync() == null) {
            dao.saveUserProfile(UserProfile())
        }
        if (dao.getPresetsCount() == 0) {
            com.example.data.local.AppDatabase.populateInitialData(dao)
        }
    }
}
