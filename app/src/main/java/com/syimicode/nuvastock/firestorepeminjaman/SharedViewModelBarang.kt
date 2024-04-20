package com.syimicode.nuvastock.firestorepeminjaman

import androidx.lifecycle.ViewModel
import com.syimicode.nuvastock.firestorebarang.BarangModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class SharedViewModelBarang @Inject constructor() : ViewModel() {

    // Map untuk menyimpan barang yang dipilih beserta jumlahnya
    val selectedBarang: MutableMap<BarangModel, Int> = mutableMapOf()

    // Fungsi untuk mereset daftar barang yang dipilih
    fun resetSelectedBarang() {
        selectedBarang.clear() // Menghapus semua barang yang dipilih dari map
    }
}