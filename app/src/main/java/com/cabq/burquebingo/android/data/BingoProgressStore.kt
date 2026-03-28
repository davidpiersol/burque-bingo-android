package com.cabq.burquebingo.android.data

import android.content.Context
import org.json.JSONArray

/**
 * Persists marked square IDs only (same key scheme as Flutter: `burque_bingo_marked_{cardId}`).
 * Note: Flutter uses a different prefs file, so marks do not sync across the two apps.
 */
class BingoProgressStore(context: Context) {
    private val app = context.applicationContext
    private val prefs = app.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun loadMarked(cardId: String): Set<String> {
        val raw = prefs.getString(KEY_PREFIX + cardId, null) ?: return emptySet()
        if (raw.isEmpty()) return emptySet()
        return try {
            val arr = JSONArray(raw)
            buildSet {
                for (i in 0 until arr.length()) {
                    add(arr.getString(i))
                }
            }
        } catch (_: Exception) {
            emptySet()
        }
    }

    fun saveMarked(cardId: String, ids: Set<String>) {
        val sorted = ids.toList().sorted()
        val arr = JSONArray()
        for (id in sorted) arr.put(id)
        prefs.edit().putString(KEY_PREFIX + cardId, arr.toString()).apply()
    }

    companion object {
        private const val PREFS_NAME = "burque_bingo_android"
        private const val KEY_PREFIX = "burque_bingo_marked_"
    }
}
