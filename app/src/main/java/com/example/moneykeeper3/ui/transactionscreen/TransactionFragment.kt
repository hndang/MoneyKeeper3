package com.example.moneykeeper3.ui.transactionscreen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.moneykeeper3.R
import com.example.moneykeeper3.database.MoneyKeeperApplication
import com.example.moneykeeper3.databinding.FragmentHomeBinding
import com.example.moneykeeper3.databinding.FragmentTransactionBinding
import com.example.moneykeeper3.formatDatePretty
import com.example.moneykeeper3.formatTimePretty
import com.example.moneykeeper3.model.TransactionViewModel
import com.example.moneykeeper3.model.TransactionViewModelFactory
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.time.*
import java.time.format.DateTimeFormatter
import java.util.*


class TransactionFragment : Fragment() {

    private lateinit var binding: FragmentTransactionBinding
    // Shared view model b/w different fragment
    private val viewModel: TransactionViewModel by activityViewModels(){
        TransactionViewModelFactory((requireActivity().application as MoneyKeeperApplication).repository)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentTransactionBinding.inflate(inflater, container,false)
        binding.fragment = this
        binding.transactionViewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.dateInputText.text = formatDatePretty(ZonedDateTime.now())
        binding.timeInputText.text = formatTimePretty(LocalTime.now())
    }

    fun createTimePicker(){
        val picker =
            MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .setHour(12)
                .setMinute(10)
                .setTitleText("Transaction time")
                .build()
        picker.show(this.childFragmentManager, "transaction timepicker")
        picker.addOnPositiveButtonClickListener(){
            binding.timeInputText.text = formatTimePretty(LocalTime.of(picker.hour, picker.minute))
        }
    }

    fun createDatePicker(){
        val datepicker =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText("Transaction date")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build()
        datepicker.show(this.childFragmentManager, "transaction datepicker")
        datepicker.addOnPositiveButtonClickListener {
            binding.dateInputText.text = formatDatePretty(ZonedDateTime.ofInstant(Instant.ofEpochMilli(datepicker.selection!!),
                ZoneOffset.UTC))
        }
    }

    fun switchTransactionType(){
        binding.moneyTextInput.prefixText = if (binding.signSwitch.isChecked) "$ -" else "$"
    }
}