package com.syimicode.nuvastock.firestorepeminjaman

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.syimicode.nuvastock.firestorebarang.BarangModel
import com.syimicode.nuvastock.firestorebarang.BarangState
import com.syimicode.nuvastock.firestorepeminjaman.repository.PeminjamanRepository
import com.syimicode.nuvastock.utils.ResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class PeminjamanViewModel @Inject constructor(
    private val repo: PeminjamanRepository
) : ViewModel() {

    private val _res: MutableState<PeminjamanState> = mutableStateOf(PeminjamanState())
    private val originalData = MutableStateFlow<List<PeminjamanModel>>(emptyList())

    val res: State<PeminjamanState> = _res

    val riwayatPinjamCount: StateFlow<Int> = repo.getRiwayatPinjamCount()

    fun insert(item: PeminjamanModel) = repo.insert(item)

    init {
        getItems()
    }

    fun getItems() = viewModelScope.launch {
        repo.getItems().collect {
            when (it) {
                is ResultState.Success -> {
                    // Kelompokkan peminjaman berdasarkan tanggal peminjaman
                    val groupedData = it.data.groupBy { it.tanggalPeminjaman }

                    // Susun data dalam setiap kelompok berdasarkan urutan tanggal peminjaman
                    val sortedGroupedData = groupedData.toSortedMap(compareByDescending {
                        LocalDate.parse(
                            it,
                            DateTimeFormatter.ofPattern("dd MMMM yyyy")
                        )
                    })

                    // Ambil peminjaman hari ini
                    val today = LocalDate.now().format(DateTimeFormatter.ofPattern("dd MMMM yyyy"))

                    // Geser peminjaman hari ini ke posisi teratas jika ada
                    val todayPeminjaman = sortedGroupedData.remove(today)
                    todayPeminjaman?.let {
                        sortedGroupedData[today] = it
                    }

                    // Menggabungkan semua data menjadi satu daftar
                    val combinedData = sortedGroupedData.flatMap { it.value }

                    originalData.value = combinedData // Simpan data utuh
                    _res.value = PeminjamanState(data = combinedData)
                }

                is ResultState.Failure -> {
                    _res.value = PeminjamanState(
                        error = it.msg.toString()
                    )
                }

                ResultState.Loading -> {
                    _res.value = PeminjamanState(
                        isLoading = true
                    )
                }
            }
        }
    }

    // Tambahkan fungsi untuk mengubah properti sudahDikembalikan menjadi true
    fun updateStatusBarang(peminjaman: PeminjamanModel) = viewModelScope.launch {
        val result = repo.updateStatusBarang(peminjaman)
        if (result is ResultState.Success) {
            // Perbarui data lokal jika pembaruan ke Firestore berhasil
            val updatedData = res.value.data.map { item ->
                if (item.idPesanan == peminjaman.idPesanan) {
                    item.copy(sudahDikembalikan = true)
                } else {
                    item
                }
            }
            _res.value = PeminjamanState(data = updatedData)
            Log.d("PeminjamanViewModel", "Data updated: $updatedData")
        }
    }

}


data class PeminjamanState(
    val data: List<PeminjamanModel> = emptyList(),
    val error: String = "",
    val isLoading: Boolean = false
)