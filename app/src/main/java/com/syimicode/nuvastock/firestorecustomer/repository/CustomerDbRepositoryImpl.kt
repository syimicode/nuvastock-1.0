package com.syimicode.nuvastock.firestorecustomer.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.syimicode.nuvastock.firestorecustomer.CustomerModel
import com.syimicode.nuvastock.utils.ResultState
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class CustomerDbRepositoryImpl @Inject constructor(
    private val db: FirebaseFirestore
) : CustomerRepository {

    override fun insert(item: CustomerModel.CustomerItem): Flow<ResultState<String>> =
        callbackFlow {
            trySend(ResultState.Loading)
            db.collection("Customer")
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

    override fun getItems(): Flow<ResultState<List<CustomerModel>>> = callbackFlow {
        trySend(ResultState.Loading)
        db.collection("Customer")
            .get()
            .addOnSuccessListener {
                val items = it.map { data ->
                    CustomerModel(
                        item = CustomerModel.CustomerItem(
                            imgKtpUrl = data["imgKtpUrl"] as String?,
                            namaCustomer = data["namaCustomer"] as String?,
                            whatsapp = data["whatsapp"] as String?,
                            alamat = data["alamat"] as String?
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

    override fun delete(key: String): Flow<ResultState<String>> = callbackFlow {
        trySend(ResultState.Loading)
        db.collection("Customer")
            .document(key)
            .delete()
            .addOnCompleteListener {
                if (it.isSuccessful)
                    trySend(ResultState.Success(" Berhasil dihapus "))
            }.addOnFailureListener {
                trySend(ResultState.Failure(it))
            }

        awaitClose {
            close()
        }
    }

    override fun update(item: CustomerModel): Flow<ResultState<String>> = callbackFlow {
        trySend(ResultState.Loading)
        val map = HashMap<String, Any>()
        map["imgKtpUrl"] = item.item?.imgKtpUrl!!
        map["namaCustomer"] = item.item.namaCustomer!!
        map["whatsapp"] = item.item.whatsapp!!
        map["alamat"] = item.item.alamat!!

        if (!item.key.isNullOrEmpty()) {
            db.collection("Customer")
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

    private val _customerCount = MutableStateFlow(0)
    override fun getCustomerCount(): StateFlow<Int> = _customerCount.asStateFlow()

    private fun fetchCustomerCount() {
        db.collection("Customer")
            .addSnapshotListener { querySnapshot, _ ->
                val count = querySnapshot?.size() ?: 0
                _customerCount.value = count
            }
    }

    init {
        fetchCustomerCount()
    }
}