package com.example.donappt5.data.model

import android.content.Intent
import android.util.Log
import com.example.donappt5.R
import com.example.donappt5.data.util.Util
import com.google.firebase.firestore.DocumentSnapshot

class Charity {
    var image: Int = R.drawable.ic_launcher_foreground

    var id: Int = 0

    var trust: Float = 0.0f

    lateinit var firestoreID: String
    lateinit var name: String //TODO change to setters and getters
    lateinit var briefDescription: String
    lateinit var fullDescription: String
    lateinit var photourl: String
    lateinit var paymentUrl: String
    lateinit var tags: ArrayList<String>
    lateinit var contacts: ArrayList<Map<String, String>>

    constructor(
        gfirestoreID: String?,
        gname: String?,
        gbdesc: String?,
        gfdesc: String?,
        gtrust: Float,
        gim: Int,
        gid: Int,
        gtags: ArrayList<String>,
        gcontacts: ArrayList<Map<String, String>>,
        gphotourl: String?
    ) {
        firestoreID = gfirestoreID?: ""
        name = gname?: ""
        briefDescription = gbdesc?: ""
        fullDescription = gfdesc?: ""
        trust = gtrust
        image = gim
        id = gid
        photourl = gphotourl?: ""
        paymentUrl = ""
        tags = gtags
        contacts = gcontacts
    }

    constructor(
        gfirestoreID: String?,
        gname: String?,
        gbdesc: String?,
        gfdesc: String?,
        gtrust: Float,
        gim: Int,
        gid: Int,
        gtags: ArrayList<String>,
        gcontacts: ArrayList<Map<String, String>>?,
        gphotourl: String?,
        paymentUrl: String?
    ) {
        firestoreID = gfirestoreID?: ""
        name = gname?: ""
        briefDescription = gbdesc?: ""
        fullDescription = gfdesc?: ""
        trust = gtrust
        image = gim
        id = gid
        photourl = gphotourl?: ""
        tags = gtags
        contacts = gcontacts?: arrayListOf()
        this.paymentUrl = paymentUrl?: ""
    }

    constructor() {
        name = "enter charity name here"
        briefDescription = "enter your charity description here"
        fullDescription = "enter your charity description here, enter qiwi url on the page to the right"
        trust = -1f
        id = -2
        paymentUrl = ""
        photourl = ""
        tags = arrayListOf()
        contacts = arrayListOf()
    }

    constructor(document: DocumentSnapshot?) {
        if (document == null) {
            return
        }
        firestoreID = document.id
        name = document.getString("name")?: "No name"
        fullDescription = document.getString("description")?: "No description"
        briefDescription = fullDescription.substring(0, Math.min(fullDescription.length, 50))
        photourl = document.getString("photourl")?: ""
        paymentUrl = document.getString("qiwiurl")?: ""
        image = R.drawable.ic_launcher_foreground
        trust = -1f
        id = -2
        tags = arrayListOf()
        contacts = arrayListOf()
        document.get("contacts")?.let {
            for (contactEntry in (document.get("contacts") as List<*>)?: arrayListOf<Map<String, String>>()) {
                contacts.add((contactEntry as Map<*, *>).map { e-> e.key.toString() to e.value.toString()}.toMap())
            }
        }
        for (tagEntry in (document.get("tags") as List<*>?)?: arrayListOf<String>()) {
            tags.add((tagEntry as Map<*, *>)["id"].toString())
        }

    }

    companion object {
        fun DocumentSnapshot.toCharity(): Charity? {
            try {
                return Charity(this)
            } catch (e: Exception) {
                Log.e(TAG, "Error converting charity", e)
                return null
            }
        }

        fun Intent.putCharityExtra(charity: Charity) {
            this.putExtra("firestoreID", charity.firestoreID)
            this.putExtra("chname", charity.name)
            this.putExtra("bdesc", charity.briefDescription)
            this.putExtra("fdesc", charity.fullDescription)
            this.putExtra("trust", charity.trust)
            this.putExtra("image", charity.image)
            this.putExtra("id", charity.id)
            this.putExtra("url", charity.photourl)
            this.putExtra("qiwiPaymentUrl", charity.paymentUrl)
            this.putExtra("tags", charity.tags)
            this.putExtra("contacts", charity.contacts)
        }

        fun Intent.getCharityExtra(): Charity {
            return Charity(
                this.getStringExtra("firestoreID"),
                this.getStringExtra("chname"),
                this.getStringExtra("bdesc"),
                this.getStringExtra("fdesc"),
                this.getFloatExtra("trust", 0f),
                this.getIntExtra("image", 0),
                this.getIntExtra("id", -1),
                this.getStringArrayListExtra("tags")?: arrayListOf(),
                Util.getSerializable(
                    this,
                    "contacts",
                    arrayListOf<Map<String, String>>()::class.java
                ),
                this.getStringExtra("url"),
                this.getStringExtra("qiwiPaymentUrl")
            )
        }

        private const val TAG = "Charity"
    }
}