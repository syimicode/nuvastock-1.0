package com.syimicode.nuvastock.firestorepeminjaman.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.syimicode.nuvastock.R
import com.syimicode.nuvastock.common.CommonDialog
import com.syimicode.nuvastock.firestorecustomer.CustomerModel
import com.syimicode.nuvastock.firestorecustomer.CustomerViewModel
import com.syimicode.nuvastock.firestorepeminjaman.SharedViewModelCustomer
import com.syimicode.nuvastock.ui.theme.fontexo
import com.syimicode.nuvastock.ui.theme.line
import com.syimicode.nuvastock.ui.theme.text_primary
import com.syimicode.nuvastock.ui.theme.white
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp

@Composable
fun SelectCustomerScreen(
    navController: NavHostController,
    viewModel: CustomerViewModel = hiltViewModel(),
    sharedViewModelCustomer: SharedViewModelCustomer
) {

    val res = viewModel.res.value

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
                    text = "Pilih Customer",
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

        if (res.data.isNotEmpty()) {

            // Urutkan isi LazyColumn berasarkan nama
            val sortedData = res.data.sortedBy { it.item?.namaCustomer?.lowercase() }

            LazyColumn {
                items(
                    sortedData,
                    key = { it.key ?: "" }
                ) { items ->
                    EachRowSelectCustomer(
                        navController = navController,
                        itemState = items,
                        sharedViewModelCustomer = sharedViewModelCustomer
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
fun EachRowSelectCustomer(
    navController: NavHostController,
    itemState: CustomerModel,
    sharedViewModelCustomer: SharedViewModelCustomer
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(white)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = rememberRipple(bounded = true)
                ) {
                    // Set pelanggan yang dipilih di SharedViewModel
                    sharedViewModelCustomer.setSelectedCustomer(itemState)

                    // Navigasi ke AddPeminjamanScreen
                    navController.navigateUp()
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

                Icon(
                    painter = painterResource(id = R.drawable.ic_user),
                    contentDescription = "ic_user",
                    modifier = Modifier
                        .padding(end = 16.sdp)
                        .size(24.sdp),
                    tint = Color.Unspecified,
                )

                Text(
                    text = if ((itemState.item?.namaCustomer?.length ?: 0) > 27) {
                        "${itemState.item?.namaCustomer?.substring(0, 27)}..."
                    } else {
                        itemState.item?.namaCustomer!!
                    },
                    style = TextStyle(
                        color = text_primary,
                        fontSize = 14.ssp,
                        fontFamily = fontexo,
                        fontWeight = FontWeight.Normal
                    )
                )
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
    }
}