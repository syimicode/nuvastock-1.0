package com.syimicode.nuvastock.firestorebarang.repository

import com.syimicode.nuvastock.firestorebarang.BarangModel
import com.syimicode.nuvastock.utils.ResultState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface BarangRepository {

    fun insert(item: BarangModel.BarangItem): Flow<ResultState<String>>

    fun getItems(): Flow<ResultState<List<BarangModel>>>

    fun update(item: BarangModel): Flow<ResultState<String>>

    fun getBarangCount(): StateFlow<Int>
}