package com.example.donappt5.views.campaign

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.donappt5.R
import com.example.donappt5.data.model.Campaign.Companion.getCampaignExtra
import com.example.donappt5.data.model.Comment
import com.example.donappt5.data.model.Comment.Companion.toCommment
import com.example.donappt5.data.model.Post
import com.example.donappt5.data.model.Post.Companion.getPostExtra
import com.example.donappt5.data.model.Post.Companion.toPost
import com.example.donappt5.data.services.FirestoreService
import com.example.donappt5.views.CustomTheme
import com.example.donappt5.views.campaign.components.CommentItem
import com.example.donappt5.views.campaign.components.PostItem
import java.time.LocalDateTime

class PostActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val campaignId = intent.getStringExtra("campaignid")?: ""
            val liked = intent.getBooleanExtra("liked", false)
            val post = intent.getPostExtra()
            PostPage(campaignId, post, liked)
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Preview
@Composable
fun PostPage(campaignId: String = "",
             post: Post = Post("", LocalDateTime.MIN, "", "", arrayListOf(),
                 arrayListOf(), 0, false, false, 0),
             liked: Boolean = false) {
    var comments by remember {
        mutableStateOf(arrayListOf<Comment>())
    }
    FirestoreService.getComments(campaignId, post.id).addOnSuccessListener {
        val loadedComments = arrayListOf<Comment>()
        for (doc in it) {
            doc.toCommment().let { comment ->
                if (comment != null) {
                    loadedComments.add(comment)
                }
            }
        }
        comments = loadedComments
    }

    CustomTheme {
        Column(Modifier.scrollable(rememberScrollableState { 0f }, Orientation.Vertical)) {
            PostItem(Modifier, post, liked, campaignId)
            var userInput by remember { mutableStateOf("") }
            LazyColumn(
                Modifier
                    .background(CustomTheme.colors.background)
                    .padding(10.dp)
                    .fillMaxWidth()) {
                stickyHeader {
                    Box(
                        contentAlignment = Alignment.CenterStart,
                        modifier = Modifier
                            .background(
                                color = CustomTheme.colors.surface,
                                shape = RoundedCornerShape(10.dp)
                            )
                            .wrapContentHeight()
                            .fillMaxWidth()
                            .shadow(CustomTheme.elevation.default)
                            .padding(15.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            TextField(value = userInput, onValueChange = { userInput = it })
                            Icon(
                                painter = painterResource(
                                    R.drawable.send
                                ),
                                contentDescription = "send the comment",
                                tint = CustomTheme.colors.primary,
                                modifier = Modifier
                                    .clickable {
                                        FirestoreService
                                            .sendComment(campaignId, post.id, userInput)
                                            .addOnSuccessListener {
                                                it
                                                    .get()
                                                    .addOnSuccessListener { doc ->
                                                        comments += Comment(doc)
                                                        userInput = ""
                                                    }
                                            }
                                    }
                                    .width(35.dp)
                                    .height(35.dp)
                                    .background(CustomTheme.colors.surface)
                            )
                        }
                    }
                }
                items(comments) {
                    CommentItem(it)
                }
            }
        }
    }
}
