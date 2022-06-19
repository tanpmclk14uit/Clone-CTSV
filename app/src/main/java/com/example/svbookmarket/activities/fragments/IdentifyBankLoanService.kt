package com.example.svbookmarket.activities.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import com.example.svbookmarket.R
import com.example.svbookmarket.activities.common.Constants
import com.example.svbookmarket.activities.viewmodel.RegisterNewOnlineServiceOrderViewModel
import com.example.svbookmarket.databinding.FragmentIdentifyBankLoanServiceBinding

class IdentifyBankLoanService : Fragment() {

    private lateinit var binding: FragmentIdentifyBankLoanServiceBinding
    private val viewModel: RegisterNewOnlineServiceOrderViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_identify_bank_loan_service, container, false)
        onRadioTuitionKindChange()
        onRadioFamilyKindChange()
        return binding.root
    }
    private fun onRadioTuitionKindChange(){
        binding.tuitionKind.setOnCheckedChangeListener { _, checkedId ->
            val checkedButton = view?.findViewById<RadioButton>(checkedId)
            val checkedButtonText = checkedButton?.text.toString()
            viewModel.tuitionKind = checkedButtonText
        }
    }
    private fun onRadioFamilyKindChange(){
        binding.familyKind.setOnCheckedChangeListener { _, checkedId ->
            val checkedButton = view?.findViewById<RadioButton>(checkedId)
            val checkedButtonText = checkedButton?.text.toString()
            viewModel.familyKind = checkedButtonText
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.kind = Constants.OrderKind.GXNVVNH
    }

    override fun onPause() {
        super.onPause()
        viewModel.tuitionKind = ""
        viewModel.familyKind = ""
    }
}