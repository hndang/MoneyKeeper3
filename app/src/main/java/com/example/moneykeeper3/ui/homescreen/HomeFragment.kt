package com.example.moneykeeper3.ui.homescreen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.annotation.WorkerThread
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.moneykeeper3.R
import com.example.moneykeeper3.database.Category
import com.example.moneykeeper3.database.Transaction
import com.example.moneykeeper3.database.TransactionDatabase
import com.example.moneykeeper3.databinding.FragmentHomeBinding
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var db: TransactionDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(layoutInflater,R.layout.fragment_home, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        db = TransactionDatabase.getDatabase(requireContext(), lifecycleScope)

        binding.bAddCategory.setOnClickListener(){
            lifecycleScope.launch(){
                insertT()
            }

        }
        binding.bAddTransaction.setOnClickListener(){
            lifecycleScope.launch(){
                insertC()
            }
        }
    }
    @WorkerThread
    suspend fun insertT(){
        val random = Math.random()*10
        db.transactionDao().insertTransaction(Transaction(0,22,33.0,"category x", "note"))
    }

    @WorkerThread
    suspend fun insertC(){
        val random = Math.random()*10
        db.transactionDao().insertCategory(Category("category ${random}",1,1))
    }

}