package com.example.donappt5.views.charitydescription.popups

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.donappt5.R
import com.example.donappt5.data.services.FirestoreService
import com.example.donappt5.databinding.PopupNoPaymentMethodBinding
import java.util.Date

class NoPaymentFragment(val charityId: String, val charityName: String, val campaignId: String?,
                        val campaignName: String?) : DialogFragment() {

    lateinit var binding: PopupNoPaymentMethodBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = PopupNoPaymentMethodBinding.inflate(inflater)
        binding.tvDonatedMessage.text = getString(R.string.already_donated_message, charityName)
        binding.btnConfirmPayment.setOnClickListener {
            FirestoreService.addDonationToHistory(binding.etDonationAmount.text.toString().toDouble(),
                binding.tvCurrency.text.toString(), Date(), charityId, charityName, campaignId, campaignName
            )
            binding.tvDonatedMessage.text = getString(R.string.thank_you_save_donation)
            binding.etDonationAmount.setText("")
        }
        return binding.root
    }
}