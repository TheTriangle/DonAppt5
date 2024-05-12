package com.example.donappt5.data.util

import android.content.Intent
import android.os.Build
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.Timestamp
import java.io.Serializable
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

object Util {
    private const val ALLOWED_ID_CHARACTERS =
        "0123456789qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM"

    private const val BASE32_CHARS = "0123456789bcdefghjkmnpqrstuvwxyz"

    fun <T : Serializable?> getSerializable(intent: Intent, key: String, m_class: Class<T>): T {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            intent.getSerializableExtra(key, m_class)!!
        else
            intent.getSerializableExtra(key) as T
    }

    fun getRandomString(sizeOfRandomString: Int): String {
        val random = Random()
        val sb = StringBuilder(sizeOfRandomString)
        for (i in 0 until sizeOfRandomString) sb.append(
            ALLOWED_ID_CHARACTERS[random.nextInt(
                ALLOWED_ID_CHARACTERS.length
            )]
        )
        return sb.toString()
    }

    fun getAveragePosition(pos1: LatLng, pos2: LatLng): LatLng {
        return LatLng((pos1.latitude + pos2.latitude)/2,
                     (pos1.longitude + pos2.longitude)/2)
    }

    fun getLexographicalAverage(str1: String, str2: String): String {
        var ans = ""
        for (i in 0 until minOf(str1.length, str2.length)) {
            ans += Util.BASE32_CHARS[(BASE32_CHARS.indexOf(str1[i]) + BASE32_CHARS.indexOf(str2[i]))/2]
        }
        return ans
    }

    fun LocalDateTime.toTimestamp(zone: ZoneId = ZoneId.systemDefault())
        = Timestamp(Date.from(this.toInstant(zone.rules.getOffset(Instant.now()))))

    fun Instant.toTimestamp()
            = Timestamp(Date.from(this))

    fun Timestamp.toLocalDateTime(zone: ZoneId = ZoneId.systemDefault()) = LocalDateTime.ofInstant(
        Instant.ofEpochMilli(seconds * 1000 + nanoseconds / 1000000), zone)

    var FILLING_ALPHABET = 0
    var FILLING_DISTANCE = 1
    var FILLING_SEARCH = 2
    var FILLING_FAVORITES = 3
}