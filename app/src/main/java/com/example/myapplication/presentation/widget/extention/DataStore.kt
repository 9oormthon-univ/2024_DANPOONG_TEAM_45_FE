package com.example.myapplication.presentation.widget.extention

import android.content.Context
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.preferencesDataStore


private const val DATA_STORE = "data_store"

val Context.dataStore by preferencesDataStore(
    name = DATA_STORE,
    produceMigrations = { context ->
        listOf(
            //마이그레이션 키
            SharedPreferencesMigration(
                context = context,
                sharedPreferencesName = DATA_STORE,
                keysToMigrate = setOf(

                )
            )
        )
    }
)