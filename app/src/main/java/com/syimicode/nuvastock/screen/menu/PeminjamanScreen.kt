package com.syimicode.nuvastock.screen.menu

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.syimicode.nuvastock.R
import com.syimicode.nuvastock.common.CommonDialog
import com.syimicode.nuvastock.firestorebarang.BarangModel
import com.syimicode.nuvastock.firestorepeminjaman.PeminjamanModel
import com.syimicode.nuvastock.firestorepeminjaman.PeminjamanViewModel
import com.syimicode.nuvastock.ui.theme.fontexo
import com.syimicode.nuvastock.ui.theme.green
import com.syimicode.nuvastock.ui.theme.light_green
import com.syimicode.nuvastock.ui.theme.line
import com.syimicode.nuvastock.ui.theme.red
import com.syimicode.nuvastock.ui.theme.text_primary
import com.syimicode.nuvastock.ui.theme.text_secondary
import com.syimicode.nuvastock.ui.theme.white
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp

@Composable
fun PeminjamanScreen(
    navController: NavHostController,
    viewModel: PeminjamanViewModel = hiltViewModel()
) {
    val res = viewModel.res.value

    // Buat state untuk melacak posisi scroll pada LazyColumn
    val scrollState = rememberLazyListState()

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
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Peminjaman",
                    style = TextStyle(
                        color = text_primary,
                        fontSize = 20.ssp,
                        fontFamily = fontexo,
                        fontWeight = FontWeight.SemiBold,
                    )
                )

                IconButton(
                    modifier = Modifier
                        .size(24.sdp)
                        .background(white),
                    onClick = {
                        navController.navigate(Screens.AddPeminjamanScreen.name)
                    }
                ) {

                    Icon(
                        painter = painterResource(id = R.drawable.ic_add),
                        contentDescription = "ic_add",
                        modifier = Modifier
                            .size(24.sdp),
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


        // Bagian Daftar Peminjaman
        if (res.data.isNotEmpty()) {
            // Kelompokkan data peminjaman berdasarkan tanggal peminjaman
            val groupedData = res.data.groupBy { it.tanggalPeminjaman }

            LazyColumn(
                state = scrollState,
                content = {
                    groupedData.forEach { date, items ->
                        // Tampilkan tanggal peminjaman sebagai bagian dari LazyColumn
                        item {
                            Text(
                                text = date!!,
                                style = TextStyle(
                                    color = text_secondary,
                                    fontSize = 12.ssp,
                                    fontFamily = fontexo,
                                    fontWeight = FontWeight.Normal,
                                ),
                                modifier = Modifier.padding(start = 16.sdp, top = 16.sdp)
                            )
                        }

                        // Tampilkan item peminjaman yang terkait dengan tanggal
                        items(items) { peminjaman ->
                            EachRow(
                                navController = navController,
                                itemsState = peminjaman
                            )
                        }
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


@Composable
fun EachRow(
    navController: NavHostController,
    itemsState: PeminjamanModel
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(white)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(bounded = true)
            ) {
                navController.navigate(
                    "${Screens.DetailPeminjamanScreen.name}/${itemsState.idPesanan}"
                )
            }
            .padding(horizontal = 16.sdp, vertical = 24.sdp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_barang_dipinjam),
                    contentDescription = "ic_barang_dipinjam",
                    modifier = Modifier.size(24.sdp),
                    tint = Color.Unspecified,
                )

                Spacer(modifier = Modifier.width(20.sdp))

                Text(
                    text = "Barang Dipinjam",
                    style = TextStyle(
                        color = text_primary,
                        fontSize = 16.ssp,
                        fontFamily = fontexo,
                        fontWeight = FontWeight.SemiBold,
                    )
                )
            }

            // Menghitung jumlah total barang yang dipinjam
            val totalBarangDipinjam =
                itemsState.barang.sumOf { it.jumlahPinjam?.toIntOrNull() ?: 0 }

            // Menggabungkan teks jumlah barang dengan nama peminjam
            val textToShow = "$totalBarangDipinjam barang | ${itemsState.namaPeminjam}"

            Text(
                text = textToShow,
                modifier = Modifier.padding(start = 44.sdp, top = 8.sdp),
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
        }

        // Tampilkan teks "Selesai" jika sudahDikembalikan bernilai true
        if (itemsState.sudahDikembalikan) {
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.sdp))
                    .background(light_green)
                    .padding(horizontal = 8.sdp, vertical = 4.sdp)
            ) {
                Text(
                    text = "Selesai",
                    style = TextStyle(
                        color = green,
                        fontSize = 10.ssp,
                        fontFamily = fontexo,
                        fontWeight = FontWeight.Normal,
                    )
                )
            }
        }
    }
}


@Preview
@Composable
fun PeminjamanScreenPreview() {
    PeminjamanScreen(navController = rememberNavController())
}