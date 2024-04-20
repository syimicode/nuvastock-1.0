package com.syimicode.nuvastock.firestorebarang.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.syimicode.nuvastock.firestorebarang.BarangModel
import com.syimicode.nuvastock.utils.ResultState
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class BarangDbRepositoryImpl @Inject constructor(
    private val db: FirebaseFirestore
) : BarangRepository {

    override fun insert(item: BarangModel.BarangItem): Flow<ResultState<String>> =
        callbackFlow {
            trySend(ResultState.Loading)
            db.collection("Barang")
                .add(item)
                .addOnSuccessListener {
                    trySend(ResultState.Success(" Berhasil ditambah "))
                }.addOnFailureListener {
                    trySend(ResultState.Failure(it))
                }

            awaitClose {
                close()
            }
        }

    override fun getItems(): Flow<ResultState<List<BarangModel>>> = callbackFlow {
        trySend(ResultState.Loading)
        db.collection("Barang")
            .get()
            .addOnSuccessListener {
                val items = it.map { data ->
                    BarangModel(
                        item = BarangModel.BarangItem(
                            imgBarangUrl = data["imgBarangUrl"] as String?,
                            namaBarang = data["namaBarang"] as String?,
                            harga = data["harga"] as String?,
                            jumlah = data["jumlah"] as String?,
                            keterangan = data["keterangan"] as String?
                        ),
                        key = data.id
                    )
                }
                trySend(ResultState.Success(items))
            }.addOnFailureListener {
                trySend(ResultState.Failure(it))
            }

        awaitClose {
            close()
        }
    }

    override fun update(item: BarangModel): Flow<ResultState<String>> = callbackFlow {
        trySend(ResultState.Loading)
        val map = HashMap<String, Any>()
        map["imgBarangUrl"] = item.item?.imgBarangUrl!!
        map["namaBarang"] = item.item.namaBarang!!
        map["harga"] = item.item.harga!!
        map["jumlah"] = item.item.jumlah!!
        map["keterangan"] = item.item.keterangan!!

        if (!item.key.isNullOrEmpty()) {
            db.collection("Barang")
                .document(item.key)
                .update(map)
                .addOnCompleteListener {
                    if (it.isSuccessful)
                        trySend(ResultState.Success(" Berhasil diubah "))
                    else
                        trySend(ResultState.Failure(Exception(" Gagal mengubah ")))
                }.addOnFailureListener {
                    trySend(ResultState.Failure(it))
                }
        }

        awaitClose {
            close()
        }
    }

    private val _barangCount = MutableStateFlow(0)
    override fun getBarangCount(): StateFlow<Int> = _barangCount.asStateFlow()

    private fun fetchBarangCount() {
        db.collection("Barang")
            .addSnapshotListener { querySnapshot, _ ->
                val count = querySnapshot?.size() ?: 0
                _barangCount.value = count
            }
    }

    init {
        fetchBarangCount()
    }
}