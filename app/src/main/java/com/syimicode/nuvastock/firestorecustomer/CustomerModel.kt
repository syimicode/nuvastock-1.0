package com.syimicode.nuvastock.firestorecustomer

data class CustomerModel(
    val item: CustomerItem?,
    val key: String? = ""
) {
    data class CustomerItem(
        var imgKtpUrl: String? = "",
        var namaCustomer: String? = "",
        var whatsapp: String? = "",
        var alamat: String? = "",
    )
}