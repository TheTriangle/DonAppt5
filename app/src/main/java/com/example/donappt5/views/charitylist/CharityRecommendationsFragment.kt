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
import androidx.lifecycle.lifecycleScope
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.donappt5.R
import com.example.donappt5.data.adapters.CharityAdapter
import com.example.donappt5.databinding.FragmentCharityListBinding
import com.example.donappt5.data.model.Charity
import com.example.donappt5.data.util.ModelConfig
import com.example.donappt5.data.model.RecommendationClient
import com.example.donappt5.views.charitydescription.CharityActivity
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 * Use the [CharityListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CharityRecommendationsFragment : Fragment() {
    private lateinit var binding: FragmentCharityListBinding
    private lateinit var adapter: CharityAdapter
    var chars = ArrayList<Charity>()
    private var config = ModelConfig()
    private lateinit var client: RecommendationClient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d("CharityRecommendationsFragment", "entered")
        // Inflate the layout for this fragment
        binding = FragmentCharityListBinding.inflate(inflater, container, false)
        setupView()
        val view = binding.root
        return view
    }

    private fun setupView() {
        adapter =
            CharityAdapter(context, chars)
        binding.apply {
            lvMain.isClickable = true
            lvMain.adapter = adapter

            lvMain.onItemClickListener =
                OnItemClickListener { parent, view, position, id ->
                    val clickedCharity: Charity = adapter.getCharity(position)
                    Log.d(
                        "Click", "itemClick: position = " + position + ", id = "
                                + id + ", name = " + clickedCharity.name + "url = " + clickedCharity.photourl + ", payment url = " + clickedCharity.paymentUrl
                    )
                    val intent = Intent(context, CharityActivity::class.java)
                    intent.putExtra("firestoreID", clickedCharity.firestoreID)
                    intent.putExtra("chname", clickedCharity.name)
                    intent.putExtra("bdesc", clickedCharity.briefDescription)
                    intent.putExtra("fdesc", clickedCharity.fullDescription)
                    intent.putExtra("trust", clickedCharity.trust)
                    intent.putExtra("image", clickedCharity.image)
                    intent.putExtra("id", clickedCharity.id)
                    intent.putExtra("url", clickedCharity.photourl)
                    intent.putExtra("qiwiPaymentUrl", clickedCharity.paymentUrl)
                    startActivity(intent)
                }
            lvMain.setOnScrollListener(object : AbsListView.OnScrollListener {
                override fun onScrollStateChanged(view: AbsListView, scrollState: Int) {}
                override fun onScroll(
                    view: AbsListView,
                    firstVisibleItem: Int,
                    visibleItemCount: Int,
                    totalItemCount: Int
                ) {
                    val refreshLayout =
                        activity?.findViewById<SwipeRefreshLayout>(R.id.pullToRefresh)
                    refreshLayout?.isEnabled = false

                }
            })
        }
        fillData()
    }

    fun fillData() {
        client = RecommendationClient(requireContext(), config)
        lifecycleScope.launch {
            client.load {
                lifecycleScope.launch {
                    val db = FirebaseFirestore.getInstance()
                    var results = client.recommend()
                    results.forEach {
                        db.collection("charities").document(it.id).get().addOnSuccessListener { doc ->
                            adapter.objects.add(
                                Charity(
                                    doc
                                )
                            )
                            adapter.notifyDataSetChanged()
                        }
                    }
                }
            }
        }
    }

    override fun onStop() {
        lifecycleScope.launch {
            client.unload()
        }
        super.onStop()
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            CharityRecommendationsFragment().apply {
            }
    }
}