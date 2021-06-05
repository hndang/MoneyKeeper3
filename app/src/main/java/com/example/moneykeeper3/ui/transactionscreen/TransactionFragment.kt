package com.example.moneykeeper3.ui.transactionscreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.moneykeeper3.*
import com.example.moneykeeper3.database.MoneyKeeperApplication
import com.example.moneykeeper3.database.Transaction
import com.example.moneykeeper3.databinding.TransactionFragmentBinding
import com.example.moneykeeper3.model.TransactionViewModel
import com.example.moneykeeper3.model.TransactionViewModelFactory
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import timber.log.Timber
import java.time.*


class TransactionFragment : Fragment() {

    private lateinit var binding: TransactionFragmentBinding
    // Shared view model b/w different fragment
    private val viewModel: TransactionViewModel by activityViewModels(){
        TransactionViewModelFactory((requireActivity().application as MoneyKeeperApplication).repository)
    }
    private val args: TransactionFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private fun createNewTransaction() {
        Timber.d("New transaction: Use Default layout")
        viewModel.resetTransaction()
    }

    private fun createEditTransaction(id: Long) {
        Timber.d("Edit transaction")
        //populate view
        if(id == -1L){
            Timber.e("Invalid transaction id !")
            view?.let { Snackbar.make(it, "Invalid transaction", Snackbar.LENGTH_SHORT) }
            cancel()
        }
        binding.deleteButton.visibility = View.VISIBLE
        viewModel.getTransaction(id)
//        viewModel.currentTransaction.value?.let{ transaction ->
//            binding.moneyTextInput.editText?.setText(transaction.transactionAmount.toString())
//        }

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = TransactionFragmentBinding.inflate(inflater, container, false)
        binding.fragment = this
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.d("set category adapter")
        (binding.categoryTextInput.editText as? AutoCompleteTextView)?.setAdapter(
            CategoryListAdapter(
                requireContext(), mutableListOf(
                    defaultCategory[1]
                )
            )
        )

        if(args.isEdit){
            createEditTransaction(args.transactionId)
        }else{
            createNewTransaction()
        }

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
            binding.dateInputText.text = formatDatePretty(
                ZonedDateTime.ofInstant(
                    Instant.ofEpochMilli(
                        datepicker.selection!!
                    ),
                    ZoneOffset.UTC
                )
            )
        }
    }

    fun switchTransactionType(){
        binding.moneyTextInput.prefixText = if (binding.signSwitch.isChecked) "$ -" else "$"
    }

    fun cancel(){
        findNavController().popBackStack()
    }

    fun delete(){
        val test = viewModel.currentTransaction.value
        test?.let {
            viewModel.deleteTransaction(it)
            cancel()
        }
    }

    fun save(){
        //verify non empty element
        val sign = if (binding.signSwitch.isChecked) -1 else 1
        val moneyAmt =
                if (verifyTextInput(binding.moneyTextInput, "no money amount inputted"))
                    binding.moneyTextInput.editText?.text.toString().toDouble()*sign
                else null

        val category =
                if (verifyTextInput(binding.categoryTextInput, "no category chosen"))
                    binding.categoryTextInput.editText?.text.toString()
                else null
        val dateTime = getDateTimePretty(
            binding.dateInputText.text.toString(),
            binding.timeInputText.text.toString()
        )
        val note = binding.noteTextInput.editText?.text.toString() ?: ""

        // Debug log
        Timber.d("money:$moneyAmt")
        Timber.d("category:$category")
        Timber.d("datetime:$dateTime")
        Timber.d("note:$note")

        // Saving

        if (moneyAmt != null && category != null){

            // Edit
            if(args.isEdit){
                viewModel.updateTransaction(Transaction(
                    transactionId = args.transactionId,
                    transactionDate = convertCalendarForRepo(dateTime),
                    transactionAmount = moneyAmt,
                    categoryName = category,
                    transactionNote = note)
                )
            // new
            }else{
                viewModel.newTransaction(Transaction(
                    transactionDate = convertCalendarForRepo(dateTime),
                    transactionAmount = moneyAmt,
                    categoryName = category,
                    transactionNote = note)
                )
            }
            // todo notification for cancel and saved job seaparately
            cancel()
        }
    }

    private fun verifyTextInput(textField: TextInputLayout, errorMsg: String = "no value inputted") : Boolean{
        textField.editText?.let {
            return if(it.text.toString() == ""){
                textField.isErrorEnabled = true
                textField.error = errorMsg
                false
            }else{
                textField.isErrorEnabled = false
                true
            }
        } ?: return false
    }
}