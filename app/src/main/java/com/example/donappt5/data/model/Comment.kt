package com.example.donappt5.data.model

import android.util.Log
import com.example.donappt5.data.util.Util.toLocalDateTime
import com.google.firebase.firestore.DocumentSnapshot
import java.time.LocalDateTime

class Comment {
    var id: String
    var text: String
    var date: LocalDateTime
    var organization: Boolean
    var userId: String
    var userName: String

    constructor(
        gid: String?,
        gtext: String?,
        gdate: LocalDateTime?,
        gorganization: Boolean?,
        guserId: String,
        guserName: String,
    ) {
        id = gid?: ""
        text = gtext?: ""
        date = gdate?: LocalDateTime.MIN
        organization = gorganization?: false
        userId = guserId?: ""
        userName = guserName?: ""
    }

    constructor(document: DocumentSnapshot) {
        id = document.id
        text = document.getString("text")?: ""
        date = document.getTimestamp("date")?.toLocalDateTime()?: LocalDateTime.now()
        organization = document.getBoolean("organisation")?: false
        userId = document.getString("uid")?: ""
        userName = document.getString("username")?: ""
    }

    companion object {
        fun DocumentSnapshot.toCommment(): Comment? {
            try {
                return Comment(this)
            } catch (e: Exception) {
                Log.e(TAG, "Error converting campaign", e)
                return null
            }
        }

        private const val TAG = "Comment"
    }
}