package com.syimicode.nuvastock.firestorecustomer.repository

import com.syimicode.nuvastock.firestorecustomer.CustomerModel
import com.syimicode.nuvastock.utils.ResultState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface CustomerRepository {

    fun insert(item: CustomerModel.CustomerItem): Flow<ResultState<String>>

    fun getItems(): Flow<ResultState<List<CustomerModel>>>

    fun delete(key: String): Flow<ResultState<String>>

    fun update(item: CustomerModel): Flow<ResultState<String>>

    fun getCustomerCount(): StateFlow<Int>
}