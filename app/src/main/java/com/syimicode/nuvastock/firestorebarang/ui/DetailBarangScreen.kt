package com.syimicode.nuvastock.firestorebarang.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.syimicode.nuvastock.R
import com.syimicode.nuvastock.firestorebarang.BarangViewModel
import com.syimicode.nuvastock.screen.menu.Screens
import com.syimicode.nuvastock.ui.theme.fontexo
import com.syimicode.nuvastock.ui.theme.line
import com.syimicode.nuvastock.ui.theme.text_primary
import com.syimicode.nuvastock.ui.theme.text_secondary
import com.syimicode.nuvastock.ui.theme.white
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailBarangScreen(
    navController: NavHostController,
    viewModel: BarangViewModel = hiltViewModel(),
    barangId: String // Tambahkan parameter barangId
) {

    val res = viewModel.res.value

    // Gunakan barangId untuk mendapatkan detail barang dari Firestore
    val selectedBarang = res.data.find { it.key == barangId }

    // State untuk menampilkan dialog delete
    val openDialogBox = remember { mutableStateOf(false) }

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
                    text = "Detail Barang",
                    style = TextStyle(
                        color = text_primary,
                        fontSize = 16.ssp,
                        fontFamily = fontexo,
                        fontWeight = FontWeight.SemiBold,
                    )
                )

                Spacer(modifier = Modifier.weight(1f))

                IconButton(
                    modifier = Modifier
                        .padding(end = 24.sdp)
                        .size(24.sdp),
                    onClick = {
                        navController.navigate("${Screens.UpdateBarangScreen.name}/$barangId")
                    }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_edit),
                        contentDescription = "ic_edit",
                        modifier = Modifier
                            .size(24.sdp),
                        tint = Color.Unspecified
                    )
                }

                if (openDialogBox.value) {
                    // Kirim openDialogBox ke DeleteCustomerScreen
                    DeleteBarangScreen(
                        openDialogBox = openDialogBox,
                        barangId = selectedBarang?.item?.namaBarang ?: "",
                    )
                }

                IconButton(
                    modifier = Modifier
                        .size(24.sdp),
                    onClick = {
                        // Panggil fungsi untuk menampilkan dialog saat IconButton "delete" diklik
                        openDialogBox.value = true
                    }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_delete),
                        contentDescription = "ic_delete",
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

        Column(
            modifier = Modifier
                .fillMaxSize()
                // Tambahkan verticalScroll agar konten dapat di-scroll
                .verticalScroll(rememberScrollState())
                .background(white)
        ) {

            // #1 COLUMN (Upload Gambar)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(white),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .background(white)
                        .padding(top = 16.sdp, start = 16.sdp, end = 16.sdp)
                        .width(142.sdp)
                ) {
                    if (selectedBarang?.item?.imgBarangUrl != null) {
                        Image(
                            painter = rememberAsyncImagePainter(
                                selectedBarang.item.imgBarangUrl ?: ""
                            ),
                            contentDescription = "detail gambar",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .align(Alignment.Center)
                                .size(120.sdp)
                                .clip(RoundedCornerShape(10.sdp))
                        )
                    } else {
                        // Menampilkan placeholder jika gambar belum muncul
                        Image(
                            painter = painterResource(id = R.drawable.upload_img),
                            contentDescription = "upload gambar",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .align(Alignment.Center)
                                .size(120.sdp)
                                .clip(RoundedCornerShape(10.sdp))
                        )
                    }
                }
            }


            // #2 COLUMN (Nama Barang)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.sdp, start = 16.sdp, end = 16.sdp)
                    .background(white)
            ) {
                Text(
                    text = "Nama Barang",
                    style = TextStyle(
                        color = text_secondary,
                        fontSize = 12.ssp,
                        fontFamily = fontexo,
                        fontWeight = FontWeight.Normal,
                    )
                )
            }

            Spacer(modifier = Modifier.height(4.sdp))

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(white)
                    .padding(horizontal = 16.sdp)
                    .border(1.sdp, line, RoundedCornerShape(5.sdp)),
                value = selectedBarang?.item?.namaBarang ?: "",
                onValueChange = { },
                readOnly = true,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    containerColor = white
                ),
                textStyle = TextStyle(
                    color = text_primary,
                    fontSize = 14.ssp,
                    fontFamily = fontexo,
                    fontWeight = FontWeight.Normal,
                )
            )


            // #3 COLUMN (Harga)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.sdp, start = 16.sdp, end = 16.sdp)
                    .background(white)
            ) {
                Text(
                    text = "Harga",
                    style = TextStyle(
                        color = text_secondary,
                        fontSize = 12.ssp,
                        fontFamily = fontexo,
                        fontWeight = FontWeight.Normal,
                    )
                )
            }

            Spacer(modifier = Modifier.height(4.sdp))

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(white)
                    .padding(horizontal = 16.sdp)
                    .border(1.sdp, line, RoundedCornerShape(5.sdp)),
                value = selectedBarang?.item?.harga ?: "",
                onValueChange = { },
                readOnly = true,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    containerColor = white
                ),
                textStyle = TextStyle(
                    color = text_primary,
                    fontSize = 14.ssp,
                    fontFamily = fontexo,
                    fontWeight = FontWeight.Normal,
                )
            )


            // #4 COLUMN (Jumlah)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.sdp, start = 16.sdp, end = 16.sdp)
                    .background(white)
            ) {
                Text(
                    text = "Jumlah",
                    style = TextStyle(
                        color = text_secondary,
                        fontSize = 12.ssp,
                        fontFamily = fontexo,
                        fontWeight = FontWeight.Normal,
                    )
                )
            }

            Spacer(modifier = Modifier.height(4.sdp))

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(white)
                    .padding(horizontal = 16.sdp)
                    .border(1.sdp, line, RoundedCornerShape(5.sdp)),
                value = selectedBarang?.item?.jumlah ?: "",
                onValueChange = { },
                readOnly = true,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    containerColor = white
                ),
                textStyle = TextStyle(
                    color = text_primary,
                    fontSize = 14.ssp,
                    fontFamily = fontexo,
                    fontWeight = FontWeight.Normal,
                )
            )


            // #5 COLUMN (Keterangan)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.sdp, start = 16.sdp, end = 16.sdp)
                    .background(white)
            ) {
                Text(
                    text = "Keterangan (opsional)",
                    style = TextStyle(
                        color = text_secondary,
                        fontSize = 12.ssp,
                        fontFamily = fontexo,
                        fontWeight = FontWeight.Normal,
                    )
                )
            }

            Spacer(modifier = Modifier.height(4.sdp))

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(white)
                    .padding(horizontal = 16.sdp)
                    .border(1.sdp, line, RoundedCornerShape(5.sdp)),
                value = selectedBarang?.item?.keterangan ?: "",
                onValueChange = { },
                readOnly = true,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    containerColor = white
                ),
                textStyle = TextStyle(
                    color = text_primary,
                    fontSize = 14.ssp,
                    fontFamily = fontexo,
                    fontWeight = FontWeight.Normal,
                )
            )

            Spacer(modifier = Modifier.height(40.sdp))

        }
    }
}


//@Preview
//@Composable
//fun DetailBarangPreview() {
//    DetailBarangScreen()
//}