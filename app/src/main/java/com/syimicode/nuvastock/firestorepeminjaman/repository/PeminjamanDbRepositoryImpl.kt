package com.syimicode.nuvastock.firestorepeminjaman.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.syimicode.nuvastock.firestorebarang.BarangModel
import com.syimicode.nuvastock.firestorebarang.repository.BarangRepository
import com.syimicode.nuvastock.firestorepeminjaman.BarangPeminjamanModel
import com.syimicode.nuvastock.firestorepeminjaman.PeminjamanModel
import com.syimicode.nuvastock.utils.ResultState
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class PeminjamanDbRepositoryImpl @Inject constructor(
    private val db: FirebaseFirestore
) : PeminjamanRepository {

    override fun insert(item: PeminjamanModel): Flow<ResultState<String>> =
        callbackFlow {
            trySend(ResultState.Loading)
            db.collection("Peminjaman")
                .add(item)
                .addOnSuccessListener {

                    val idPesanan = it.id // Dapatkan ID baru dari dokumen yang ditambahkan
                    val itemWithId =
                        item.copy(idPesanan = idPesanan) // Salin item dan tetapkan ID dokumen yang baru diperoleh

                    db.collection("Peminjaman").document(it.id)
                        .set(itemWithId) // Perbarui dokumen dengan item yang memiliki IDPesanan yang diperbarui
                        .addOnSuccessListener {
                            trySend(ResultState.Success(" Berhasil ditambah "))
                        }.addOnFailureListener {
                            trySend(ResultState.Failure(it))
                        }

                }.addOnFailureListener {
                    trySend(ResultState.Failure(it))
                }

            awaitClose {
                close()
            }
        }

    override fun getItems(): Flow<ResultState<List<PeminjamanModel>>> = callbackFlow {
        trySend(ResultState.Loading)
        db.collection("Peminjaman")
            .get()
            .addOnSuccessListener {
                val items = it.map { document ->
                    val idPesanan = document.id // Dapatkan Document ID sebagai ID pesanan
                    val namaPeminjam = document.getString("namaPeminjam")
                    val tanggalPeminjaman = document.getString("tanggalPeminjaman")
                    val lamaPeminjaman = document.getString("lamaPeminjaman")
                    val statusBarang = document.getBoolean("sudahDikembalikan") ?: false

                    // Mengambil daftar barang yang dipilih beserta jumlahnya
                    val barangList = mutableListOf<BarangPeminjamanModel>()
                    val barangDocuments = document.get("barang") as List<Map<String, Any>>
                    for (barangDocument in barangDocuments) {
                        val barangId = barangDocument["barangId"] as String
                        val imgBarangUrl = barangDocument["imgBarangUrl"] as String
                        val namaBarang = barangDocument["namaBarang"] as String
                        val harga = barangDocument["harga"] as String
                        val jumlah = barangDocument["jumlahPinjam"] as String
                        val keterangan = barangDocument["keterangan"] as String
                        val barang = BarangPeminjamanModel(
                            barangId,
                            imgBarangUrl,
                            namaBarang,
                            harga,
                            jumlah,
                            keterangan
                        )
                        barangList.add(barang)
                    }

                    // Membuat objek PeminjamanModel dari data yang diambil
                    PeminjamanModel(
                        idPesanan, // Gunakan Document ID sebagai ID pesanan
                        namaPeminjam,
                        tanggalPeminjaman,
                        lamaPeminjaman,
                        statusBarang,
                        barangList
                    )
                }
                trySend(ResultState.Success(items))
            }.addOnFailureListener { exception ->
                trySend(ResultState.Failure(exception))
            }

        awaitClose {
            close()
        }
    }

    private val _riwayatPinjamCount = MutableStateFlow(0)
    override fun getRiwayatPinjamCount(): StateFlow<Int> = _riwayatPinjamCount.asStateFlow()

    private fun fetchRiwayatPinjamCount() {
        db.collection("Peminjaman")
            .addSnapshotListener { querySnapshot, _ ->
                val count = querySnapshot?.size() ?: 0
                _riwayatPinjamCount.value = count
            }
    }

    init {
        fetchRiwayatPinjamCount()
    }

    override fun updateStatusBarang(peminjaman: PeminjamanModel): ResultState<String> {
        return try {
            db.collection("Peminjaman").document(peminjaman.idPesanan!!)
                // Perbarui properti sudahDikembalikan menjadi true
                .update("sudahDikembalikan", true)
                .addOnSuccessListener {
                    ResultState.Success(" Status berhasil diperbarui ")
                }
                .addOnFailureListener { exception ->
                    ResultState.Failure(exception)
                }
            ResultState.Loading
        } catch (e: Exception) {
            ResultState.Failure(e)
        }
    }

}