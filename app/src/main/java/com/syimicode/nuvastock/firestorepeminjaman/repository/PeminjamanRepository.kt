package com.syimicode.nuvastock.firestorepeminjaman.repository

import com.syimicode.nuvastock.firestorepeminjaman.PeminjamanModel
import com.syimicode.nuvastock.utils.ResultState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface PeminjamanRepository {

    fun insert(item: PeminjamanModel): Flow<ResultState<String>>

    fun getItems(): Flow<ResultState<List<PeminjamanModel>>>

    fun getRiwayatPinjamCount(): StateFlow<Int>

    // Tambahkan fungsi untuk mengupdate status barang yang sudah dikembalikan ke Firestore
    fun updateStatusBarang(peminjaman: PeminjamanModel): ResultState<String>

}