package com.syimicode.nuvastock.firestorecustomer

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.syimicode.nuvastock.firestorecustomer.repository.CustomerRepository
import com.syimicode.nuvastock.utils.ResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CustomerViewModel @Inject constructor(
    private val repo: CustomerRepository
) : ViewModel() {

    private val _res: MutableState<CustomerState> = mutableStateOf(CustomerState())
    val res: State<CustomerState> = _res

    val customerCount: StateFlow<Int> = repo.getCustomerCount()

    fun insert(item: CustomerModel.CustomerItem) = repo.insert(item)

    private val _updateData: MutableState<CustomerModel> = mutableStateOf(
        CustomerModel(
            item = CustomerModel.CustomerItem()
        )
    )

    fun setData(data: CustomerModel) {
        _updateData.value = data
    }

    init {
        getItems()
    }

    fun getItems() = viewModelScope.launch {
        repo.getItems().collect {
            when (it) {
                is ResultState.Success -> {
                    _res.value = CustomerState(data = it.data)
                }

                is ResultState.Failure -> {
                    _res.value = CustomerState(
                        error = it.msg.toString()
                    )
                }

                ResultState.Loading -> {
                    _res.value = CustomerState(
                        isLoading = true
                    )
                }
            }
        }
    }

    fun delete(key: String) = repo.delete(key)

    fun update(item: CustomerModel) = repo.update(item)
}


data class CustomerState(
    val data: List<CustomerModel> = emptyList(),
    val error: String = "",
    val isLoading: Boolean = false
)