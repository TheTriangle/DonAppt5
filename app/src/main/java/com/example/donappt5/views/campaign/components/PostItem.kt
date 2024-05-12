package com.example.donappt5.views.campaign.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.donappt5.R
import com.example.donappt5.data.model.Post
import com.example.donappt5.data.services.FirestoreService
import com.example.donappt5.views.CustomTheme
import java.time.LocalDateTime

@Preview
@Composable
fun PostItem(modifier: Modifier = Modifier, post: Post = Post("", LocalDateTime.now(), "someText", "someHeader",
             arrayListOf(), arrayListOf(), 0, true, true, 0),
             liked: Boolean = false, parentCampaignId: String = "") {
    CustomTheme {
        var rememberedLiked = remember { mutableStateOf(liked) }
        Box(
            contentAlignment = Alignment.CenterStart,
            modifier = modifier
                .background(
                    color = CustomTheme.colors.surface,
                    shape = RoundedCornerShape(10.dp)
                )
                .wrapContentHeight()
                .shadow(CustomTheme.elevation.default)
                .padding(15.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(10.dp),
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Text(post.header, style = CustomTheme.typography.header,
                    fontWeight = FontWeight.ExtraBold)
                Text(post.fullText, style = CustomTheme.typography.body)
                LazyRow(modifier = Modifier
                    .height(400.dp)
                    .padding(10.dp)
                    .fillMaxWidth()) {
                    items(post.images.size) {
                        AsyncImage(
                            //placeholder = painterResource(id = R.drawable.com_facebook_profile_picture_blank_square),
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(post.images[it])
                                .build(),
                            contentScale = ContentScale.FillHeight,
                            modifier = Modifier.height(400.dp),
                            contentDescription = "Image for the post ${post.header}"
                        )

                        if (it != post.images.size - 1) Spacer(modifier = Modifier.width(20.dp))
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(R.drawable.comment),
                        contentDescription = "proceed to comments icon",
                        tint = CustomTheme.colors.primary
                    )
                    Text(post.commentsCount.toString(), style = CustomTheme.typography.body,
                        modifier = Modifier.shadow(CustomTheme.elevation.default))
                    Spacer(Modifier.weight(1f))
                    Icon(
                        painter = painterResource(R.drawable.share),
                        contentDescription = "like the Post",
                        tint = CustomTheme.colors.primary
                    )
                    Icon(
                        painter = painterResource(
                            if (rememberedLiked.value) R.drawable.thumb_up else R.drawable.thumb_up_outline
                        ),
                        contentDescription = "like the Post",
                        tint = CustomTheme.colors.primary,
                        modifier = Modifier.clickable {
                            if (rememberedLiked.value) {
                                post.likes--
                                FirestoreService.dislike(post.id, parentCampaignId)
                            }
                            else {
                                post.likes++
                                FirestoreService.like(post.id, parentCampaignId)
                            }

                            rememberedLiked.value = !rememberedLiked.value
                        }
                    )
                    Text(post.likes.toString(), style = CustomTheme.typography.body,
                        modifier = Modifier.shadow(CustomTheme.elevation.default))
                }
            }
        }
    }
}