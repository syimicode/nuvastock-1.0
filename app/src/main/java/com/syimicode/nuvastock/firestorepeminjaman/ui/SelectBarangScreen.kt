package com.syimicode.nuvastock.firestorepeminjaman.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.syimicode.nuvastock.R
import com.syimicode.nuvastock.common.CommonDialog
import com.syimicode.nuvastock.firestorebarang.BarangModel
import com.syimicode.nuvastock.firestorebarang.BarangViewModel
import com.syimicode.nuvastock.firestorepeminjaman.SharedViewModelBarang
import com.syimicode.nuvastock.ui.theme.blue
import com.syimicode.nuvastock.ui.theme.fontexo
import com.syimicode.nuvastock.ui.theme.line
import com.syimicode.nuvastock.ui.theme.text_primary
import com.syimicode.nuvastock.ui.theme.text_secondary
import com.syimicode.nuvastock.ui.theme.white
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp

@Composable
fun SelectBarangScreen(
    navController: NavHostController,
    viewModel: BarangViewModel = hiltViewModel(),
    sharedViewModelBarang: SharedViewModelBarang
) {

    val res = viewModel.res.value

    // State untuk menyimpan jumlah total barang yang dipilih
    var totalJumlah by rememberSaveable { mutableStateOf(sharedViewModelBarang.selectedBarang.values.sum()) }

    // Fungsi untuk mengupdate totalJumlah
    fun updateTotalJumlah(newJumlah: Int) {
        totalJumlah = maxOf(0, totalJumlah + newJumlah)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(white)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(white)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.sdp)
                    .background(white),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    modifier = Modifier
                        .padding(end = 16.sdp)
                        .size(20.sdp)
                        .background(white),
                    onClick = { navController.navigateUp() }
                ) {

                    Icon(
                        painter = painterResource(id = R.drawable.ic_left),
                        contentDescription = "ic_left",
                        modifier = Modifier
                            .size(20.sdp),
                        tint = Color.Unspecified
                    )
                }

                Text(
                    text = "Pilih Barang",
                    style = TextStyle(
                        color = text_primary,
                        fontSize = 16.ssp,
                        fontFamily = fontexo,
                        fontWeight = FontWeight.SemiBold,
                    )
                )
            }

            Divider(
                color = line,
                thickness = 1.sdp,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
            )
        }


        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(white)
        ) {

            // Bagian Daftar Barang
            if (res.data.isNotEmpty()) {

                // Urutkan isi LazyColumn berasarkan nama
                val sortedData = res.data.sortedBy { it.item?.namaBarang?.lowercase() }

                LazyColumn(
                    modifier = Modifier
                        .background(white)
                        .padding(bottom = 77.sdp),
                ) {
                    items(
                        sortedData,
                        key = { it.key ?: "" }
                    ) { items ->

                        val initialJumlah = sharedViewModelBarang.selectedBarang[items] ?: 0

                        EachRowSelectBarang(
                            itemsState = items,
                            updateTotalJumlah = { updateTotalJumlah(it) },
                            sharedViewModelBarang = sharedViewModelBarang,
                            initialJumlah = initialJumlah // Gunakan nilai jumlah sebelumnya
                        )
                    }
                }
            }

            // Bagian Button
            Surface(
                modifier = Modifier.align(Alignment.BottomCenter),
                shadowElevation = 8.sdp, // Menambahkan efek bayangan
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(white)
                        .padding(16.sdp),
                    horizontalArrangement = Arrangement.spacedBy(24.sdp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Row(
                        modifier = Modifier.background(white),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Text(
                            text = "$totalJumlah",
                            modifier = Modifier
                                .width(34.sdp)
                                .padding(end = 4.sdp),
                            style = TextStyle(
                                color = blue,
                                fontSize = 20.ssp,
                                fontFamily = fontexo,
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.SemiBold,
                            )
                        )

                        Text(
                            text = "Jumlah Total",
                            style = TextStyle(
                                color = text_secondary,
                                fontSize = 12.ssp,
                                fontFamily = fontexo,
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.Normal,
                            )
                        )
                    }

                    // Pada bagian tombol "Selesai", simpan data barang yang sudah dipilih
                    Button(
                        onClick = {
                            // Navigasi ke AddPeminjamanScreen
                            navController.navigateUp()
                        },
                        contentPadding = PaddingValues(vertical = 12.sdp),
                        shape = RoundedCornerShape(10.sdp),
                        colors = ButtonDefaults.buttonColors(containerColor = blue),
                        modifier = Modifier
                            .weight(1f)
                    ) {
                        Text(
                            text = "Selesai",
                            style = TextStyle(
                                color = white,
                                fontSize = 14.ssp,
                                fontFamily = fontexo,
                                fontWeight = FontWeight.SemiBold,
                                textAlign = TextAlign.Center
                            )
                        )
                    }
                }
            }
        }

        if (res.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CommonDialog()
            }
        }

        if (res.error.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = res.error)
            }
        }

    }
}


