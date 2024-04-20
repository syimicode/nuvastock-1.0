package com.syimicode.nuvastock.firestorebarang

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.syimicode.nuvastock.firestorebarang.repository.BarangRepository
import com.syimicode.nuvastock.utils.ResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BarangViewModel @Inject constructor(
    private val repo: BarangRepository
) : ViewModel() {

    private val _res: MutableState<BarangState> = mutableStateOf(BarangState())
    private val originalData = MutableStateFlow<List<BarangModel>>(emptyList())

    val res: State<BarangState> = _res

    val barangCount: StateFlow<Int> = repo.getBarangCount()

    fun insert(item: BarangModel.BarangItem) = repo.insert(item)

    init {
        getItems()
    }

    fun getItems() = viewModelScope.launch {
        repo.getItems().collect {
            when (it) {
                is ResultState.Success -> {
                    originalData.value = it.data // Simpan data utuh
                    _res.value = BarangState(data = it.data)
                }

                is ResultState.Failure -> {
                    _res.value = BarangState(
                        error = it.msg.toString()
                    )
                }

                ResultState.Loading -> {
                    _res.value = BarangState(
                        isLoading = true
                    )
                }
            }
        }
    }

    fun update(item: BarangModel) = repo.update(item)

    fun searchBarang(keyword: String) {
        viewModelScope.launch {
            val currentData = originalData.value // Dapatkan data asli
            val filteredData = if (keyword.isNotBlank()) {
                currentData.filter {
                    it.item?.namaBarang?.contains(keyword, ignoreCase = true) ?: false
                }
            } else {
                currentData
            }
            _res.value = BarangState(data = filteredData)
        }
    }

    fun setItems() {
        val currentData = originalData.value // Dapatkan data asli
        _res.value = BarangState(data = currentData)
    }
}


data class BarangState(
    val data: List<BarangModel> = emptyList(),
    val error: String = "",
    val isLoading: Boolean = false
)