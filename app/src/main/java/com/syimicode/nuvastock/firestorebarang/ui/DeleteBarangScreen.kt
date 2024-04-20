package com.syimicode.nuvastock.firestorebarang.ui

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.window.Dialog
import com.syimicode.nuvastock.R
import com.syimicode.nuvastock.ui.theme.fontexo
import com.syimicode.nuvastock.ui.theme.line
import com.syimicode.nuvastock.ui.theme.red
import com.syimicode.nuvastock.ui.theme.text_primary
import com.syimicode.nuvastock.ui.theme.white
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp

@Composable
fun DeleteBarangScreen(
    openDialogBox: MutableState<Boolean>,
    barangId: String, // Tambahkan parameter barangId
) {

    if (openDialogBox.value) {
        Dialog(
            onDismissRequest = { openDialogBox.value = false }
        ) {
            CustomUIDelete(openDialogBox, barangId)
        }
    }
}


@Composable
fun CustomUIDelete(
    openDialog: MutableState<Boolean>,
    barangId: String, // Tambahkan parameter barangId
) {

    val context = LocalContext.current

    Card(
        modifier = Modifier
            .padding(8.sdp),
        shape = RoundedCornerShape(10.sdp),
    ) {

        Column(
            modifier = Modifier
                .background(white)
        ) {

            Spacer(modifier = Modifier.height(24.sdp))

            // Image
            Image(
                painter = painterResource(id = R.drawable.img_barang),
                contentDescription = "img_barang",
                modifier = Modifier
                    .height(100.sdp)
                    .fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.sdp))

            Column(
                modifier = Modifier.padding(horizontal = 16.sdp)
            ) {
                Text(
                    text = "Hapus barang ini?",
                    modifier = Modifier.fillMaxWidth(),
                    style = TextStyle(
                        color = text_primary,
                        fontSize = 16.ssp,
                        fontFamily = fontexo,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center
                    )
                )

                Spacer(modifier = Modifier.height(8.sdp))

                Text(
                    text = barangId,
                    modifier = Modifier.fillMaxWidth(),
                    style = TextStyle(
                        color = text_primary,
                        fontSize = 14.ssp,
                        fontFamily = fontexo,
                        fontWeight = FontWeight.Normal,
                        textAlign = TextAlign.Center
                    )
                )
            }

            Spacer(modifier = Modifier.height(24.sdp))

            // Button
            Row(
                modifier = Modifier
                    .background(white)
                    .fillMaxWidth()
                    .padding(horizontal = 16.sdp),
                horizontalArrangement = Arrangement.spacedBy(16.sdp)
            ) {
                Button(
                    onClick = { openDialog.value = false },
                    contentPadding = PaddingValues(
                        vertical = 12.sdp
                    ),
                    shape = RoundedCornerShape(10.sdp),
                    colors = ButtonDefaults.buttonColors(containerColor = line),
                    modifier = Modifier
                        .weight(1f),
                ) {
                    Text(
                        text = "Batal",
                        style = TextStyle(
                            color = text_primary,
                            fontSize = 14.ssp,
                            fontFamily = fontexo,
                            fontWeight = FontWeight.Normal,
                            textAlign = TextAlign.Center
                        )
                    )
                }

                Button(
                    onClick = {
                        Toast.makeText(
                            context, " Anda bukan Admin ", Toast.LENGTH_SHORT
                        ).show()
                    },
                    contentPadding = PaddingValues(
                        vertical = 12.sdp
                    ),
                    shape = RoundedCornerShape(10.sdp),
                    colors = ButtonDefaults.buttonColors(containerColor = red),
                    modifier = Modifier
                        .weight(1f),
                ) {
                    Text(
                        text = "Hapus",
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

            Spacer(modifier = Modifier.height(24.sdp))
        }

    }
}