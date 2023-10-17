package com.example.donappt5.views.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.donappt5.R
import com.example.donappt5.data.model.Charity
import com.example.donappt5.data.model.Donation
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Locale
import kotlin.math.roundToInt

class DonationsAdapter(var ctx: Context?) : BaseAdapter() {
    var objects: ArrayList<Donation> = arrayListOf()
    var lInflater: LayoutInflater = ctx
        ?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    override fun getCount(): Int {
        return objects.size
    }

    // элемент по позиции
    override fun getItem(position: Int): Any {
        return objects[position]
    }

    // id по позиции
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val donation = getDonation(position)
        var view = convertView
        if (view == null) {
            view = lInflater.inflate(R.layout.item_donation, parent, false)
        }

        (view?.findViewById<View>(R.id.tvOrganisationName) as TextView).text =
            donation?.charityName

        (view.findViewById<View>(R.id.tvCampaignName) as TextView).text =
            donation?.campaignName

        if (donation?.campaignId == null) {
            (view.findViewById<View>(R.id.tvProgress) as TextView).text =
                ""
        }

        val formatter = SimpleDateFormat("dd.MM.yyyy", Locale.US)
        (view.findViewById<View>(R.id.tvDate) as TextView).text =
            donation?.time?.let { formatter.format(it) }

        (view.findViewById<View>(R.id.tvSum) as TextView).text =
            String.format("%.2f", donation?.amount) + donation?.currency

        //TODO load campaign progress if campaignName != null
        return view
    }

    fun getDonation(position: Int): Donation? {
        if (position >= count) return null
        return getItem(position) as Donation
    }
}