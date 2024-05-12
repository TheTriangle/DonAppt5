package com.example.donappt5.views.charitylist

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.AdapterView.OnItemClickListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.donappt5.R
import com.example.donappt5.views.adapters.CharitiesAdapter
import com.example.donappt5.databinding.FragmentCharityListBinding
import com.example.donappt5.data.model.Charity
import com.example.donappt5.data.model.Charity.Companion.putCharityExtra
import com.example.donappt5.data.util.Status
import com.example.donappt5.data.util.Util
import com.example.donappt5.viewmodels.CharityListViewModel
import com.example.donappt5.views.charitydescription.CharityActivity

class CharityListFragment(var fillingmode: Int = Util.FILLING_ALPHABET) : Fragment() {
    private lateinit var binding: FragmentCharityListBinding
    val viewModel: CharityListViewModel by activityViewModels()
    private lateinit var adapter: CharitiesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCharityListBinding.inflate(inflater, container, false)
        val view = binding.root
        Log.d("fillingmode", (fillingmode == Util.FILLING_FAVORITES).toString())
        viewModel.fillingmode = fillingmode
        setupObserver()
        setupView()
        viewModel.fillData()
        return view
    }

    private fun setupObserver() {
        viewModel.chars.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    Log.d("charityvm", "obsize: ${viewModel.chars.value?.data?.size}")
                    it.data?.let { it1 -> renderList(it1) }
                }
                Status.LOADING -> {
                    // Handle Loading
                }
                Status.ERROR -> {
                    // Handle Error
                }
            }
        }
    }

    fun setupView() {
        adapter = CharitiesAdapter(
            context,
            arrayListOf()
        )
        binding.apply {
            lvMain.isClickable = true
            lvMain.adapter = adapter
            lvMain.setOnScrollListener(object : AbsListView.OnScrollListener {
                override fun onScrollStateChanged(view: AbsListView, scrollState: Int) {}
                override fun onScroll(
                    view: AbsListView,
                    firstVisibleItem: Int,
                    visibleItemCount: Int,
                    totalItemCount: Int
                ) {
                    onMyScroll(view, firstVisibleItem, visibleItemCount, totalItemCount)

                    // Refresh only when scrolled to the top
                    val refreshLayout = activity?.findViewById<SwipeRefreshLayout>(R.id.pullToRefresh)
                    val topRowVerticalPosition =
                        if (view.childCount == 0) 0 else lvMain.getChildAt(
                            0
                        ).getTop()
                    if (refreshLayout != null) {
                        refreshLayout.isEnabled = topRowVerticalPosition == 0
                    }
                }
            })

            lvMain.onItemClickListener =
                OnItemClickListener { parent, view, position, id ->
                    val clickedCharity: Charity = adapter.getCharity(position)
                        ?: return@OnItemClickListener
                    Log.d(
                        "Click", "itemClick: position = " + position + ", id = "
                                + id + ", name = " + clickedCharity.name + "url = " + clickedCharity.photourl + ", payment url = " + clickedCharity.paymentUrl
                    )
                    val intent = Intent(context, CharityActivity::class.java)
                    intent.putCharityExtra(clickedCharity)
                    startActivity(intent)
                }
        }
    }

    fun onMyScroll(
        lw: AbsListView, firstVisibleItem: Int,
        visibleItemCount: Int, totalItemCount: Int
    ) {
        if (fillingmode == Util.FILLING_FAVORITES) return
        when (lw.id) {
            R.id.lvMain -> {
                val lastItem = firstVisibleItem + visibleItemCount
                if (lastItem == totalItemCount) {
                    if (viewModel.preLast != lastItem) {
                        //to avoid multiple calls for last item
                        Log.d("Last", "Last")
                        viewModel.preLast = lastItem
                        viewModel.fillData()
                    }
                }
            }
        }
    }

    private fun renderList(projects: List<Charity>) {
        adapter.clear()
        adapter.addData(projects)
        adapter.notifyDataSetChanged()
    }

    companion object {
        @JvmStatic
        fun newInstance(gfillingmode: Int) =
            CharityListFragment(gfillingmode).apply {}
    }
}