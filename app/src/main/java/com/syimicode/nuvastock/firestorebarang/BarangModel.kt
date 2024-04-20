package com.syimicode.nuvastock.firestorebarang

data class BarangModel(
    val item: BarangItem?,
    val key: String? = ""
) {
    data class BarangItem(
        var imgBarangUrl: String? = "",
        var namaBarang: String? = "",
        var harga: String? = "",
        var jumlah: String? = "",
        var keterangan: String? = ""
    )
}