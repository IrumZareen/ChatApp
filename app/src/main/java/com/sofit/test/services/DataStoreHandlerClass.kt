package com.sofit.test.services

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.sofit.test.model.User
import kotlinx.coroutines.flow.map


class DataStoreHandlerClass(val context: Context) {
    private val Context.userPreferencesDataStore: DataStore<Preferences> by preferencesDataStore(
        name = "user_data"
    )

    private val USER_NAME = stringPreferencesKey("user_name")
    private val USER_EMAIL = stringPreferencesKey("user_email")
    private val LOGO_PATH = stringPreferencesKey("user_profile_pic")
    private val USER_COUNTRY = stringPreferencesKey("user_country")


    suspend fun saveUserToPreferencesStore(user: User) {
        context.userPreferencesDataStore.edit { preferences ->
            preferences[USER_NAME] = user.name!!
            preferences[USER_EMAIL] = user.email!!
            preferences[LOGO_PATH] = user.profilePic!!
            preferences[USER_COUNTRY] = user.country!!

        }
    }

    fun getUserFromPreferencesStore() = context.userPreferencesDataStore.data
        .map { preferences ->
            User(
                name = preferences[USER_NAME] ?: "",
                email = preferences[USER_EMAIL] ?: "",
                profilePic = preferences[LOGO_PATH] ?: "",
                country = preferences[USER_COUNTRY] ?: "",
            )
        }

}