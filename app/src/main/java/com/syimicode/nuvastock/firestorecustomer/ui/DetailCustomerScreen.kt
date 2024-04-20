package com.syimicode.nuvastock.firestorecustomer.ui

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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.syimicode.nuvastock.R
import com.syimicode.nuvastock.firestorecustomer.CustomerViewModel
import com.syimicode.nuvastock.screen.menu.Screens
import com.syimicode.nuvastock.ui.theme.fontexo
import com.syimicode.nuvastock.ui.theme.line
import com.syimicode.nuvastock.ui.theme.text_primary
import com.syimicode.nuvastock.ui.theme.text_secondary
import com.syimicode.nuvastock.ui.theme.white
import com.syimicode.nuvastock.utils.ResultState
import com.syimicode.nuvastock.utils.showMsg
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailCustomerScreen(
    navController: NavHostController,
    viewModel: CustomerViewModel = hiltViewModel(),
    customerId: String // Tambahkan parameter customerId
) {

    val res = viewModel.res.value

    // Gunakan customerId untuk mendapatkan detail customer dari Firestore
    val selectedCustomer = res.data.find { it.key == customerId }

    // State untuk menampilkan dialog delete
    val openDialogBox = remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val isDialog = remember { mutableStateOf(false) }

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
                    text = "Detail Customer",
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
                        navController.navigate("${Screens.UpdateCustomerScreen.name}/$customerId")
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
                    DeleteCustomerScreen(
                        openDialogBox = openDialogBox,
                        customerId = selectedCustomer?.item?.namaCustomer ?: "",
                        onDelete = {
                            scope.launch(Dispatchers.Main) {
                                // Panggil fungsi delete pada ViewModel saat penghapusan dikonfirmasi
                                selectedCustomer?.key?.let { key ->
                                    viewModel.delete(key).collect {
                                        when (it) {
                                            is ResultState.Success -> {
                                                // Berpindah ke halaman sebelumnya
                                                navController.navigateUp()
                                                isDialog.value = false
                                                context.showMsg(it.data)
                                            }

                                            is ResultState.Failure -> {
                                                // Berpindah ke halaman sebelumnya
                                                navController.popBackStack()
                                                isDialog.value = false
                                                context.showMsg(it.msg.toString())
                                            }

                                            ResultState.Loading -> {
                                                isDialog.value = true
                                            }
                                        }
                                    }
                                }
                            }
                        }
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
                        .width(222.sdp)
                ) {
                    if (selectedCustomer?.item?.imgKtpUrl != null) {
                        Image(
                            painter = rememberAsyncImagePainter(
                                selectedCustomer.item.imgKtpUrl ?: ""
                            ),
                            contentDescription = "detail gambar",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .align(Alignment.Center)
                                .width(200.sdp)
                                .height(120.sdp)
                                .clip(RoundedCornerShape(10.sdp))
                        )
                    } else {
                        // Menampilkan placeholder jika gambar belum muncul
                        Image(
                            painter = painterResource(id = R.drawable.upload_ktp),
                            contentDescription = "upload gambar",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .align(Alignment.Center)
                                .width(200.sdp)
                                .height(120.sdp)
                                .clip(RoundedCornerShape(10.sdp))
                        )
                    }
                }
            }


            // #2 COLUMN (Nama)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.sdp, start = 16.sdp, end = 16.sdp)
                    .background(white)
            ) {
                Text(
                    text = "Nama",
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
                value = selectedCustomer?.item?.namaCustomer ?: "",
                onValueChange = { },
                readOnly = true,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    containerColor = white,
                ),
                textStyle = TextStyle(
                    color = text_primary,
                    fontSize = 14.ssp,
                    fontFamily = fontexo,
                    fontWeight = FontWeight.Normal,
                )
            )


            // #3 COLUMN (Whatsapp)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.sdp, start = 16.sdp, end = 16.sdp)
                    .background(white)
            ) {
                Text(
                    text = "Whatsapp",
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
                value = selectedCustomer?.item?.whatsapp ?: "",
                onValueChange = { },
                readOnly = true,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    containerColor = white,
                ),
                textStyle = TextStyle(
                    color = text_primary,
                    fontSize = 14.ssp,
                    fontFamily = fontexo,
                    fontWeight = FontWeight.Normal,
                )
            )


            // #4 COLUMN (Alamat)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.sdp, start = 16.sdp, end = 16.sdp)
                    .background(white)
            ) {
                Text(
                    text = "Alamat",
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
                value = selectedCustomer?.item?.alamat ?: "",
                onValueChange = { },
                readOnly = true,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    containerColor = white,
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
//fun DetailCustomerScreenPreview() {
//    DetailCustomerScreen(
//        navController = rememberNavController(),
//        viewModel = FirestoreViewModel(
//            hiltViewModel()
//        )
//    )
//}