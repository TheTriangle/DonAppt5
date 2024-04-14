package com.example.donappt5.views.donationslist

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.donappt5.R
import com.example.donappt5.data.model.Donation
import com.example.donappt5.data.util.Status
import com.example.donappt5.databinding.FragmentDonationsListBinding
import com.example.donappt5.viewmodels.DonationsListViewModel
import com.example.donappt5.views.adapters.DonationsAdapter
import com.example.donappt5.views.charitydescription.CharityActivity
import com.example.donappt5.views.charitylist.CharityListFragment

class DonationsListFragment : Fragment() {
    private lateinit var binding: FragmentDonationsListBinding
    lateinit var adapter: DonationsAdapter
    val viewModel: DonationsListViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDonationsListBinding.inflate(inflater, container, false)
        val view = binding.root
        adapter = DonationsAdapter(requireActivity())
        setupView()
        setupObserver()
        viewModel.fillData()
        return view
    }

    private fun setupObserver() {
        viewModel.donations.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.let { it1 -> renderList(it1) }
                }
                Status.LOADING -> {
                }
                Status.ERROR -> {
                }
            }
        }
    }

    private fun renderList(donations: List<Donation>) {
        adapter.objects.addAll(donations)
        adapter.notifyDataSetChanged()
    }

    fun setupView() {
        binding.apply {
            lvDonations.adapter = adapter
            lvDonations.setOnScrollListener(object : AbsListView.OnScrollListener {
                override fun onScrollStateChanged(view: AbsListView, scrollState: Int) {}
                override fun onScroll(
                    view: AbsListView,
                    firstVisibleItem: Int,
                    visibleItemCount: Int,
                    totalItemCount: Int
                ) {
                    viewModel.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount)

                    // Refresh only when scrolled to the top
                    val refreshLayout = activity?.findViewById<SwipeRefreshLayout>(R.id.pullToRefresh)
                    val topRowVerticalPosition =
                        if (view.childCount == 0) 0 else lvDonations.getChildAt(
                            0
                        ).getTop()
                    if (refreshLayout != null) {
                        refreshLayout.isEnabled = topRowVerticalPosition == 0
                    }
                }
            })

            lvDonations.onItemClickListener =
                AdapterView.OnItemClickListener { parent, view, position, id ->
                    val clickedDonatin: Donation = adapter.getDonation(position)
                        ?: return@OnItemClickListener

                    val intent = Intent(context, CharityActivity::class.java)
                    /*intent.putExtra("firestoreID", clickedCharity.firestoreID);
                    intent.putExtra("chname", clickedCharity.name)
                    intent.putExtra("bdesc", clickedCharity.briefDescription)
                    intent.putExtra("fdesc", clickedCharity.fullDescription)
                    intent.putExtra("trust", clickedCharity.trust)
                    intent.putExtra("image", clickedCharity.image)
                    intent.putExtra("id", clickedCharity.id)
                    intent.putExtra("url", clickedCharity.photourl)
                    intent.putExtra("qiwiPaymentUrl", clickedCharity.paymentUrl)
                    intent.putExtra("tags", clickedCharity.tags)
                    startActivity(intent) TODO go to campaign page */
                }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(gfillingmode: Int) =
            CharityListFragment(gfillingmode).apply {}
    }
}