@Composable
fun EachRowSelectBarang(
    itemsState: BarangModel,
    updateTotalJumlah: (Int) -> Unit,
    sharedViewModelBarang: SharedViewModelBarang,
    initialJumlah: Int // Tambahkan parameter untuk menyimpan nilai jumlah barang sebelumnya
) {

    // Mengambil keterangan, jika null maka diubah menjadi string kosong
    val keterangan = itemsState.item?.keterangan?.trim() ?: ""

    // Batasan panjang teks keterangan
    val maxKeteranganLength = 30

    val trimmedKeterangan = if (keterangan.isNotEmpty()) {
        if (keterangan.length > maxKeteranganLength) {
            // Potong teks jika lebih panjang dari maxKeteranganLength
            "${keterangan.substring(0, maxKeteranganLength)}..."
        } else {
            keterangan
        }
    } else {
        "" // Kembalikan string kosong jika keterangan kosong setelah di-trim
    }

    // Tampilkan keterangan dan simbol | jika keterangan tidak kosong setelah di-trim
    val keteranganText = if (trimmedKeterangan.isNotEmpty()) {
        "| $trimmedKeterangan"
    } else {
        "" // Kosongkan keterangan jika keterangan kosong setelah di-trim
    }

    // Menyimpan jumlah terkini untuk setiap item
    var jumlah by rememberSaveable { mutableStateOf(initialJumlah) }


    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(white)
                .padding(16.sdp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(white),
                verticalAlignment = Alignment.CenterVertically
            ) {

                // Menampilkan gambar dari URL imgBarangUrl
                val painter = rememberAsyncImagePainter(
                    ImageRequest.Builder(LocalContext.current)
                        .data(itemsState.item?.imgBarangUrl ?: "")
                        .placeholder(R.drawable.upload_img) // Placeholder jika gambar tidak ada
                        .error(R.drawable.upload_img) // Placeholder jika terjadi kesalahan saat memuat gambar
                        .crossfade(true) // Efek fade saat gambar berubah
                        .build()
                )

                Image(
                    painter = painter,
                    contentDescription = "img barang",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(40.sdp)
                        .clip(RoundedCornerShape(8.sdp))
                )

                Spacer(modifier = Modifier.width(12.sdp))

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .background(white),
                ) {
                    Text(
                        text = if ((itemsState.item?.namaBarang?.length ?: 0) > 21) {
                            "${itemsState.item?.namaBarang?.substring(0, 21)}..."
                        } else {
                            itemsState.item?.namaBarang!!
                        },
                        style = TextStyle(
                            color = text_primary,
                            fontSize = 12.ssp,
                            fontFamily = fontexo,
                            fontWeight = FontWeight.Normal,
                        ),
                        maxLines = 1, // Hanya satu baris
                        // Tambahkan ellipsis jika teks melebihi batas
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(4.sdp))

                    Text(
                        text = "${itemsState.item?.harga} $keteranganText",
                        style = TextStyle(
                            color = text_secondary,
                            fontSize = 10.ssp,
                            fontFamily = fontexo,
                            fontWeight = FontWeight.Normal,
                        ),
                        maxLines = 1, // Hanya satu baris
                        // Tambahkan ellipsis jika teks melebihi batas
                        overflow = TextOverflow.Ellipsis
                    )
                }

                IconButton(
                    modifier = Modifier
                        .padding(start = 16.sdp)
                        .clip(RoundedCornerShape(4.sdp))
                        .background(line)
                        .size(24.sdp),
                    onClick = {
                        if (jumlah > 0) {
                            jumlah = maxOf(0, jumlah - 1)
                            updateTotalJumlah(-1) // Kurangi satu dari totalJumlah
                            sharedViewModelBarang.selectedBarang[itemsState] = jumlah
                        }
                    }
                ) {

                    Icon(
                        painter = painterResource(id = R.drawable.ic_minus),
                        contentDescription = "ic_minus",
                        modifier = Modifier
                            .size(12.sdp),
                        tint = Color.Unspecified
                    )
                }

                Text(
                    text = if (jumlah > 0) jumlah.toString() else "0",
                    modifier = Modifier
                        .width(26.sdp)
                        .padding(start = 8.sdp),
                    style = TextStyle(
                        color = text_primary,
                        fontSize = 14.ssp,
                        fontFamily = fontexo,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Normal,
                    )
                )

                IconButton(
                    modifier = Modifier
                        .padding(start = 8.sdp)
                        .clip(RoundedCornerShape(4.sdp))
                        .background(line)
                        .size(24.sdp),
                    onClick = {
                        jumlah += 1
                        updateTotalJumlah(1) // Tambah satu ke totalJumlah
                        sharedViewModelBarang.selectedBarang[itemsState] = jumlah
                    }
                ) {

                    Icon(
                        painter = painterResource(id = R.drawable.ic_plus),
                        contentDescription = "ic_plus",
                        modifier = Modifier
                            .size(12.sdp),
                        tint = Color.Unspecified
                    )
                }
            }

        }
    }
}