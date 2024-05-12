package com.example.donappt5.views.campaign.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.donappt5.R
import com.example.donappt5.data.model.Comment
import com.example.donappt5.data.model.User
import com.example.donappt5.data.model.User.Companion.toUser
import com.example.donappt5.data.services.FirestoreService
import com.example.donappt5.views.CustomTheme
import java.time.LocalDateTime

@Preview
@Composable
fun CommentItem(comment: Comment = Comment("", "", LocalDateTime.MIN,
    false, "", "")) {
    val user = remember {
        mutableStateOf<User?>(null)
    }

    FirestoreService.getUser(comment.userId).addOnSuccessListener {
        user.value = it.toUser()
    }
    Row(modifier = Modifier
        .background(
            color = CustomTheme.colors.surface,
            shape = RoundedCornerShape(10.dp)
        )
        .wrapContentHeight()
        .fillMaxWidth()
        .shadow(CustomTheme.elevation.default)
        .padding(15.dp)) {
        if (!comment.organization) {
            AsyncImage(
                placeholder = painterResource(id =
                R.drawable.com_facebook_profile_picture_blank_square),
                model = ImageRequest.Builder(LocalContext.current).data(user.value?.photoUrl)
                        .build(),
                contentScale = ContentScale.FillHeight,
                modifier = Modifier
                    .height(64.dp)
                    .width(64.dp)
                    .clip(CircleShape),
                contentDescription = "${user.value?.username?: "Someone"}'s profile picture"
            )
        }
        Spacer(Modifier.width(5.dp))
        Column {
            if (!comment.organization) {
                Text(
                    text = user.value?.username ?: "Anonymous", style = CustomTheme.typography.body,
                    fontWeight = FontWeight.ExtraBold
                )
            } else {
                Text(
                    text = comment.userName, style = CustomTheme.typography.body,
                    fontWeight = FontWeight.ExtraBold
                )
            }

            Text(modifier = Modifier.fillMaxWidth(), text = comment.text, style = CustomTheme.typography.body)
        }
    }
}