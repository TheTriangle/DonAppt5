package com.example.donappt5.data.model

import android.net.Uri
import com.google.firebase.firestore.DocumentSnapshot

class User(var username: String?, var email: String?, var photoUrl: Uri?, var uid: String) {
    companion object {
        lateinit var currentUser: User
        fun DocumentSnapshot.toUser(): User {
            return User(this.data?.get("name") as String?,
                        this.data?.get("mail") as String?,
                        Uri.parse(this.data?.get("photo") as String??: ""),
                        this.id)
        }
    }
}