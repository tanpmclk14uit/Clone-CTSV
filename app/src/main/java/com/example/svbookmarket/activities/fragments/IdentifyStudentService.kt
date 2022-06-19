package com.example.svbookmarket.activities.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import com.example.svbookmarket.R
import com.example.svbookmarket.activities.common.Constants
import com.example.svbookmarket.activities.viewmodel.RegisterNewOnlineServiceOrderViewModel
import com.example.svbookmarket.databinding.FragmentIdentifyBankLoanServiceBinding
import com.example.svbookmarket.databinding.FragmentIdentifyStudentServiceBinding

class IdentifyStudentService : Fragment() {

    private lateinit var binding: FragmentIdentifyStudentServiceBinding

    private val viewModel: RegisterNewOnlineServiceOrderViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_identify_student_service, container, false)
        onRadioChange()
        onOtherReasonTextChange()
        return binding.root
    }
    private fun onRadioChange(){
        binding.radioChangeReason.setOnCheckedChangeListener { _, checkedId ->
            val checkedButton = view?.findViewById<RadioButton>(checkedId)
            val checkedButtonText = checkedButton?.text.toString()
            if(checkedButtonText == "Khác"){
                // show other reason
                binding.otherReason.visibility = View.VISIBLE
                viewModel.reason = ""
            }else{
                // hind other reason
                binding.otherReason.visibility = View.GONE
                // handle normal reason
                viewModel.reason = checkedButtonText
            }
        }
    }
    private fun onOtherReasonTextChange(){
        binding.otherReason.doAfterTextChanged {
            if(it.isNullOrBlank()){
                viewModel.reason = ""
                binding.otherReason.error = "Vui lòng điền lý do xác nhận!"
            }else{
                viewModel.reason = it.toString().trim()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.kind = Constants.OrderKind.GXNSV
    }

    override fun onPause() {
        super.onPause()
        viewModel.reason = ""
    }

}