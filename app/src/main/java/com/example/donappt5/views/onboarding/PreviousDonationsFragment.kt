package com.example.donappt5.views.onboarding

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TableRow
import androidx.fragment.app.Fragment
import com.example.donappt5.R
import com.example.donappt5.databinding.FragmentPreviousDonationsBinding
import com.example.donappt5.data.model.OnBoardingDonationRecord

private const val PREVIOUS_DATA = "prevData"
private const val GUIDE = "guide"

class PreviousDonationsFragment : Fragment() {
    private var prevData: ArrayList<OnBoardingDonationRecord>? = null
    private var guide: String? = null
    private lateinit var binding: FragmentPreviousDonationsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            prevData = it.getSerializable(PREVIOUS_DATA) as ArrayList<OnBoardingDonationRecord>?
            guide = it.getString(GUIDE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPreviousDonationsBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onResume() {
        super.onResume()
        setupView()
    }

    override fun onPause() {
        super.onPause()
        prevData = arrayListOf()
        for (i in 0 until binding.tlOnboarding.childCount) {
            val row = binding.tlOnboarding.getChildAt(i) as TableRow
            val etCharityName = row.getChildAt(0) as EditText
            val etDonation = row.getChildAt(1) as EditText
            if (etCharityName.text.toString() != "") {
                prevData!!.add(OnBoardingDonationRecord(etCharityName.text.toString(), etDonation.text.toString().toIntOrNull()))
            }
        }
    }

    private fun setupView() {
        binding.apply {
            btnAddRow.setOnClickListener {
                addRow()
            }
            tvGuide.text = guide
            binding.tlOnboarding.removeAllViews()
        }

        if (prevData != null) {
            for (i in prevData!!) {
                addRow(i.charityName, i.monthlyDonation)
            }
        }
    }

    public fun addRow(name: String = "", donation: Int? = null) {
        val inflater = LayoutInflater.from(context)
        val tr = inflater.inflate(R.layout.onboarding_table_row, null) as TableRow
        val etName = tr.findViewById<EditText>(R.id.etCharityName)
        etName.setText(name)
        val etDonation  = tr.findViewById<EditText>(R.id.etDonation)
        if (donation != null) {
            etDonation.setText(Integer.toString(donation))
        }
        val btnDeleteRow = tr.findViewById<Button>(R.id.btnDeleteRow)
        btnDeleteRow.setOnClickListener {
            AlertDialog.Builder(context)
                .setTitle("Delete record")
                .setMessage("Do you really want to delete thish record?")
                .setPositiveButton("yes", DialogInterface.OnClickListener { dialog, which ->
                    prevData!!.removeAt(binding.tlOnboarding.indexOfChild(tr))
                    binding.tlOnboarding.removeView(tr)
                    binding.tlOnboarding.invalidate()
                })
                .setNegativeButton("no", null)
                .show()
        }
        binding.tlOnboarding.addView(tr) //добавляем созданную строку в таблицу
    }


    public fun addData(data: ArrayList<OnBoardingDonationRecord>) {
        for (i in data) {
            addRow(i.charityName, i.monthlyDonation)
        }
    }

    public fun getData(): MutableList<OnBoardingDonationRecord> {
        val res = mutableListOf<OnBoardingDonationRecord>()
        for (i in 0 until binding.tlOnboarding.childCount) {
            val row = binding.tlOnboarding.getChildAt(i) as TableRow
            val etCharityName = row.getChildAt(0) as EditText
            val etDonation = row.getChildAt(1) as EditText
            if (etCharityName.text.toString() != "") {
                res.add(OnBoardingDonationRecord(etCharityName.text.toString(), etDonation.text.toString().toIntOrNull()))
            }
        }
        return res
    }

    companion object {
        @JvmStatic
        fun newInstance(guide: String, prevData: ArrayList<OnBoardingDonationRecord>? = null) =
            PreviousDonationsFragment().apply {
                arguments = Bundle().apply {
                    putString(GUIDE, guide)
                    putSerializable(PREVIOUS_DATA, prevData)
                }
            }
    }
}
