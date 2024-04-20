package com.syimicode.nuvastock.firestorepeminjaman

data class PeminjamanModel(
    val idPesanan: String? = "", // Properti untuk menyimpan ID dokumen Firestore
    val namaPeminjam: String? = "", // Nama pelanggan
    val tanggalPeminjaman: String? = "", // Tanggal peminjaman dalam format yang sesuai
    val lamaPeminjaman: String? = "", // Durasi sewa dalam jumlah hari atau rentang waktu yang sesuai
    var sudahDikembalikan: Boolean = false, // Properti untuk menandai apakah status barang sudah dikembalikan atau belum
    val barang: List<BarangPeminjamanModel> // Daftar barang yang dipilih beserta jumlahnya
)

data class BarangPeminjamanModel(
    val barangId: String? = "", // ID barang
    var imgBarangUrl: String? = "",
    var namaBarang: String? = "",
    var harga: String? = "",
    var jumlahPinjam: String? = "",
    var keterangan: String? = ""
)