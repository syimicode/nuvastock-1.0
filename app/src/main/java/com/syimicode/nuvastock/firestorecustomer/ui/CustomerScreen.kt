package com.syimicode.nuvastock.firestorecustomer.ui

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.syimicode.nuvastock.R
import com.syimicode.nuvastock.common.CommonDialog
import com.syimicode.nuvastock.firestorecustomer.CustomerModel
import com.syimicode.nuvastock.firestorecustomer.CustomerViewModel
import com.syimicode.nuvastock.screen.menu.Screens
import com.syimicode.nuvastock.ui.theme.blue
import com.syimicode.nuvastock.ui.theme.fontexo
import com.syimicode.nuvastock.ui.theme.line
import com.syimicode.nuvastock.ui.theme.text_primary
import com.syimicode.nuvastock.ui.theme.white
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CustomerScreen(
    navController: NavHostController,
    viewModel: CustomerViewModel = hiltViewModel()
) {

    val res = viewModel.res.value
    val isUpdate = remember { mutableStateOf(false) }

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
                        navController.navigate(Screens.AddCustomerScreen.name)
                    },
                    containerColor = blue,
                    shape = CircleShape,
                    elevation = FloatingActionButtonDefaults.elevation(2.sdp),
                    modifier = Modifier
                        .padding(end = 8.sdp, bottom = 16.sdp)
                        .size(60.sdp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_add),
                        contentDescription = "tambahCustomer",
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
                            text = "Customer",
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

                // Atur visibilitas FloatingActionButton berdasarkan perubahan posisi scroll
                LaunchedEffect(scrollState) {
                    snapshotFlow { scrollState.firstVisibleItemIndex }
                        .collect { firstVisibleItem ->
                            val isScrolling = firstVisibleItem > 0
                            isFabVisible.value = !isScrolling
                        }
                }

                if (res.data.isNotEmpty()) {

                    // Urutkan isi LazyColumn berdasarkan nama
                    val sortedData = res.data.sortedBy { it.item?.namaCustomer?.lowercase() }

                    LazyColumn(
                        state = scrollState,
                        content = {
                            items(
                                sortedData,
                                key = { it.key ?: "" }
                            ) { items ->
                                EachRow(
                                    navController = navController,
                                    itemState = items,
                                    onUpdate = {
                                        isUpdate.value = true
                                        viewModel.setData(
                                            CustomerModel(
                                                key = items.key,
                                                item = CustomerModel.CustomerItem(
                                                    items.item?.namaCustomer, // Gunakan nilai yang diperbarui
                                                    items.item?.whatsapp, // Gunakan nilai yang diperbarui
                                                    items.item?.alamat // Gunakan nilai yang diperbarui
                                                )
                                            )
                                        )
                                    }
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
    itemState: CustomerModel,
    onUpdate: () -> Unit = {}
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
                    navController.navigate("${Screens.DetailCustomerScreen.name}/${itemState.key}")
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


@Preview
@Composable
fun CustomerScreenPreview() {
    CustomerScreen(navController = rememberNavController())
}
