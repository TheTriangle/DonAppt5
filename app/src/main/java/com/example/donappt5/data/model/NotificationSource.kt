package com.example.donappt5.data.model

import androidx.compose.runtime.mutableStateOf
import com.example.donappt5.data.services.FirestoreService
import com.google.firebase.firestore.DocumentSnapshot

class NotificationSource {
    var sourceId: String = ""
    var sourceName: String = ""
    var subscribed = mutableStateOf(true)

    fun subscribe() {
        subscribed.value = true
        FirestoreService.subscribeTo(sourceId, sourceName)
    }

    fun unsubscribe() {
        subscribed.value = false
        FirestoreService.unsubscribeFrom(sourceId)
    }

    constructor(gsourceId: String, gsourceName: String) {
        sourceId = gsourceId
        sourceName = gsourceName
    }

    constructor(document: DocumentSnapshot) {
        sourceId = document.getString("sourceid")?: ""
        sourceName = document.getString("sourcename")?: ""
    }

    companion object {
        fun DocumentSnapshot.toNotificationSource(): NotificationSource {
            return NotificationSource(this)
        }
    }
}