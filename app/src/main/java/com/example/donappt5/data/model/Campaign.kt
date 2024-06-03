package com.example.donappt5.data.model

import android.content.Intent
import android.util.Log
import androidx.compose.runtime.MutableState
import com.example.donappt5.data.model.Post.Companion.toPost
import com.example.donappt5.data.services.FirestoreService
import com.google.firebase.firestore.DocumentSnapshot

class Campaign() {
    var totalAmount: Int = 0
    var collectedAmount: Int = 0
    lateinit var name: String
    lateinit var parentCharityId: String
    lateinit var id: String
    var yoomoney: String? = null

    lateinit var posts: MutableState<ArrayList<Post>>

    fun loadPosts() {
        FirestoreService.getPosts(id).addOnSuccessListener { documentSnapshots ->
            val loadedPosts = arrayListOf<Post>()
            for (doc in documentSnapshots) {
                loadedPosts.add(doc.toPost())
            }
            posts.value = loadedPosts
        }
    }

    constructor(gid: String, gtotalAmount: Int, gcollectedAmount: Int, gname: String,
                gparentCharityId: String, gyoomoney: String?) : this() {
        id = gid
        totalAmount = gtotalAmount
        collectedAmount = gcollectedAmount
        name = gname
        parentCharityId = gparentCharityId
        yoomoney = gyoomoney
    }

    constructor(document: DocumentSnapshot) : this() {
        totalAmount = document.getLong("totalamount")?.toInt()?: 0
        collectedAmount = document.getLong("collectedamount")?.toInt()?: 0
        name = document.getString("name")?: "Name not found"
        parentCharityId = document.getString("parentcharity")?: "Parent charity not found"
        id = document.id
        yoomoney = document.getString("yoomoney")
    }

    companion object {
        fun DocumentSnapshot.toCampaign(): Campaign? {
            try {
                return Campaign(this)
            } catch (e: Exception) {
                Log.e(TAG, "Error converting campaign", e)
                return null
            }
        }

        fun Intent.putCampaignExtra(campaign: Campaign) {
            this.putExtra("totalamount", campaign.totalAmount)
            this.putExtra("collectedamount", campaign.collectedAmount)
            this.putExtra("id", campaign.id)
            this.putExtra("parentid", campaign.parentCharityId)
            this.putExtra("name", campaign.name)
            this.putExtra("yoomoney", campaign.yoomoney)
        }

        fun Intent.getCampaignExtra(): Campaign {
            return Campaign(
                this.getStringExtra("id")?: "",
                this.getIntExtra("totalamount", 1),
                this.getIntExtra("collectedamount", 0),
                this.getStringExtra("name")?: "",
                this.getStringExtra("parentid")?: "",
                this.getStringExtra("yoomoney")?: ""
            )
        }

        private const val TAG = "Campaign"
    }
}