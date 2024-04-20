package com.syimicode.nuvastock.firestorepeminjaman

import android.util.Log
import androidx.lifecycle.ViewModel
import com.syimicode.nuvastock.firestorecustomer.CustomerModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SharedViewModelCustomer @Inject constructor() : ViewModel() {

    // Menyimpan data customer yang dipilih pada AddPeminjamanScreen
    private val _selectedCustomer: MutableStateFlow<CustomerModel?> = MutableStateFlow(null)
    val selectedCustomer: StateFlow<CustomerModel?> = _selectedCustomer.asStateFlow()

    fun setSelectedCustomer(customer: CustomerModel) {
        _selectedCustomer.value = customer
        Log.d("SharedViewModel", "Selected customer changed: ${customer.item?.namaCustomer}")
        Log.d("SharedViewModel", "Customer key: ${customer.key}")
    }

    // Fungsi untuk mereset selectedCustomer menjadi null saat tidak jadi memilih customer
    fun resetSelectedCustomer() {
        _selectedCustomer.value = null
        Log.d("SharedViewModel", "Selected customer reset to null")
    }
}