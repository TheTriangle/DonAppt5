package com.example.donappt5.data.model

import android.content.Intent
import com.example.donappt5.data.model.Charity.Companion.getCharityExtra
import com.example.donappt5.data.util.Util
import com.example.donappt5.data.util.Util.toLocalDateTime
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import com.google.type.DateTime
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Date

class Post {
    var id: String
    var publicationDate: LocalDateTime
    var fullText: String
    var header: String
    var images: ArrayList<String>
    var documents: ArrayList<String>
    var likes: Int = 0
    var pinned: Boolean = false
    var isFinishPost: Boolean = false
    var commentsCount: Int = 0

    constructor(gid: String?,
                gpublicationDate: LocalDateTime?,
                gfullText: String?,
                gheader: String?,
                gimages: ArrayList<String>?,
                gdocuments: ArrayList<String>?,
                glikes: Int?,
                gpinned: Boolean?,
                gfinish: Boolean?,
                gcommentsCount: Int?) {
        id = gid?: ""
        publicationDate = gpublicationDate?: LocalDateTime.MIN
        fullText = gfullText?: ""
        header = gheader?: ""
        images = gimages?: arrayListOf()
        documents = gdocuments?: arrayListOf()
        likes = glikes?: 0
        pinned = gpinned?: false
        isFinishPost = gfinish?: false
        commentsCount = gcommentsCount?: 0
    }

    constructor(document: DocumentSnapshot) {
        publicationDate = document.getTimestamp("date")?.toLocalDateTime()?: LocalDateTime.now()
        fullText = document.getString("fulltext")?: ""
        header = document.getString("header")?: ""
        images = arrayListOf()
        for (imageUrl in document.get("images") as List<*>) {
            images.add(imageUrl as String)
        }
        documents = arrayListOf()
        for (docUrl in document.get("documents") as List<*>) {
            documents.add(docUrl as String)
        }
        likes = document.getLong("likes")?.toInt()?: 0
        pinned = document.getBoolean("pinned") == true
        isFinishPost = document.getBoolean("finish") == true
        commentsCount = document.getLong("commentsCount")?.toInt()?: 0
        id = document.id
    }

    companion object {
        fun DocumentSnapshot.toPost(): Post {
            return Post(this)
        }

        fun Intent.putPostExtra(post: Post) {
            this.putExtra("id", post.id)
            this.putExtra("publicationDate", post.publicationDate.toString())
            this.putExtra("fullText", post.fullText)
            this.putExtra("header", post.header)
            this.putExtra("images", post.images)
            this.putExtra("documents", post.documents)
            this.putExtra("likes", post.likes)
            this.putExtra("pinned", post.pinned)
            this.putExtra("isFinishPost", post.isFinishPost)
            this.putExtra("commentsCount", post.commentsCount)
        }

        fun Intent.getPostExtra(): Post {
            return Post(
                this.getStringExtra("id"),
                LocalDateTime.parse(this.getStringExtra("publicationDate")),
                this.getStringExtra("fullText"),
                this.getStringExtra("header"),
                Util.getSerializable(
                    this,
                    "images",
                    arrayListOf<String>()::class.java
                ),
                Util.getSerializable(
                    this,
                    "documents",
                    arrayListOf<String>()::class.java
                ),
                this.getIntExtra("likes", 0),
                this.getBooleanExtra("pinned", false),
                this.getBooleanExtra("isFinishPost", false),
                this.getIntExtra("commentsCount", 0)
            )
        }
    }
}