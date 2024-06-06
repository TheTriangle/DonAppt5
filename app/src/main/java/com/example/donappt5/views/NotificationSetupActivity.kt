package com.example.donappt5.views

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.AbsListView
import android.widget.AdapterView.OnItemClickListener
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.donappt5.R
import com.example.donappt5.data.model.Campaign
import com.example.donappt5.data.model.Campaign.Companion.toCampaign
import com.example.donappt5.views.adapters.CharitiesAdapter
import com.example.donappt5.databinding.ActivityOwnedCharityListBinding
import com.example.donappt5.data.model.Charity
import com.example.donappt5.data.model.Charity.Companion.putCharityExtra
import com.example.donappt5.data.model.Comment
import com.example.donappt5.data.model.NotificationSource
import com.example.donappt5.data.model.NotificationSource.Companion.toNotificationSource
import com.example.donappt5.data.services.FirestoreService
import com.example.donappt5.data.util.MyGlobals
import com.example.donappt5.data.util.Status
import com.example.donappt5.databinding.ActivityCharitydescBinding
import com.example.donappt5.databinding.ActivityNotificationsBinding
import com.example.donappt5.views.charitydescription.components.CampaignListItem
import com.google.android.material.bottomnavigation.BottomNavigationView

class NotificationSetupActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNotificationsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotificationsBinding.inflate(layoutInflater)
        val myGlobals = MyGlobals(this)
        myGlobals.setupBottomNavigation(this, this, binding.bottomNavigation)
        binding.setupPage.apply {
            setContent {
                NotificationSetupPage()
            }
        }
        setContentView(binding.root)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Preview
@Composable
fun NotificationSetupPage() {
    var newPublications by remember {
        mutableStateOf(true)
    }
    var goalsAchieved by remember {
        mutableStateOf(true)
    }
    var urgentMessages by remember {
        mutableStateOf(true)
    }
    var campaignOver by remember {
        mutableStateOf(true)
    }
    var subscriptions by remember {
        mutableStateOf(arrayListOf<NotificationSource>())
    }

    FirestoreService.getSubscriptions().addOnCompleteListener {
        for (doc in it.result) {
            val loadedSubscriptions = arrayListOf<NotificationSource>()
            doc.toNotificationSource()?.let { subscription -> loadedSubscriptions.add(subscription) }
            subscriptions = loadedSubscriptions
        }
    }


    CustomTheme {
        LazyColumn {
            stickyHeader {
                Text(style = CustomTheme.typography.body, color = CustomTheme.colors.primary,
                     text = stringResource(id = R.string.notify_about_text),
                     fontWeight = FontWeight.ExtraBold)

                CheckItem(newPublications, stringResource(id = R.string.new_publications)) {
                    newPublications = it
                }
                Spacer(Modifier.height(5.dp))

                CheckItem(goalsAchieved, stringResource(id = R.string.acheiveid_goals)) {
                    goalsAchieved = it
                }
                Spacer(Modifier.height(5.dp))

                CheckItem(urgentMessages, stringResource(id = R.string.urgent_messages)) {
                    urgentMessages = it
                }
                Spacer(Modifier.height(5.dp))

                CheckItem(campaignOver, stringResource(id = R.string.campaign_closures)) {
                    campaignOver = it
                }

                Spacer(Modifier.height(30.dp))
                Text(style = CustomTheme.typography.body, color = CustomTheme.colors.primary,
                    text = stringResource(id = R.string.allow_from_text),
                    fontWeight = FontWeight.ExtraBold)
            }
            items(subscriptions) { subscription ->
                CheckItem(subscription.subscribed.value, subscription.sourceName) {
                    if (it) {
                        subscription.subscribe()
                    } else {
                        subscription.unsubscribe()
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun CheckItem(checked: Boolean = true, text: String = "source",
              onCheckedChange: (checkedState: Boolean) -> Unit = {}) {
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
            .clickable {
                onCheckedChange(!checked)
            }
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(
                    R.drawable.checkmark,
                ),
                contentDescription = "checkmark",
                //tint = CustomTheme.colors.primary,
                modifier = Modifier
                    .width(20.dp)
                    .height(20.dp)
                    .background(CustomTheme.colors.surface),
                alpha = if(checked) 1.0f else 0.35f
            )
            Spacer(Modifier.width(10.dp))
            Text(text, style = CustomTheme.typography.body)
        }
    }
}