package com.example.donappt5.viewmodels

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.donappt5.data.model.Charity
import com.example.donappt5.data.services.FirestoreService
import com.example.donappt5.data.util.Response
import com.example.donappt5.util.Event
import com.example.donappt5.util.Util
import com.example.donappt5.views.charitycreation.CharityCreateDesc
import com.example.donappt5.views.charitycreation.CharityCreatePaymentCredentials
import com.example.donappt5.views.charitycreation.CharityCreationActivity.Companion.CART
import com.example.donappt5.views.charitycreation.CharityCreationActivity.Companion.CEDU
import com.example.donappt5.views.charitycreation.CharityCreationActivity.Companion.CHEAL
import com.example.donappt5.views.charitycreation.CharityCreationActivity.Companion.CKIDS
import com.example.donappt5.views.charitycreation.CharityCreationActivity.Companion.CPOV
import com.example.donappt5.views.charitycreation.CharityCreationActivity.Companion.CSCI
import com.example.donappt5.views.charitycreation.CharityCreationActivity.Companion.TAGS_COUNT
import com.google.android.gms.tasks.Task
import com.google.common.primitives.Ints
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.koalap.geofirestore.GeoLocation

class CharityEditViewModel : ViewModel() {
    lateinit var charity: Charity
    val repo: FirestoreService = FirestoreService
    val imageUri = MutableLiveData<Uri?>(null)
    val edited = MutableLiveData<Response<Boolean>>()
    val tags = MutableLiveData<Response<Boolean>>()
    val deleted = MutableLiveData<Response<Boolean>>()
    val isNameFree = MutableLiveData<Response<Boolean>>()
    var SELECT_PICTURE = 2878
    var creatingChar: Charity? = null
    private var fileUrl: String? = null
    private var fragDesc: CharityCreateDesc? = null
    private var fragCredentials: CharityCreatePaymentCredentials? = null
    private var latitude = -1000.0
    var ctags = BooleanArray(TAGS_COUNT)
    private var longtitude = -1000.0

    private val _shouldCloseLiveData = MutableLiveData<Event<Boolean>>()
    val shouldCloseLiveData: LiveData<Event<Boolean>> = _shouldCloseLiveData

    fun createCharity(
        name: String,
        fragDesc: CharityCreateDesc?,
        fragCredentials: CharityCreatePaymentCredentials?
    ) {
        this.fragDesc = fragDesc
        this.fragCredentials = fragCredentials
        val db = FirebaseFirestore.getInstance()
        db.collection("charities")
            .whereEqualTo("name", name).get()
            .addOnCompleteListener { task: Task<QuerySnapshot> ->
                if (task.isSuccessful && task.result.documents.size != 0) {
                    createCharityWithID(task.result.documents[0].id, name)
                } else {
                    createCharityWithID(Util.getRandomString(28), name)
                }
            }
    }

