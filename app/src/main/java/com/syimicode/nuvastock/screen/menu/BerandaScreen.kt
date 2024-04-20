package com.syimicode.nuvastock.screen.menu

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.syimicode.nuvastock.R
import com.syimicode.nuvastock.firestorebarang.BarangViewModel
import com.syimicode.nuvastock.firestorecustomer.CustomerViewModel
import com.syimicode.nuvastock.firestorepeminjaman.PeminjamanViewModel
import com.syimicode.nuvastock.ui.theme.blue
import com.syimicode.nuvastock.ui.theme.fontexo
import com.syimicode.nuvastock.ui.theme.line
import com.syimicode.nuvastock.ui.theme.text_primary
import com.syimicode.nuvastock.ui.theme.white
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun BerandaScreen(navController: NavHostController) {

    val todayDate = LocalDate.now()
    val formatTanggal = todayDate.format(DateTimeFormatter.ofPattern("dd MMM yyyy"))

    val viewModel1: CustomerViewModel = hiltViewModel()
    val customerCount by viewModel1.customerCount.collectAsState()

    val viewModel2: BarangViewModel = hiltViewModel()
    val barangCount by viewModel2.barangCount.collectAsState()

    val viewModel3: PeminjamanViewModel = hiltViewModel()
    val riwayatPinjamCount by viewModel3.riwayatPinjamCount.collectAsState()

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
                text = "Beranda",
                style = TextStyle(
                    color = text_primary,
                    fontSize = 20.ssp,
                    fontFamily = fontexo,
                    fontWeight = FontWeight.SemiBold,
                )
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                // Tambahkan verticalScroll agar konten dapat di-scroll
                .verticalScroll(rememberScrollState())
                .background(white)
        ) {

            // Container Blue
            Column(
                modifier = Modifier
                    .background(white)
                    .padding(horizontal = 16.sdp)
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = blue,
                            shape = RoundedCornerShape(10.sdp)
                        )
                        .padding(horizontal = 20.sdp)
                ) {
                    // #1 Row
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.sdp)
                    ) {
                        Text(
                            text = "Today",
                            modifier = Modifier
                                .padding(end = 8.sdp),
                            style = TextStyle(
                                color = white,
                                fontSize = 16.ssp,
                                fontFamily = fontexo,
                                fontWeight = FontWeight.SemiBold,
                            )
                        )

                        Text(
                            text = formatTanggal,
                            style = TextStyle(
                                color = line,
                                fontSize = 16.ssp,
                                fontFamily = fontexo,
                                fontWeight = FontWeight.Normal,
                            )
                        )
                    }

                    // #2 Row
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 20.sdp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // #1 (Total Barang)
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "$barangCount",
                                modifier = Modifier
                                    .padding(bottom = 4.sdp),
                                style = TextStyle(
                                    color = white,
                                    fontSize = 20.ssp,
                                    fontFamily = fontexo,
                                    fontWeight = FontWeight.SemiBold,
                                    textAlign = TextAlign.Center
                                )
                            )

                            Text(
                                text = "Barang",
                                style = TextStyle(
                                    color = line,
                                    fontSize = 12.ssp,
                                    fontFamily = fontexo,
                                    fontWeight = FontWeight.Normal,
                                )
                            )
                        }

                        Divider(
                            color = line,
                            modifier = Modifier
                                .height(32.sdp)
                                .width(1.sdp)
                        )

                        // #2 (Riwayat Pinjam)
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "$riwayatPinjamCount",
                                modifier = Modifier
                                    .padding(bottom = 4.sdp),
                                style = TextStyle(
                                    color = white,
                                    fontSize = 20.ssp,
                                    fontFamily = fontexo,
                                    fontWeight = FontWeight.SemiBold,
                                    textAlign = TextAlign.Center
                                )
                            )

                            Text(
                                text = "Peminjaman",
                                style = TextStyle(
                                    color = line,
                                    fontSize = 12.ssp,
                                    fontFamily = fontexo,
                                    fontWeight = FontWeight.Normal,
                                )
                            )
                        }

                        Divider(
                            color = line,
                            modifier = Modifier
                                .height(32.sdp)
                                .width(1.sdp)
                        )

                        // #3 (Total Customer)
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "$customerCount",
                                modifier = Modifier
                                    .padding(bottom = 4.sdp),
                                style = TextStyle(
                                    color = white,
                                    fontSize = 20.ssp,
                                    fontFamily = fontexo,
                                    fontWeight = FontWeight.SemiBold,
                                    textAlign = TextAlign.Center
                                )
                            )

                            Text(
                                text = "Customer",
                                style = TextStyle(
                                    color = line,
                                    fontSize = 12.ssp,
                                    fontFamily = fontexo,
                                    fontWeight = FontWeight.Normal,
                                )
                            )
                        }

                    }
                }

            }


            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(white)
                    .padding(
                        horizontal = 16.sdp,
                        vertical = 24.sdp
                    )
            ) {

                // #1 Tambah Barang
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.sdp, line, RoundedCornerShape(10.sdp))
                        .background(white)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = rememberRipple(bounded = true)
                        ) {
                            navController.navigate(Screens.AddBarangScreen.name)
                        }
                        .padding(20.sdp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Row(
                        modifier = Modifier
                            .background(white),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Icon(
                            painter = painterResource(id = R.drawable.ic_camera),
                            contentDescription = "ic_camera",
                            modifier = Modifier
                                .padding(end = 16.sdp)
                                .size(24.sdp),
                            tint = Color.Unspecified,
                        )

                        Text(
                            text = "Tambah Barang",
                            style = TextStyle(
                                color = text_primary,
                                fontSize = 16.ssp,
                                fontFamily = fontexo,
                                fontWeight = FontWeight.Normal
                            )
                        )
                    }

                    Icon(
                        painter = painterResource(id = R.drawable.ic_right),
                        contentDescription = "ic_right",
                        modifier = Modifier
                            .size(24.sdp),
                        tint = Color.Unspecified,
                    )
                }

                Spacer(modifier = Modifier.height(16.sdp))

                // #2 Tambah Peminjaman
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.sdp, line, RoundedCornerShape(10.sdp))
                        .background(white)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = rememberRipple(bounded = true)
                        ) {
                            navController.navigate(Screens.AddPeminjamanScreen.name)
                        }
                        .padding(20.sdp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Row(
                        modifier = Modifier
                            .background(white),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Icon(
                            painter = painterResource(id = R.drawable.ic_peminjaman_filled),
                            contentDescription = "ic_peminjaman",
                            modifier = Modifier
                                .padding(end = 16.sdp)
                                .size(24.sdp),
                            tint = Color.Unspecified,
                        )

                        Text(
                            text = "Tambah Peminjaman",
                            style = TextStyle(
                                color = text_primary,
                                fontSize = 16.ssp,
                                fontFamily = fontexo,
                                fontWeight = FontWeight.Normal
                            )
                        )
                    }

                    Icon(
                        painter = painterResource(id = R.drawable.ic_right),
                        contentDescription = "ic_right",
                        modifier = Modifier
                            .size(24.sdp),
                        tint = Color.Unspecified,
                    )
                }

                Spacer(modifier = Modifier.height(16.sdp))

                // #3 Tambah Customer
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.sdp, line, RoundedCornerShape(10.sdp))
                        .background(white)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = rememberRipple(bounded = true)
                        ) {
                            navController.navigate(Screens.AddCustomerScreen.name)
                        }
                        .padding(20.sdp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Row(
                        modifier = Modifier
                            .background(white),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Icon(
                            painter = painterResource(id = R.drawable.ic_customer),
                            contentDescription = "ic_customer",
                            modifier = Modifier
                                .padding(end = 16.sdp)
                                .size(24.sdp),
                            tint = Color.Unspecified,
                        )

                        Text(
                            text = "Tambah Customer",
                            style = TextStyle(
                                color = text_primary,
                                fontSize = 16.ssp,
                                fontFamily = fontexo,
                                fontWeight = FontWeight.Normal
                            )
                        )
                    }

                    Icon(
                        painter = painterResource(id = R.drawable.ic_right),
                        contentDescription = "ic_right",
                        modifier = Modifier
                            .size(24.sdp),
                        tint = Color.Unspecified,
                    )
                }
            }

        }
    }
}


@Preview
@Composable
fun BerandaScreenPreview() {
    BerandaScreen(navController = rememberNavController())
}