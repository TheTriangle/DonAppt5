package com.example.donappt5.views.campaign

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.donappt5.data.model.Campaign
import com.example.donappt5.data.model.Campaign.Companion.getCampaignExtra
import com.example.donappt5.data.model.Campaign.Companion.putCampaignExtra
import com.example.donappt5.data.model.Campaign.Companion.toCampaign
import com.example.donappt5.data.model.Post
import com.example.donappt5.data.model.Post.Companion.putPostExtra
import com.example.donappt5.data.model.Post.Companion.toPost
import com.example.donappt5.data.services.FirestoreService
import com.example.donappt5.views.CustomTheme
import com.example.donappt5.views.campaign.components.CampaignTitleCard
import com.example.donappt5.views.campaign.components.PostItem

class CampaignActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val charityName = intent.getStringExtra("charityname")?: ""
            val campaign = intent.getCampaignExtra()
            CampaignPage(charityName, campaign)
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Preview
@Composable
fun CampaignPage(charityName: String = "Регион заботы", campaign: Campaign = Campaign()) {
    var posts by remember {
        mutableStateOf(arrayListOf<Post>())
    }
    val ctx = LocalContext.current

    CustomTheme {
        var likedPosts = remember {
            listOf<String?>()
        }
        FirestoreService.getLikesFor(campaign.id).addOnSuccessListener {
            likedPosts = it.map { doc ->
                doc.getString("postid")
            }
        }
        LazyColumn(Modifier.background(CustomTheme.colors.background).padding(10.dp)) {
            stickyHeader {
                CampaignTitleCard(charityName, campaign.name, campaign.totalAmount, campaign.collectedAmount)
            }

            items(posts) {
                PostItem(Modifier.clickable {
                    val intent = Intent(ctx, PostActivity::class.java)
                    intent.putPostExtra(it)
                    intent.putExtra("campaignid", campaign.id)
                    intent.putExtra("liked", likedPosts.contains(it.id))
                    ctx.startActivity(intent) },
                    it, likedPosts.contains(it.id), campaign.id)

            }
        }
    }

    FirestoreService.getPostsFor(campaign.id).addOnCompleteListener {
        val loadedPosts = arrayListOf<Post>()
        for (doc in it.result) {
            doc.toPost().let { post -> loadedPosts.add(post) }
        }
        posts = loadedPosts
    }
}
