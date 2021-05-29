package com.example.moneykeeper3.ui.homescreen

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.annotation.WorkerThread
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.moneykeeper3.R
import com.example.moneykeeper3.convertCalendarForRepo
import com.example.moneykeeper3.database.Category
import com.example.moneykeeper3.database.MoneyKeeperApplication
import com.example.moneykeeper3.database.Transaction
import com.example.moneykeeper3.database.TransactionDatabase
import com.example.moneykeeper3.databinding.FragmentHomeBinding
import com.example.moneykeeper3.getDateOfDay
import com.example.moneykeeper3.getDateOfMonth
import com.example.moneykeeper3.model.TransactionViewModel
import com.example.moneykeeper3.model.TransactionViewModelFactory
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    private val viewModel:TransactionViewModel by viewModels{
        TransactionViewModelFactory((requireActivity().application as MoneyKeeperApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.rvTransactions.adapter = TransactionListAdapter()
        binding.viewModel = viewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }


}