package com.syimicode.nuvastock.firestorecustomer.ui

import android.text.TextUtils
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.core.text.isDigitsOnly
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.syimicode.nuvastock.R
import com.syimicode.nuvastock.common.CommonDialog
import com.syimicode.nuvastock.firestorebarang.BarangModel
import com.syimicode.nuvastock.firestorecustomer.CustomerModel
import com.syimicode.nuvastock.firestorecustomer.CustomerViewModel
import com.syimicode.nuvastock.ui.theme.blue
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
fun UpdateCustomerScreen(
    navController: NavHostController,
    viewModel: CustomerViewModel,
    selectedCustomer: CustomerModel?,
    isDialog: MutableState<Boolean>,
    isRefresh: MutableState<Boolean>
) {

    val initialKtpUrl = selectedCustomer?.item?.imgKtpUrl

    // Menginisialisasi nilai berdasarkan selectedCustomer
    val namaCustomer = remember { mutableStateOf(selectedCustomer?.item?.namaCustomer ?: "") }
    val whatsapp = remember { mutableStateOf(selectedCustomer?.item?.whatsapp ?: "") }
    val alamat = remember { mutableStateOf(selectedCustomer?.item?.alamat ?: "") }

    LaunchedEffect(selectedCustomer) {
        namaCustomer.value = selectedCustomer?.item?.namaCustomer ?: ""
        whatsapp.value = selectedCustomer?.item?.whatsapp ?: ""
        alamat.value = selectedCustomer?.item?.alamat ?: ""
    }

    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    var progress by remember { mutableStateOf(false) }

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
                    text = "Edit Customer",
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
                    // Menampilkan gambar yang dipilih
                    if (selectedCustomer?.item?.imgKtpUrl != null) {
                        Image(
                            painter = rememberAsyncImagePainter(
                                selectedCustomer.item.imgKtpUrl ?: ""
                            ),
                            contentDescription = "hasil gambar",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .align(Alignment.Center)
                                .width(200.sdp)
                                .height(120.sdp)
                                .clip(RoundedCornerShape(10.sdp))
                        )
                    } else {
                        // Menampilkan placeholder jika belum ada gambar yang dipilih
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
                    .padding(horizontal = 16.sdp),
                value = namaCustomer.value,
                onValueChange = {
                    namaCustomer.value = it
                },
                shape = RoundedCornerShape(5.sdp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = blue,
                    unfocusedBorderColor = line,
                    containerColor = white,
                    cursorColor = blue,
                ),
                placeholder = {
                    Text(
                        text = "Masukkan nama customer..",
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
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    capitalization = KeyboardCapitalization.Words
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
                    .padding(horizontal = 16.sdp),
                value = whatsapp.value,
                onValueChange = {
                    if (it.isDigitsOnly()) whatsapp.value = it
                },
                shape = RoundedCornerShape(5.sdp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = blue,
                    unfocusedBorderColor = line,
                    containerColor = white,
                    cursorColor = blue,
                ),
                placeholder = {
                    Text(
                        text = "Masukkan no. whatsapp..",
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
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Phone
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
                    .padding(horizontal = 16.sdp),
                value = alamat.value,
                onValueChange = {
                    alamat.value = it
                },
                shape = RoundedCornerShape(5.sdp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = blue,
                    unfocusedBorderColor = line,
                    containerColor = white,
                    cursorColor = blue,
                ),
                placeholder = {
                    Text(
                        text = "Masukkan alamat lengkap..",
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
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    capitalization = KeyboardCapitalization.Words
                )

            )


            Spacer(modifier = Modifier.height(40.sdp))


            // BUTTON SIMPAN
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = white)
                    .padding(start = 16.sdp, end = 16.sdp, bottom = 16.sdp)
            ) {
                Button(
                    onClick = {
                        scope.launch(Dispatchers.Main) {
                            // Kolom wajib diisi sebelum button simpan di klik
                            if (TextUtils.isEmpty(namaCustomer.value) ||
                                TextUtils.isEmpty(whatsapp.value) ||
                                TextUtils.isEmpty(alamat.value)
                            ) {
                                Toast.makeText(
                                    context,
                                    " Semua kolom wajib diisi ",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()

                            } else {
                                isDialog.value = true

                                val customerItem = selectedCustomer?.let {
                                    CustomerModel(
                                        key = it.key,
                                        item = CustomerModel.CustomerItem(
                                            namaCustomer = namaCustomer.value,
                                            whatsapp = whatsapp.value,
                                            alamat = alamat.value,
                                            // Memasukkan kembali nilai imgBarangUrl yang awal
                                            imgKtpUrl = initialKtpUrl
                                        )
                                    )
                                }

                                // Mengubah data ke Firestore
                                customerItem?.let {
                                    viewModel.update(it).collect {
                                        when (it) {
                                            is ResultState.Success -> {
                                                viewModel.getItems()
                                                context.showMsg(it.data)
                                                // Berpindah ke halaman sebelumnya
                                                navController.popBackStack()
                                                isDialog.value = false
                                                progress = false
                                                isRefresh.value = true
                                            }

                                            is ResultState.Failure -> {
                                                navController.popBackStack()
                                                context.showMsg(it.msg.toString())
                                                isDialog.value = false
                                                progress = false
                                            }

                                            ResultState.Loading -> {
                                                isDialog.value = true
                                                progress = true
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    },
                    contentPadding = PaddingValues(
                        horizontal = 16.sdp,
                        vertical = 12.sdp
                    ),
                    shape = RoundedCornerShape(10.sdp),
                    colors = ButtonDefaults.buttonColors(containerColor = blue),
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                ) {
                    Text(
                        text = "Ubah",
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