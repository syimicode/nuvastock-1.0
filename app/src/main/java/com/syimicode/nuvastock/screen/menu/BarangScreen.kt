package com.syimicode.nuvastock.screen.menu

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.syimicode.nuvastock.R
import com.syimicode.nuvastock.common.CommonDialog
import com.syimicode.nuvastock.firestorebarang.BarangModel
import com.syimicode.nuvastock.firestorebarang.BarangViewModel
import com.syimicode.nuvastock.ui.theme.bg_search
import com.syimicode.nuvastock.ui.theme.blue
import com.syimicode.nuvastock.ui.theme.fontexo
import com.syimicode.nuvastock.ui.theme.line
import com.syimicode.nuvastock.ui.theme.text_primary
import com.syimicode.nuvastock.ui.theme.text_secondary
import com.syimicode.nuvastock.ui.theme.white
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BarangScreen(
    navController: NavHostController,
    viewModel: BarangViewModel = hiltViewModel()
) {

    val res = viewModel.res.value

    // Menambahkan state untuk nilai pencarian
    val searchText = remember { mutableStateOf("") }

    // State untuk mengontrol visibilitas ikon tutup (trailingIcon)
    val isSearchTextNotEmpty = remember { mutableStateOf(false) }

    // Buat state untuk melacak posisi scroll pada LazyColumn
    val scrollState = rememberLazyListState()

    // Buat state untuk mengontrol visibilitas FloatingActionButton
    val isFabVisible = remember { mutableStateOf(true) }

    Scaffold(
        floatingActionButton = {
            // Tambahkan AnimatedVisibility untuk FAB
            AnimatedVisibility(
                visible = isFabVisible.value,
                enter = slideInVertically(initialOffsetY = { it * 1 }),
                exit = slideOutVertically(targetOffsetY = { it * 1 }),
            ) {
                FloatingActionButton(
                    onClick = {
                        navController.navigate(Screens.AddBarangScreen.name)
                    },
                    containerColor = blue,
                    shape = CircleShape,
                    elevation = FloatingActionButtonDefaults.elevation(2.sdp),
                    modifier = Modifier
                        .padding(end = 4.sdp, bottom = 4.sdp)
                        .size(60.sdp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_add),
                        contentDescription = "tambahBarang",
                        modifier = Modifier
                            .size(30.sdp),
                        tint = Color.White
                    )
                }
            }
        }, content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(white)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(white)
                        .padding(16.sdp)
                ) {
                    Text(
                        text = "Barang",
                        style = TextStyle(
                            color = text_primary,
                            fontSize = 20.ssp,
                            fontFamily = fontexo,
                            fontWeight = FontWeight.SemiBold,
                        )
                    )
                }

                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(white)
                        .padding(horizontal = 16.sdp),
                    value = searchText.value,
                    onValueChange = { newText ->
                        searchText.value = newText

                        // Panggil fungsi pencarian dari BarangViewModel
                        viewModel.searchBarang(newText)

                        // Update state untuk mengontrol visibilitas ikon tutup
                        isSearchTextNotEmpty.value = newText.isNotBlank()
                    },
                    shape = RoundedCornerShape(10.sdp),
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_search),
                            contentDescription = "ic_search",
                            modifier = Modifier
                                .padding(start = 16.sdp, end = 12.sdp)
                                .size(20.sdp),
                            tint = Color.Unspecified,
                        )
                    },
                    trailingIcon = {
                        if (isSearchTextNotEmpty.value) {
                            IconButton(
                                modifier = Modifier
                                    .padding(end = 16.sdp)
                                    .size(20.sdp),
                                onClick = {
                                    searchText.value = "" // Mengosongkan nilai searchText

                                    // Update state untuk mengontrol visibilitas ikon tutup
                                    isSearchTextNotEmpty.value = false

                                    // Setel ulang data ke data asli saat tombol tutup diklik
                                    viewModel.setItems()
                                }
                            ) {

                                Icon(
                                    painter = painterResource(id = R.drawable.ic_close),
                                    contentDescription = "ic_close",
                                    modifier = Modifier
                                        .size(20.sdp),
                                    tint = Color.Unspecified
                                )
                            }
                        }
                    },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
                        containerColor = bg_search,
                        cursorColor = blue,
                    ),
                    placeholder = {
                        Text(
                            text = "Cari nama barang..",
                            style = TextStyle(
                                color = text_secondary,
                                fontSize = 14.ssp,
                                fontFamily = fontexo,
                                fontWeight = FontWeight.Normal,
                            )
                        )
                    },
                    textStyle = TextStyle(
                        color = text_primary,
                        fontSize = 14.ssp,
                        fontFamily = fontexo,
                        fontWeight = FontWeight.Normal,
                    )
                )

                Spacer(modifier = Modifier.height(12.sdp))

                Divider(
                    color = line,
                    thickness = 1.sdp,
                    modifier = Modifier
                        .fillMaxWidth()
                )

                // Atur visibilitas FloatingActionButton berdasarkan perubahan posisi scroll
                LaunchedEffect(scrollState) {
                    snapshotFlow { scrollState.firstVisibleItemIndex }
                        .collect { firstVisibleItem ->
                            val isScrolling = firstVisibleItem > 0
                            isFabVisible.value = !isScrolling
                        }
                }

                if (res.data.isNotEmpty()) {
                    // Urutkan isi LazyColumn berasarkan nama
                    val sortedData = res.data.sortedBy { it.item?.namaBarang?.lowercase() }

                    LazyColumn(
                        state = scrollState,
                        content = {
                            items(
                                sortedData,
                                key = { it.key ?: "" }
                            ) { items ->
                                EachRow(
                                    navController = navController,
                                    itemsState = items
                                )
                            }
                        }
                    )
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
    )

}


@Composable
fun EachRow(
    navController: NavHostController,
    itemsState: BarangModel
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

    // Tampilkan keterangan dan simbol | jika keterangan tidak kosong
    val keteranganText = if (trimmedKeterangan.isNotEmpty()) {
        "| $trimmedKeterangan"
    } else {
        "" // Kosongkan keterangan jika keterangan kosong setelah di-trim
    }


    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(white)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = rememberRipple(bounded = true)
                ) {
                    navController.navigate("${Screens.DetailBarangScreen.name}/${itemsState.key}")
                }
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
                        .size(50.sdp)
                        .clip(RoundedCornerShape(10.sdp))
                )

                Spacer(modifier = Modifier.width(20.sdp))

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
                            fontSize = 14.ssp,
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
                            fontSize = 12.ssp,
                            fontFamily = fontexo,
                            fontWeight = FontWeight.Normal,
                        ),
                        maxLines = 1, // Hanya satu baris
                        // Tambahkan ellipsis jika teks melebihi batas
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Text(
                    text = itemsState.item?.jumlah!!,
                    modifier = Modifier.padding(start = 16.sdp),
                    style = TextStyle(
                        color = blue,
                        fontSize = 16.ssp,
                        fontFamily = fontexo,
                        fontWeight = FontWeight.SemiBold,
                    )
                )
            }

        }
    }
}


@Preview
@Composable
fun BarangScreenPreview() {
    BarangScreen(navController = rememberNavController())
}