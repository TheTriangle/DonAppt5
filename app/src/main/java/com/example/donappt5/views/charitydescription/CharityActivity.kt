package com.example.donappt5.views.charitydescription

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.PagerAdapter
import com.example.donappt5.R
import com.example.donappt5.data.model.Charity
import com.example.donappt5.data.services.FirestoreService
import com.example.donappt5.data.util.MyGlobals
import com.example.donappt5.data.util.Status
import com.example.donappt5.data.util.TagConverter
import com.example.donappt5.data.util.Util.getSerializable
import com.example.donappt5.databinding.ActivityCharitydescBinding
import com.example.donappt5.viewmodels.CharityViewModel
import com.example.donappt5.views.QiwiPaymentActivity
import com.example.donappt5.views.charitydescription.components.CampaignsList
import com.example.donappt5.views.charitydescription.popups.NoPaymentFragment
import com.example.donappt5.views.charitydescription.components.CharityContactsCard
import com.example.donappt5.views.charitydescription.components.TagList
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.squareup.picasso.Picasso

class CharityActivity : AppCompatActivity() {
    lateinit var ctx: Context
    var pagerAdapter: PagerAdapter? = null
    var myGlobals: MyGlobals? = null
    var bottomNavigationView: BottomNavigationView? = null
    lateinit var viewModel: CharityViewModel
    private lateinit var binding: ActivityCharitydescBinding
    var isSubscribedTo = false

    public override fun onCreate(savedInstanceState: Bundle?) {
        ctx = this
        super.onCreate(savedInstanceState)
        binding = ActivityCharitydescBinding.inflate(layoutInflater)

        val view = binding.root
        viewModel = ViewModelProvider(this)[CharityViewModel::class.java]
        viewModel.setCharity(
            Charity(
                intent.getStringExtra("firestoreID"),
                intent.getStringExtra("chname"),
                intent.getStringExtra("bdesc"),
                intent.getStringExtra("fdesc"),
                intent.getFloatExtra("trust", 0f),
                intent.getIntExtra("image", 0),
                intent.getIntExtra("id", 0),
                intent.getStringArrayListExtra("tags")?: arrayListOf(),
                getSerializable(intent, "contacts", arrayListOf<Map<String, String>>()::class.java),
                intent.getStringExtra("url"),
                intent.getStringExtra("qiwiPaymentUrl")
            )
        )

        myGlobals = MyGlobals(ctx)
        bottomNavigationView = binding.bottomNavigation
        myGlobals!!.setupBottomNavigation(ctx, this, bottomNavigationView!!)

        setupView()
        setupObserver()
        setContentView(view)
    }

    private fun setupObserver() {
        TagConverter.init(this)
        viewModel.getCharity().observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    binding.apply {
                        titleCard.binding.tvName.text = it.data?.name
                        titleCard.binding.tvDescription.text = it.data?.fullDescription
                        titleCard.binding.tagList.apply {
                            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
                            setContent {
                                MaterialTheme {
                                    TagList(it.data?.tags?: arrayListOf())
                                }
                            }
                        }
                        it.data?.firestoreID?.let { charityId ->
                            FirestoreService.getIsUserSubscribedTo(
                                charityId
                            ).addOnSuccessListener {
                                if (!it.isEmpty) {
                                    isSubscribedTo = true

                                }
                            }
                        }
                        titleCard.binding.ivBell.setOnClickListener { view ->
                            if (isSubscribedTo) {
                                titleCard.binding.ivBell.setImageResource(R.drawable.bell_outline)
                                FirestoreService.unsubscribeFrom(it.data?.firestoreID)
                            } else {
                                titleCard.binding.ivBell.setImageResource(R.drawable.bell_filled)
                                FirestoreService.subscribeTo(it.data?.firestoreID, it.data?.name?: "")
                            }
                            isSubscribedTo = !isSubscribedTo
                        }
                        contacts.apply {
                            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
                            setContent {
                                MaterialTheme {
                                    CharityContactsCard(it.data?.contacts?: arrayListOf())
                                }
                            }
                        }
                        campaigns.apply {
                            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
                            setContent {
                                MaterialTheme {
                                    it.data?.firestoreID?.let { id -> CampaignsList(it.data.name, id) }
                                }
                            }
                        }
                    }
                    viewModel.loadFav(it.data!!.firestoreID)
                }

                Status.LOADING -> {
                    // Handle loading
                }

                Status.ERROR -> {
                    // Handle error
                }
            }
        })

        viewModel.isFavourite().observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    binding.apply {
                        if (it.data == true) {
                            //ivFavorite.setImageResource(R.drawable.ic_favorite_on)
                        } else {
                            //ivFavorite.setImageResource(R.drawable.ic_favorite_off)
                        }
                    }
                }

                Status.LOADING -> {
                    // Handle loading
                }

                Status.ERROR -> {
                    // Handle error
                }
            }
        })
    }

    private fun setupView() {
        binding.apply {
            btnDonate.setOnClickListener {
                viewModel.logAnalytics()

                if (viewModel.getCharity().value?.data?.paymentUrl != null && viewModel.getCharity().value?.data?.paymentUrl != "") {
                    val intent1 = Intent(ctx, QiwiPaymentActivity::class.java)
                    intent1.putExtra("firestoreID", viewModel.getCharity().value?.data?.firestoreID)
                    intent1.putExtra("charityname", viewModel.getCharity().value?.data?.name)
                    intent1.putExtra("qiwiPaymentUrl", viewModel.getCharity().value?.data?.paymentUrl)
                    startActivity(intent1)
                } else {
                    viewModel.getCharity().value?.data?.let {
                        NoPaymentFragment(it.firestoreID, it.name, null, null)
                            .show(supportFragmentManager, "NoPaymentSpecified")
                    }
                }
            }
            //ivFavorite.setOnClickListener { viewModel.changeFav() }
        }
    }

    public override fun onResume() {
        super.onResume()
        myGlobals!!.setSelectedItem(this, bottomNavigationView!!)
    }

    private inner class MyPagerAdapter(fm: FragmentManager?) :
        FragmentPagerAdapter(fm!!) {
        override fun getItem(pos: Int): Fragment {
            return when (pos) {
                0 -> CharityDescFragment.newInstance(viewModel.getCharity().value!!.data)
                else -> CharityDescFragment.newInstance(viewModel.getCharity().value!!.data)
            }
        }

        override fun getCount(): Int {
            return 1
        }
    }
}
