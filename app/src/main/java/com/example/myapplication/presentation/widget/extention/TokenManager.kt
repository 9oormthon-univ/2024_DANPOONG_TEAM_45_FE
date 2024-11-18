package com.example.myapplication.presentation.widget.extention

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.edit
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

    val getUserNickname: Flow<String?> = dataStore.data
        .map { preferences ->
            preferences[USER_NICKNAME_KEY]
        }

    suspend fun saveUserNickname(nickname: String) {
        dataStore.edit { preferences ->
            preferences[USER_NICKNAME_KEY] = nickname
        }
    }

    val getRefreshToken : Flow<String?> = dataStore.data
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

}