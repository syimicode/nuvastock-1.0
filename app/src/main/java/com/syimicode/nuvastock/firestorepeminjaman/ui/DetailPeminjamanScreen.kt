package com.syimicode.nuvastock.firestorepeminjaman.ui

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ExperimentalComposeApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.syimicode.nuvastock.R
import com.syimicode.nuvastock.common.CommonDialog
import com.syimicode.nuvastock.firestorepeminjaman.BarangPeminjamanModel
import com.syimicode.nuvastock.firestorepeminjaman.PeminjamanViewModel
import com.syimicode.nuvastock.ui.theme.bg_invoice
import com.syimicode.nuvastock.ui.theme.blue
import com.syimicode.nuvastock.ui.theme.fontexo
import com.syimicode.nuvastock.ui.theme.line
import com.syimicode.nuvastock.ui.theme.red
import com.syimicode.nuvastock.ui.theme.text_primary
import com.syimicode.nuvastock.ui.theme.text_secondary
import com.syimicode.nuvastock.ui.theme.white
import dev.shreyaspatil.capturable.capturable
import dev.shreyaspatil.capturable.controller.rememberCaptureController
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalComposeApi::class, ExperimentalComposeUiApi::class)
@Composable
fun DetailPeminjamanScreen(
    navController: NavHostController,
    viewModel: PeminjamanViewModel = hiltViewModel(),
    pesananId: String // Tambahkan parameter pesananId
) {
    val res = viewModel.res.value

    // Gunakan pesananId untuk mendapatkan detail peminjaman dari Firestore
    val selectedPeminjaman = res.data.find { it.idPesanan == pesananId }

    // Menghitung total biaya peminjaman
    val totalBiaya = selectedPeminjaman?.let { peminjaman ->
        peminjaman.barang.sumOf { barang ->
            // Fungs replace untuk menghapus tulisan "Rp" pada harga barang
            val harga = barang.harga?.replace("[^\\d]".toRegex(), "")?.toInt() ?: 0
            val jumlah = barang.jumlahPinjam?.toInt() ?: 0
            val lamaPeminjaman = peminjaman.lamaPeminjaman?.toInt() ?: 1
            harga * jumlah * lamaPeminjaman
        }
    } ?: 0

    // Ubah format angka totalBiaya menjadi format Rupiah (menambahkan titik sebagai pemisah ribuan)
    val formattedTotalBiaya = NumberFormat.getNumberInstance(Locale("id", "ID")).format(totalBiaya)

    val captureController = rememberCaptureController()
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    // State to control whether detail is expanded or collapsed
    var isDetailExpanded by remember { mutableStateOf(false) }

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
                    text = "Detail Peminjaman",
                    style = TextStyle(
                        color = text_primary,
                        fontSize = 16.ssp,
                        fontFamily = fontexo,
                        fontWeight = FontWeight.SemiBold,
                    )
                )

                Spacer(modifier = Modifier.weight(1f))

                IconButton(
                    modifier = Modifier.size(24.sdp),
                    onClick = {
                        coroutineScope.launch {
                            val bitmapAsync = captureController.captureAsync()
                            try {
                                val imageBitmap = bitmapAsync.await()
                                val bitmap = imageBitmap.asAndroidBitmap()
                                saveBitmapToStorage(context, bitmap)
                            } catch (error: Throwable) {
                                if (ContextCompat.checkSelfPermission(
                                        context,
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                                    )
                                    != PackageManager.PERMISSION_GRANTED
                                ) {
                                    // Jika izin belum diberikan, minta izin secara dinamis.
                                    ActivityCompat.requestPermissions(
                                        context as Activity,
                                        arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                                        REQUEST_WRITE_EXTERNAL_STORAGE
                                    )
                                    return@launch
                                }

                                Log.e(
                                    "DetailPeminjamanScreen",
                                    "Error saving bitmap: ${error.message}",
                                    error
                                )
                            }
                        }
                    }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_download),
                        contentDescription = "ic_download",
                        modifier = Modifier.size(24.sdp),
                        tint = Color.Unspecified
                    )
                }
            }

            Divider(
                color = line,
                thickness = 1.sdp,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
            )
        }

        // START EXPORT FORMAT JPG
        LazyColumn(
            modifier = Modifier
                .capturable(captureController)
                .fillMaxSize()
                .background(white),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Spacer(modifier = Modifier.height(24.sdp))

                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(100.sdp))
                        .background(blue)
                        .padding(horizontal = 12.sdp, vertical = 12.sdp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_invoice),
                        contentDescription = "ic_invoice",
                        modifier = Modifier.size(20.sdp),
                        tint = Color.Unspecified
                    )
                }

                Spacer(modifier = Modifier.height(8.sdp))

                Text(
                    text = "PT. Nuva Creative",
                    style = TextStyle(
                        color = text_primary,
                        fontSize = 20.ssp,
                        fontFamily = fontexo,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center
                    )
                )

                Text(
                    text = "ID: ${selectedPeminjaman?.idPesanan ?: ""}",
                    style = TextStyle(
                        color = text_secondary,
                        fontSize = 12.ssp,
                        fontFamily = fontexo,
                        fontWeight = FontWeight.Normal,
                        textAlign = TextAlign.Center
                    )
                )

                Spacer(modifier = Modifier.height(16.sdp))

                Column(
                    modifier = Modifier
                        .padding(horizontal = 16.sdp)
                        .clip(RoundedCornerShape(10.sdp))
                        .background(bg_invoice),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Spacer(modifier = Modifier.height(16.sdp))

                    Text(
                        text = "Total Biaya",
                        style = TextStyle(
                            color = text_secondary,
                            fontSize = 12.ssp,
                            fontFamily = fontexo,
                            fontWeight = FontWeight.Normal,
                            textAlign = TextAlign.Center
                        )
                    )

                    Spacer(modifier = Modifier.height(4.sdp))

                    Text(
                        text = "Rp $formattedTotalBiaya",
                        style = TextStyle(
                            color = blue,
                            fontSize = 20.ssp,
                            fontFamily = fontexo,
                            fontWeight = FontWeight.SemiBold,
                            textAlign = TextAlign.Center
                        )
                    )

                    Spacer(modifier = Modifier.height(24.sdp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.sdp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Tanggal",
                            style = TextStyle(
                                color = text_secondary,
                                fontSize = 12.ssp,
                                fontFamily = fontexo,
                                fontWeight = FontWeight.Normal,
                                textAlign = TextAlign.Left
                            )
                        )

                        Text(
                            text = selectedPeminjaman?.tanggalPeminjaman ?: "",
                            style = TextStyle(
                                color = text_primary,
                                fontSize = 12.ssp,
                                fontFamily = fontexo,
                                fontWeight = FontWeight.Medium,
                                textAlign = TextAlign.Right
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(8.sdp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.sdp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Customer",
                            style = TextStyle(
                                color = text_secondary,
                                fontSize = 12.ssp,
                                fontFamily = fontexo,
                                fontWeight = FontWeight.Normal,
                                textAlign = TextAlign.Left
                            )
                        )

                        Text(
                            text = selectedPeminjaman?.namaPeminjam ?: "",
                            style = TextStyle(
                                color = text_primary,
                                fontSize = 12.ssp,
                                fontFamily = fontexo,
                                fontWeight = FontWeight.Medium,
                                textAlign = TextAlign.Right
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(16.sdp))
                }


                Spacer(modifier = Modifier.height(24.sdp))

                Divider(
                    color = line,
                    thickness = 1.sdp,
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.sdp, end = 16.sdp, top = 16.sdp, bottom = 8.sdp)
                        .background(white),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Detail Peminjaman",
                        style = TextStyle(
                            color = text_primary,
                            fontSize = 14.ssp,
                            fontFamily = fontexo,
                            fontWeight = FontWeight.Medium
                        )
                    )

                    IconButton(
                        modifier = Modifier.size(20.sdp),
                        onClick = {
                            isDetailExpanded = !isDetailExpanded // Toggle detail expansion
                        }
                    ) {
                        Icon(
                            painter = painterResource(id = if (isDetailExpanded) R.drawable.ic_bottom else R.drawable.ic_top),
                            contentDescription = if (isDetailExpanded) "Collapse detail" else "Expand detail",
                            modifier = Modifier.size(20.sdp),
                            tint = Color.Unspecified
                        )
                    }
                }
            }

            // Tampilkan Detail Barang yang dipinjam
            if (isDetailExpanded) {
                if (selectedPeminjaman != null && selectedPeminjaman.barang.isNotEmpty()) {
                    items(
                        items = selectedPeminjaman.barang,
                        key = { it.barangId ?: "" }
                    ) { items ->
                        EachRowShowDetailBarang(
                            barangPeminjaman = items
                        )

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
            }


            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.sdp, vertical = 8.sdp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Lama Sewa",
                        style = TextStyle(
                            color = text_secondary,
                            fontSize = 14.ssp,
                            fontFamily = fontexo,
                            fontWeight = FontWeight.Normal,
                            textAlign = TextAlign.Left
                        )
                    )

                    Text(
                        text = "${selectedPeminjaman?.lamaPeminjaman ?: ""} Hari",
                        style = TextStyle(
                            color = red,
                            fontSize = 14.ssp,
                            fontFamily = fontexo,
                            fontWeight = FontWeight.SemiBold,
                            textAlign = TextAlign.Right
                        )
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.sdp, vertical = 8.sdp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Total Biaya",
                        style = TextStyle(
                            color = text_secondary,
                            fontSize = 14.ssp,
                            fontFamily = fontexo,
                            fontWeight = FontWeight.Normal,
                            textAlign = TextAlign.Left
                        )
                    )

                    Text(
                        text = "Rp $formattedTotalBiaya",
                        style = TextStyle(
                            color = text_primary,
                            fontSize = 14.ssp,
                            fontFamily = fontexo,
                            fontWeight = FontWeight.SemiBold,
                            textAlign = TextAlign.Right
                        )
                    )
                }

                Spacer(modifier = Modifier.height(24.sdp))

                // END EXPORT FORMAT JPG

                // Tampilkan tombol "Sudah Dikembalikan" hanya jika belum dikembalikan
                if (selectedPeminjaman != null && !selectedPeminjaman.sudahDikembalikan) {
                    Button(
                        onClick = {
                            // Memperbarui status peminjaman menjadi "sudah dikembalikan"
                            if (selectedPeminjaman != null) {
                                viewModel.updateStatusBarang(selectedPeminjaman)
                            }
                            navController.navigateUp()
                        },
                        contentPadding = PaddingValues(vertical = 12.sdp),
                        shape = RoundedCornerShape(10.sdp),
                        colors = ButtonDefaults.buttonColors(containerColor = red),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.sdp, end = 16.sdp, bottom = 16.sdp)
                    ) {
                        Text(
                            text = "Barang Dikembalikan",
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
    }
}


// Pastikan untuk menambahkan konstanta untuk kode permintaan izin.
private const val REQUEST_WRITE_EXTERNAL_STORAGE = 123
private fun saveBitmapToStorage(context: Context, bitmap: Bitmap) {
    val resolver = context.contentResolver
    val contentValues = ContentValues().apply {
        put(MediaStore.Images.Media.DISPLAY_NAME, "invoice_${System.currentTimeMillis()}.jpg")
        put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
    }

    val imageUri: Uri? =
        resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
    imageUri?.let { uri ->
        resolver.openOutputStream(uri)?.use { outputStream ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            Toast.makeText(context, "Invoice berhasil disimpan", Toast.LENGTH_LONG).show()
        }
    } ?: run {
        Toast.makeText(context, "Gagal menyimpan invoice", Toast.LENGTH_LONG).show()
    }
}


@Composable
fun EachRowShowDetailBarang(
    barangPeminjaman: BarangPeminjamanModel
) {
    // Mengambil keterangan, jika null maka diubah menjadi string kosong
    val keterangan = barangPeminjaman.keterangan?.trim() ?: ""

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
    val jumlahBarangDipilih = barangPeminjaman.jumlahPinjam?.toIntOrNull() ?: 0


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
                        .data(barangPeminjaman.imgBarangUrl ?: "")
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
                        text = if ((barangPeminjaman.namaBarang?.length ?: 0) > 21) {
                            "${barangPeminjaman.namaBarang?.substring(0, 21)}..."
                        } else {
                            barangPeminjaman.namaBarang ?: ""
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
                        text = "${barangPeminjaman.harga} $keteranganText",
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