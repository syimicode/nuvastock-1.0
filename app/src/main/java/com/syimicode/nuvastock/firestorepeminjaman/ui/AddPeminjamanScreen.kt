package com.syimicode.nuvastock.firestorepeminjaman.ui

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.core.text.isDigitsOnly
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import com.maxkeppeler.sheets.calendar.models.CalendarStyle
import com.syimicode.nuvastock.R
import com.syimicode.nuvastock.common.CommonDialog
import com.syimicode.nuvastock.firestorebarang.BarangModel
import com.syimicode.nuvastock.firestorepeminjaman.BarangPeminjamanModel
import com.syimicode.nuvastock.firestorepeminjaman.PeminjamanModel
import com.syimicode.nuvastock.firestorepeminjaman.PeminjamanViewModel
import com.syimicode.nuvastock.firestorepeminjaman.SharedViewModelBarang
import com.syimicode.nuvastock.firestorepeminjaman.SharedViewModelCustomer
import com.syimicode.nuvastock.screen.menu.Screens
import com.syimicode.nuvastock.ui.theme.blue
import com.syimicode.nuvastock.ui.theme.fontexo
import com.syimicode.nuvastock.ui.theme.line
import com.syimicode.nuvastock.ui.theme.red
import com.syimicode.nuvastock.ui.theme.text_primary
import com.syimicode.nuvastock.ui.theme.text_secondary
import com.syimicode.nuvastock.ui.theme.white
import com.syimicode.nuvastock.utils.ResultState
import com.syimicode.nuvastock.utils.showMsg
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPeminjamanScreen(
    navController: NavHostController,
    sharedViewModelCustomer: SharedViewModelCustomer,
    viewModel: PeminjamanViewModel,
    sharedViewModelBarang: SharedViewModelBarang
) {

    val selectedCustomer by sharedViewModelCustomer.selectedCustomer.collectAsState()
    Log.d("AddPeminjamanScreen", "Selected Customer: $selectedCustomer")


    val calendarState = rememberUseCaseState()

    // State untuk tanggal yang dipilih
    var selectedDate by rememberSaveable { mutableStateOf<LocalDate?>(null) }

    CalendarDialog(
        state = calendarState,
        config = CalendarConfig(
            monthSelection = true,
            yearSelection = true,
            style = CalendarStyle.MONTH
        ),
        selection = CalendarSelection.Date { date ->
            selectedDate = date
        }
    )


    val lamaSewa = rememberSaveable { mutableStateOf("") }


    // Mendapatkan daftar barang yang dipilih
    val barangDipilih = sharedViewModelBarang.selectedBarang

    // Menghitung jumlah total item barang yang dipilih
    val totalItemBarangDipilih = barangDipilih.values.sum()

    val res = viewModel.res.value

    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val isDialog = remember { mutableStateOf(false) }


    if (isDialog.value) {
        CommonDialog()
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
                    onClick = {
                        // Reset pelanggan yang dipilih ke null saat kembali ke halaman sebelumnya
                        navController.navigateUp()
                        sharedViewModelCustomer.resetSelectedCustomer()
                        sharedViewModelBarang.resetSelectedBarang()
                    }
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
                    text = "Tambah Peminjaman",
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


        // #1 ROW CUSTOMER
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(white)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = rememberRipple(bounded = true)
                ) {
                    navController.navigate(Screens.SelectCustomerScreen.name)
                }
                .padding(16.sdp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier
                    .background(white),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = "Customer",
                    modifier = Modifier.width(100.sdp),
                    style = TextStyle(
                        color = text_secondary,
                        fontSize = 14.ssp,
                        fontFamily = fontexo,
                        fontWeight = FontWeight.Normal
                    )
                )

                // Menampilkan nama customer yang dipilih
                val namaCustomer = selectedCustomer?.item?.namaCustomer

                // Menambahkan titik-titik jika namaCustomer melebihi 30 karakter
                val trimmedNamaCustomer = if ((namaCustomer?.length ?: 0) > 18) {
                    "${namaCustomer?.substring(0, 18)}..."
                } else {
                    namaCustomer
                }

                val text = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            color = if (trimmedNamaCustomer.isNullOrEmpty()) text_secondary else text_primary,
                            fontSize = 14.ssp,
                            fontFamily = fontexo,
                            fontWeight = FontWeight.Normal
                        )
                    ) {
                        append(trimmedNamaCustomer ?: "Pilih")
                    }
                }

                Text(text = text)
            }

            Icon(
                painter = painterResource(id = R.drawable.ic_right),
                contentDescription = "ic_right",
                modifier = Modifier
                    .size(20.sdp),
                tint = Color.Unspecified,
            )
        }

        Divider(
            modifier = Modifier.padding(horizontal = 16.sdp),
            color = line,
            thickness = 1.sdp
        )


        // #2 ROW TANGGAL PINJAM
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
                    .background(white),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = "Tgl. Pinjam",
                    modifier = Modifier.width(100.sdp),
                    style = TextStyle(
                        color = text_secondary,
                        fontSize = 14.ssp,
                        fontFamily = fontexo,
                        fontWeight = FontWeight.Normal
                    )
                )

                // Menampilkan tanggal yang dipilih
                val dateText = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            color = if (selectedDate != null) text_primary else text_secondary,
                            fontSize = 14.ssp,
                            fontFamily = fontexo,
                            fontWeight = FontWeight.Normal
                        )
                    ) {
                        append(selectedDate?.let {
                            val formatter =
                                // Ubah format tanggal menjadi "hari-bulan-tahun"
                                DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.getDefault())
                            formatter.format(it)
                        } ?: "Pilih tanggal")
                    }
                }
                Text(text = dateText)
            }

            IconButton(
                modifier = Modifier
                    .size(20.sdp)
                    .background(white),
                onClick = {
                    // Menampilkan dialog kalender saat baris diklik
                    calendarState.show()
                }
            ) {

                Icon(
                    painter = painterResource(id = R.drawable.ic_calendar),
                    contentDescription = "ic_calendar",
                    modifier = Modifier
                        .size(20.sdp),
                    tint = Color.Unspecified,
                )
            }
        }

        Divider(
            modifier = Modifier.padding(horizontal = 16.sdp),
            color = line,
            thickness = 1.sdp
        )


        // #3 ROW LAMA SEWA
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
                    .background(white),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = "Lama Sewa",
                    modifier = Modifier.width(100.sdp),
                    style = TextStyle(
                        color = text_secondary,
                        fontSize = 14.ssp,
                        fontFamily = fontexo,
                        fontWeight = FontWeight.Normal
                    )
                )

                // BasicTextField untuk input lama sewa
                BasicTextField(
                    value = lamaSewa.value,
                    onValueChange = {
                        if (it.isDigitsOnly()) lamaSewa.value = it
                    },
                    textStyle = TextStyle(
                        color = text_primary,
                        fontSize = 14.ssp,
                        fontFamily = fontexo,
                        fontWeight = FontWeight.Normal
                    ),
                    cursorBrush = SolidColor(blue),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    decorationBox = { innerTextField ->
                        Box {
                            innerTextField()
                            if (lamaSewa.value.isEmpty()) {
                                Text(
                                    text = "Masukkan jumlah..",
                                    style = TextStyle(
                                        color = text_secondary,
                                        fontSize = 14.ssp,
                                        fontFamily = fontexo,
                                        fontWeight = FontWeight.Normal,
                                    )
                                )
                            }
                        }
                    }
                )
            }

            Text(
                text = "Hari",
                style = TextStyle(
                    color = text_secondary,
                    fontSize = 14.ssp,
                    fontFamily = fontexo,
                    fontWeight = FontWeight.Normal
                )
            )
        }

        Divider(
            modifier = Modifier.padding(horizontal = 16.sdp),
            color = line,
            thickness = 1.sdp
        )


        // #4 ROW PILIH BARANG
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(white)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = rememberRipple(bounded = true)
                ) {
                    navController.navigate(Screens.SelectBarangScreen.name)
                }
                .padding(16.sdp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier
                    .background(white),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = "Barang",
                    modifier = Modifier.width(100.sdp),
                    style = TextStyle(
                        color = text_secondary,
                        fontSize = 14.ssp,
                        fontFamily = fontexo,
                        fontWeight = FontWeight.Normal
                    )
                )

                // Membuat teks yang mencerminkan jumlah total item barang yang dipilih
                val teksBarangDipilih = if (totalItemBarangDipilih > 0) {
                    // Jika ada item barang yang dipilih, gunakan jumlah item tersebut sebagai teks
                    "$totalItemBarangDipilih Barang"
                } else {
                    // Jika tidak ada item barang yang dipilih, atur teks menjadi "0 Barang"
                    "0 Barang"
                }

                val text = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            color = if (totalItemBarangDipilih > 0) text_primary else text_secondary,
                            fontSize = 14.ssp,
                            fontFamily = fontexo,
                            fontWeight = FontWeight.Normal
                        )
                    ) {
                        append(teksBarangDipilih)
                    }
                }

                Text(text = text)
            }

            Icon(
                painter = painterResource(id = R.drawable.ic_right),
                contentDescription = "ic_right",
                modifier = Modifier
                    .size(20.sdp),
                tint = Color.Unspecified,
            )
        }

        Divider(
            modifier = Modifier.padding(horizontal = 16.sdp),
            color = line,
            thickness = 1.sdp
        )


        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(white)
        ) {

            // Bagian Daftar Barang
            if (res.data.isNotEmpty()) {

                // Filter agar barang yang ditampilkan di LazyColumn adalah barang yang berjumlah lebih dari 0
                val filteredData =
                    barangDipilih.filter { (_, jumlah) -> jumlah > 0 }.keys.toList()

                LazyColumn(
                    modifier = Modifier
                        .background(white)
                        .padding(bottom = 77.sdp)
                ) {
                    items(
                        items = filteredData,
                        key = { it.key ?: "" }
                    ) { items ->
                        EachRowShowBarang(
                            itemsState = items,
                            sharedViewModelBarang = sharedViewModelBarang
                        )
                    }
                }
            }

            // Fungsi untuk memeriksa apakah semua kolom sudah diisi
            fun isValidInput(): Boolean {
                if (selectedCustomer == null ||
                    selectedDate == null ||
                    lamaSewa.value.isEmpty() ||
                    barangDipilih.isEmpty()
                ) {
                    Toast.makeText(
                        context, " Semua kolom wajib diisi ",
                        Toast.LENGTH_SHORT
                    ).show()
                    return false
                }
                return true
            }

            // Fungsi untuk mereset nilai pada kolom saat button simpan berhasil mengirimkan data
            fun resetFields() {
                sharedViewModelCustomer.resetSelectedCustomer()
                sharedViewModelBarang.resetSelectedBarang()
            }

            // Bagian Button
            Surface(
                modifier = Modifier.align(Alignment.BottomCenter),
                shadowElevation = 8.sdp // Menambahkan efek bayangan
            ) {
                Button(
                    onClick = {
                        if (isValidInput()) {
                            scope.launch(Dispatchers.Main) {
                                isDialog.value = true

                                val barangPeminjamanList =
                                    sharedViewModelBarang.selectedBarang
                                        // Filter barang yang dipilih dengan jumlah lebih dari 0
                                        .filter { (_, jumlah) -> jumlah > 0 }
                                        .map { entry ->
                                            val barangModel = entry.key
                                            val jumlah = entry.value.toString()

                                            // Pastikan barangModel tidak null sebelum mengakses propertinya
                                            BarangPeminjamanModel(
                                                barangId = barangModel.key!!, // Ambil ID barang dari barangModel
                                                imgBarangUrl = barangModel.item?.imgBarangUrl
                                                    ?: "", // Ambil imgBarangUrl dari barangModel
                                                namaBarang = barangModel.item?.namaBarang
                                                    ?: "", // Ambil namaBarang dari barangModel
                                                harga = barangModel.item?.harga
                                                    ?: "", // Ambil harga dari barangModel
                                                keterangan = barangModel.item?.keterangan
                                                    ?: "", // Ambil keterangan dari barangModel
                                                jumlahPinjam = jumlah // Ambil jumlah barang yang dipilih
                                            )
                                        }

                                val peminjamanModel = PeminjamanModel(
                                    /* Isi dengan Document ID default dari firestore
                                    (kode lengkap ada di PeminjamanDbRepositoryImpl bagian insert) */
                                    idPesanan = "",

                                    // Isi dengan nama customer yang dipilih
                                    namaPeminjam = selectedCustomer?.item?.namaCustomer ?: "",

                                    // Isi dengan tanggal yang dipilih
                                    tanggalPeminjaman = selectedDate?.let {
                                        // Ubah format tanggal menjadi "hari-bulan-tahun"
                                        val formatter = DateTimeFormatter.ofPattern(
                                            "dd MMMM yyyy",
                                            Locale.getDefault()
                                        )
                                        formatter.format(it)
                                    } ?: "",
                                    // Isi dengan nilai dari mutableState lamaSewa
                                    lamaPeminjaman = lamaSewa.value,

                                    // Isi dengan daftar barang yang telah dipilih
                                    barang = barangPeminjamanList
                                )

                                // Menambahkan data ke Firestore
                                viewModel.insert(peminjamanModel).collect {
                                    when (it) {
                                        is ResultState.Success -> {
                                            // Berpindah ke halaman sebelumnya
                                            navController.popBackStack()
                                            isDialog.value = false
                                            context.showMsg(it.data)
                                            viewModel.getItems()

                                            // Reset semua data kolom ke awal
                                            resetFields()
                                        }

                                        is ResultState.Failure -> {
                                            // Berpindah ke halaman sebelumnya
                                            navController.popBackStack()
                                            isDialog.value = false
                                            context.showMsg(it.msg.toString())
                                        }

                                        else -> {}
                                    }
                                }
                            }
                        }
                    },
                    contentPadding = PaddingValues(vertical = 12.sdp),
                    shape = RoundedCornerShape(10.sdp),
                    colors = ButtonDefaults.buttonColors(containerColor = blue),
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(white)
                        .padding(16.sdp)
                ) {
                    Text(
                        text = "Simpan",
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
fun EachRowShowBarang(
    itemsState: BarangModel,
    sharedViewModelBarang: SharedViewModelBarang
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

    // Mendapatkan jumlah barang yang dipilih dari sharedViewModelBarang
    val selectedBarang = sharedViewModelBarang.selectedBarang

    // Mendapatkan jumlah barang yang dipilih dari sharedViewModelBarang
    val jumlahBarangDipilih = selectedBarang[itemsState] ?: 0


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

                Text(
                    text = "$jumlahBarangDipilih",
                    modifier = Modifier.padding(start = 16.sdp),
                    style = TextStyle(
                        color = red,
                        fontSize = 16.ssp,
                        fontFamily = fontexo,
                        fontWeight = FontWeight.SemiBold,
                    )
                )
            }

        }
    }
}