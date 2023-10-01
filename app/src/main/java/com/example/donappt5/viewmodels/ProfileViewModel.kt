package com.example.donappt5.viewmodels

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.donappt5.data.services.FirestoreService
import com.example.donappt5.data.util.MyGlobals
import com.example.donappt5.data.util.Response
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProfileViewModel : ViewModel() {
    var loadedUri: Uri? = null
    var photourl = MutableLiveData<Response<String>>()
    var myGlobals: MyGlobals? = null

    init {
        photourl.postValue(Response.loading(null))
        FirestoreService.getUserData().addOnCompleteListener { documentSnapshot ->
            val url = documentSnapshot.result.getString("photourl")
            photourl.postValue(Response.success(url))
        }
    }

    fun updateUserName(ans: String) : String{
        val user = FirebaseAuth.getInstance().currentUser
        val db = FirebaseFirestore.getInstance()
        val update =
            HashMap<String, Any>()
        update["name"] = ans
        db.collection("users").document(user!!.uid)
            .update(update)
        return ans
    }
}