    fun createCharityWithID(id: String?, name: String) {
        Log.d("progresstracker", "createCharity")
        creatingChar = Charity()
        creatingChar!!.name = name
        creatingChar!!.firestoreID = id!!
        creatingChar!!.fullDescription = fragDesc!!.text
        creatingChar!!.paymentUrl = fragCredentials!!.getText()
        creatingChar!!.briefDescription = creatingChar!!.fullDescription.substring(
            0, Ints.min(
                creatingChar!!.fullDescription.length, 50
            )
        )
        // Access a Cloud Firestore instance from your Activity
        val db = FirebaseFirestore.getInstance()
        val charity: MutableMap<String, Any> = HashMap()
        val user = FirebaseAuth.getInstance().currentUser
        charity["name"] = creatingChar!!.name
        charity["description"] = creatingChar!!.fullDescription
        charity["qiwiurl"] = creatingChar!!.paymentUrl
        charity["creatorid"] = user!!.uid
        Log.d("storageprogresstracker", "-1")
        if (imageUri.value != null) {
            Log.d("storageprogresstracker", "0")
            val storage = FirebaseStorage.getInstance()
            val storageRef = storage.reference
            val file: Uri? = imageUri.value //Uri.fromFile(new File(pathtoimage));
            val imgsref = storageRef.child("charities" + creatingChar!!.firestoreID + "/photo")
            val uploadTask = imgsref.putFile(file!!)
            uploadTask.addOnFailureListener { exception -> // Handle unsuccessful uploads
                Log.d("storageprogresstracker", "dam$exception")
            }.addOnSuccessListener { taskSnapshot: UploadTask.TaskSnapshot? ->
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
                Log.d("storageprogresstracker", "1")
                storageRef.child("charities" + creatingChar!!.firestoreID + "/photo")
                    .downloadUrl.addOnSuccessListener { uri: Uri ->
                        // Got the download URL for 'users/me/profile.png'
                        Log.d("urlgetter", uri.toString())
                        fileUrl = uri.toString()
                        charity["photourl"] = fileUrl!!
                        db.collection("charities").document(creatingChar!!.firestoreID)
                            .set(charity)
                            .addOnSuccessListener { aVoid: Void? ->
                                FirestoreService.setCharityLocation(
                                    creatingChar!!.firestoreID,
                                    GeoLocation(latitude, longtitude)
                                )
                                putTags()
                            }
                    }
                    .addOnFailureListener { exception: Exception ->
                        // Handle any errors
                        Log.d("storageprogresstracker", "2$exception")
                    }
            }

            //Log.d("urlgetter", fileUrl);
        } else {
            Log.d("storageprogresstracker", "3")
            db.collection("charities").document(creatingChar!!.firestoreID)
                .set(charity)
                .addOnSuccessListener {
                    Log.d("Charitycreationlog", "DocumentSnapshot successfully written!")
                    FirestoreService.setCharityLocation(
                        creatingChar!!.firestoreID,
                        GeoLocation(latitude, longtitude)
                    )
                    putTags()
                }
                .addOnFailureListener { e: Exception? ->
                    Log.w(
                        "Charitycreationlog",
                        "Error writing document",
                        e
                    )
                }
        }
    }

    fun putTags() {
        val db = FirebaseFirestore.getInstance()
        val namemap: Map<String, Any> = HashMap()
        val tagsmap: MutableMap<String, Any> = HashMap()
        if (ctags[CART]) {
            db.collection("tags").document("art").collection("list")
                .document(creatingChar!!.firestoreID).set(namemap)
            tagsmap["art"] = true
        } else tagsmap["art"] = false
        if (ctags[CPOV]) {
            db.collection("tags").document("poverty").collection("list")
                .document(creatingChar!!.firestoreID).set(namemap)
            tagsmap["poverty"] = true
        } else tagsmap["poverty"] = false
        if (ctags[CEDU]) {
            db.collection("tags").document("education").collection("list")
                .document(creatingChar!!.firestoreID).set(namemap)
            tagsmap["education"] = true
        } else tagsmap["education"] = false
        if (ctags[CSCI]) {
            db.collection("tags").document("science&research").collection("list").document(
                creatingChar!!.firestoreID
            ).set(namemap)
            tagsmap["science&research"] = true
        } else tagsmap["science&research"] = false
        if (ctags[CKIDS]) {
            db.collection("tags").document("children").collection("list")
                .document(creatingChar!!.firestoreID).set(namemap)
            tagsmap["children"] = true
        } else tagsmap["children"] = false
        if (ctags[CHEAL]) {
            db.collection("tags").document("healthcare").collection("list")
                .document(creatingChar!!.firestoreID).set(namemap)
            tagsmap["healthcare"] = true
        } else tagsmap["healthcare"] = false
        db.collection("charities").document(creatingChar!!.firestoreID).update(tagsmap)
            .addOnSuccessListener { finish() }
    }

    private fun finish(){
        _shouldCloseLiveData.postValue(Event(true))
    }

    fun setGeoInfo(latitude: Double, longtitude: Double){
        this.latitude = latitude
        this.longtitude = longtitude
    }

