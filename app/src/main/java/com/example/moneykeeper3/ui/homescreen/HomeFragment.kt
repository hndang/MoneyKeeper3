package com.example.moneykeeper3.ui.homescreen

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.TextView
import androidx.annotation.WorkerThread
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ConcatAdapter
import com.example.moneykeeper3.R
import com.example.moneykeeper3.convertCalendarForRepo
import com.example.moneykeeper3.database.Category
import com.example.moneykeeper3.database.MoneyKeeperApplication
import com.example.moneykeeper3.database.Transaction
import com.example.moneykeeper3.database.TransactionDatabase
import com.example.moneykeeper3.databinding.HomeFragmentBinding
import com.example.moneykeeper3.getDateOfDay
import com.example.moneykeeper3.getDateOfMonth
import com.example.moneykeeper3.model.TransactionViewModel
import com.example.moneykeeper3.model.TransactionViewModelFactory
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable
import kotlinx.coroutines.launch
import timber.log.Timber

class HomeFragment : Fragment() {

    private lateinit var binding: HomeFragmentBinding

    // Shared view model b/w different fragment
    private val viewModel:TransactionViewModel by activityViewModels(){
        TransactionViewModelFactory((requireActivity().application as MoneyKeeperApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onResume() {
        super.onResume()
        val inputManager = requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(view?.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = HomeFragmentBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        val transactionListAdapter = TransactionListAdapter(this)
        val filterAdapter = FilterAdapter(viewModel)
        val graphAdapter = GraphAdapter(viewModel)
        binding.rvTransactions.adapter = ConcatAdapter(filterAdapter,transactionListAdapter)
        binding.fragment = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }


    fun newChip(viewGroup: ViewGroup): Chip{
        return layoutInflater.inflate(R.layout.standalone_chip, viewGroup, false) as Chip
    }

    fun newTransaction(){
        findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToTransactionFragment())
    }

    fun editTransaction(transactionId : Long){
        findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToTransactionFragment(true, transactionId))
    }

}