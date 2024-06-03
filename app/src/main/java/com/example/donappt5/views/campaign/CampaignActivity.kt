package com.example.donappt5.views.campaign

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import com.example.donappt5.R
import com.example.donappt5.data.model.Campaign
import com.example.donappt5.data.model.Campaign.Companion.getCampaignExtra
import com.example.donappt5.data.model.Post
import com.example.donappt5.data.model.Post.Companion.putPostExtra
import com.example.donappt5.data.model.Post.Companion.toPost
import com.example.donappt5.data.services.FirestoreService
import com.example.donappt5.viewmodels.CharityViewModel
import com.example.donappt5.views.CustomTheme
import com.example.donappt5.views.QiwiPaymentActivity
import com.example.donappt5.views.campaign.components.CampaignTitleCard
import com.example.donappt5.views.campaign.components.PostItem
import com.example.donappt5.views.charitydescription.popups.NoPaymentFragment


class CampaignActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var viewModel: CharityViewModel = ViewModelProvider(this)[CharityViewModel::class.java]
        setContent {
            val charityName = intent.getStringExtra("charityname")?: ""
            val campaign = intent.getCampaignExtra()
            CampaignPage(charityName, campaign) {
                viewModel.logAnalytics()
                if (!campaign.yoomoney.isNullOrEmpty()) {
                    val browserIntent =
                        Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://yoomoney.ru/quickpay/confirm" +
                                    "?receiver=${campaign.yoomoney}" +
                                    "&quickpay-form=button" +
                                    "&paymentType=AC" +
                                    "&sum=100"))
                    startActivity(browserIntent)
                } else {
                    viewModel.getCharity().value?.data?.let {
                        NoPaymentFragment(it.firestoreID, it.name, null, null)
                            .show(supportFragmentManager, "NoPaymentSpecified")
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Preview
@Composable
fun CampaignPage(charityName: String = "Регион заботы", campaign: Campaign = Campaign(),
                 onDonateClick: () -> Unit = {}) {
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
        Box(Modifier.fillMaxSize()) {
            LazyColumn(
                Modifier
                    .background(CustomTheme.colors.background)
                    .padding(10.dp)) {
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
            FloatingActionButton(
                onClick = {
                    onDonateClick.invoke()
                },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .background(
                        color = CustomTheme.colors.primary,
                        shape = RoundedCornerShape(10.dp)
                    ),
                containerColor = CustomTheme.colors.primary,
            ) {
                Text(stringResource(id = R.string.donate_button),
                    style = CustomTheme.typography.body, color = CustomTheme.colors.onPrimary)
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