    fun putTags(tags: Map<String, Boolean>) {
        repo.putTags(charity.firestoreID, tags)
            .addOnSuccessListener {
                this.tags.postValue(Response.success(true))
                Log.d("CharityEditVeiwModel", "putTags: success")
            }
            .addOnFailureListener {
                this.tags.postValue(Response.error(it.message.toString(), null))
            }
    }

    fun editCharity(fields: MutableMap<String, Any>) {
        if (imageUri.value != null) {
            repo.uploadCharityImage(charity.firestoreID, imageUri.value!!)
                .addOnSuccessListener {
                    repo.getImageUrl(charity.firestoreID).addOnSuccessListener { uri ->
                        fields["photourl"] = uri.toString()
                        repo.editCharity(charity.firestoreID, fields).addOnSuccessListener {
                            edited.postValue(Response.success(true))
                        }
                    }
                }
                .addOnFailureListener {
                    Log.d("CharityEditVeiwModel", "editCharity: failed to upload image")
                }
        } else {
            repo.editCharity(charity.firestoreID, fields)
                .addOnSuccessListener {
                    edited.postValue(Response.success(true))
                }
                .addOnFailureListener {
                    edited.postValue(Response.error(it.message.toString(), false))
                }
        }
    }

    fun deleteCharity() {
        repo.deleteCharity(charity.firestoreID)
            .addOnSuccessListener {
                deleted.postValue(Response.success(true))
            }
            .addOnFailureListener {
                deleted.postValue(Response.error(it.message.toString(), false))
            }
    }

    fun checkName(name: String) {
        repo.checkName(name).addOnCompleteListener() {
            if (it.isSuccessful) {
                if (it.result.size() != 0 && name != charity.name) {
                    isNameFree.postValue(Response.success(false))
                } else {
                    isNameFree.postValue(Response.success(true))
                }
            }
        }
    }

    fun checkCreationName(name: String) {
        repo.checkName(name).addOnCompleteListener() {
            if (it.isSuccessful) {
                if (it.result.size() != 0) {
                    isNameFree.postValue(Response.success(false))
                } else {
                    isNameFree.postValue(Response.success(true))
                }
            }
        }
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (data == null) {
            return
        }
        val resultingactivity = data.getStringExtra("resultingactivity")
        Log.d("progresstracker", "resulted activity $resultingactivity")
        if (resultingactivity != null) {
            if (resultingactivity == "LocatorActivity") {
                onLocatorActivityResult(data)
            } else if (resultingactivity == "TagsActivity") {
                onTagsActivityResult(data)
            }
        } else {
            Thread {
                if (resultCode == AppCompatActivity.RESULT_OK) {
                    if (requestCode == SELECT_PICTURE) {
                        // Get the url from data
                        val selectedImageUri = data.data
                        if (null != selectedImageUri) {
                            // Get the path from the Uri
                            imageUri.postValue(selectedImageUri)
                            Log.i("imageloader", "Image URI : $imageUri")
                        }
                    }
                }
            }.start()
        }
    }

    // TODO
    protected fun onLocatorActivityResult(data: Intent?) {
        if (data == null) {
            return
        }
        val coordsgiven = data.getBooleanExtra("locationgiven", false)
        val latitude = data.getDoubleExtra("latitude", 0.0)
        val longitude = data.getDoubleExtra("longitude", 0.0)
        if (coordsgiven) {
            FirestoreService.setCharityLocation(
                charity.firestoreID,
                GeoLocation(latitude, longitude)
            )
        }
    }

    fun onTagsActivityResult(data: Intent) {
        val tags = mutableMapOf(
            "art" to false,
            "kids" to false,
            "poverty" to false,
            "science&research" to false,
            "healthcare" to false,
            "education" to false
        )
        tags["art"] = data.getBooleanExtra("art", false)
        tags["kids"] = data.getBooleanExtra("kids", false)
        tags["poverty"] = data.getBooleanExtra("poverty", false)
        tags["science&research"] = data.getBooleanExtra("science&research", false)
        tags["healthcare"] = data.getBooleanExtra("healthcare", false)
        tags["education"] = data.getBooleanExtra("education", false)
        putTags(tags)
    }
}