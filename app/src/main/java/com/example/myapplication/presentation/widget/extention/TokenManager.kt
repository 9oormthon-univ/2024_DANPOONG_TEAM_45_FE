package com.example.myapplication.presentation.widget.extention

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesOf
import androidx.datastore.preferences.core.stringPreferencesKey
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TokenManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val dataStore = context.dataStore

    companion object {
        private val REFRESH_TOKEN_KEY = stringPreferencesKey("refresh_token")
        private val ACCESS_TOKEN_KEY = stringPreferencesKey("access_token")
        private val LIST_KEY = stringPreferencesKey("list_key")
        private val USER_NICKNAME_KEY = stringPreferencesKey("nickname_key")
        private val USER_PROFILE_KEY = stringPreferencesKey("profile_key")
        private val CHARACTER_KEY = stringPreferencesKey("character_id")
        private val CHAPTER_KEY = stringPreferencesKey("chapter_iscleared")
        private val USER_KEY = stringPreferencesKey("user_id")
        private val DATE_KEY = stringPreferencesKey("date")
        private val COUNT_KEY = stringPreferencesKey("count")
        private val TUTORIAL_1 = stringPreferencesKey("tut1")
        private val TUTORIAL_2 = stringPreferencesKey("tut2")
    }

    suspend fun deleteHome() {
        dataStore.edit { preferences ->
            preferences[USER_KEY] = ""
            preferences[CHARACTER_KEY] = ""
        }
    }

    val getAccessToken: Flow<String?> = dataStore.data
        .map { preferences ->
            preferences[ACCESS_TOKEN_KEY]
        }

    suspend fun saveAccessToken(token: String) {
        dataStore.edit { preferences ->
            preferences[ACCESS_TOKEN_KEY] = token
        }
    }

    suspend fun deleteAccessToken() {
        dataStore.edit { preferences ->
            preferences[ACCESS_TOKEN_KEY] = ""
        }
    }

    val getUserProfile: Flow<String?> = dataStore.data
        .map { preferences ->
            preferences[USER_PROFILE_KEY]
        }

    suspend fun saveUserProfile(profileUrl: String) {
        dataStore.edit { preferences ->
            preferences[USER_PROFILE_KEY] = profileUrl
        }
    }

    val getChapterIsCleared: Flow<String?> = dataStore.data
        .map { preferences ->
            preferences[CHAPTER_KEY]
        }

    suspend fun saveChapterIsCleared(isCleared: String) {
        dataStore.edit { preferences ->
            preferences[CHAPTER_KEY] = isCleared
        }
    }

    val getUserNickname: Flow<String?> = dataStore.data
        .map { preferences ->
            preferences[USER_NICKNAME_KEY]
        }

    suspend fun saveUserNickname(nickname: String) {
        dataStore.edit { preferences ->
            preferences[USER_NICKNAME_KEY] = nickname
        }
    }

    val getUserId: Flow<String?> = dataStore.data
        .map { preferences ->
            preferences[USER_KEY]
        }

    suspend fun saveUserId(id: String) {
        dataStore.edit { preferences ->
            preferences[USER_KEY] = id
        }
    }

    val getCharacterId: Flow<String?> = dataStore.data
        .map { preferences ->
            preferences[CHARACTER_KEY]
        }

    suspend fun saveCharacterId(id: String) {
        dataStore.edit { preferences ->
            preferences[CHARACTER_KEY] = id
        }
    }


    val getRefreshToken: Flow<String?> = dataStore.data
        .map { preferences ->
            preferences[REFRESH_TOKEN_KEY]
        }

    suspend fun saveRefreshToken(token: String) {
        dataStore.edit { preferences ->
            preferences[REFRESH_TOKEN_KEY] = token
        }
    }

    // 리스트 저장 함수
    suspend fun saveList(list: List<String>) {
        val json = Gson().toJson(list)  // 리스트를 JSON 문자열로 변환
        dataStore.edit { preferences ->
            preferences[LIST_KEY] = json
            Log.d("저장 리스트", "저장된 JSON: $json") // 저장된 JSON 출력
        }
    }

    // 리스트 불러오기 함수
    fun getList(): Flow<List<String>> {
        return dataStore.data.map { preferences ->
            val json = preferences[LIST_KEY] ?: "[]"  // 저장된 JSON 문자열을 가져옴
            Log.d("가저온 리스트", "저장된 JSON: $json") // 저장된 JSON 출력
            Gson().fromJson(json, object : TypeToken<List<String>>() {}.type) ?: emptyList()
        }
    }


    suspend fun saveDateToken(token: String) {
        dataStore.edit { preferences ->
            preferences[DATE_KEY] = token
        }
    }

    val getDateToken: Flow<String?> = dataStore.data
        .map { preferences ->
            preferences[DATE_KEY]
        }

    suspend fun saveCountToken(token: String) {
        dataStore.edit { preferences ->
            preferences[COUNT_KEY] = token
        }
    }

    val getCountToken: Flow<String?> = dataStore.data
        .map { preferences ->
            preferences[COUNT_KEY]
        }

    suspend fun saveTut1(token: String) {
        dataStore.edit { preferences ->
            preferences[TUTORIAL_1] = token
        }
    }

    val getTut1: Flow<String?> = dataStore.data
        .map { preferences ->
            preferences[TUTORIAL_1]
        }

    suspend fun saveTut2(token: String) {
        dataStore.edit { preferences ->
            preferences[TUTORIAL_2] = token
        }
    }

    val getTut2: Flow<String?> = dataStore.data
        .map { preferences ->
            preferences[TUTORIAL_2]
        }

}