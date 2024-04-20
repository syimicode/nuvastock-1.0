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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.syimicode.nuvastock.R
import com.syimicode.nuvastock.auth.UserData
import com.syimicode.nuvastock.firestorecustomer.CustomerViewModel
import com.syimicode.nuvastock.ui.theme.fontexo
import com.syimicode.nuvastock.ui.theme.line
import com.syimicode.nuvastock.ui.theme.text_primary
import com.syimicode.nuvastock.ui.theme.text_secondary
import com.syimicode.nuvastock.ui.theme.white
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp

@Composable
fun PengaturanScreen(
    userData: UserData?,
    onSignOut: () -> Unit,
    navController: NavHostController
) {

    val viewModel: CustomerViewModel = hiltViewModel()
    val customerCount by viewModel.customerCount.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(white),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(white)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.sdp)
                    .background(white)
            ) {
                Text(
                    text = "Pengaturan",
                    style = TextStyle(
                        color = text_primary,
                        fontSize = 20.ssp,
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
                .background(white),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.sdp)
                    .background(white),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                if (userData?.profilePictureUrl != null) {
                    AsyncImage(
                        model = userData.profilePictureUrl,
                        contentDescription = "Profile picture",
                        modifier = Modifier
                            .size(100.sdp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            // #1 ROW (Nama)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.sdp)
                    .background(white),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = "Nama",
                    style = TextStyle(
                        color = text_secondary,
                        fontSize = 16.ssp,
                        fontFamily = fontexo,
                        fontWeight = FontWeight.Normal
                    )
                )
                if (userData?.username != null) {
                    Text(
                        text = userData.username,
                        style = TextStyle(
                            color = text_primary,
                            fontSize = 16.ssp,
                            fontFamily = fontexo,
                            fontWeight = FontWeight.Normal
                        )
                    )
                }
            }

            // #2 ROW (Masuk dengan)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.sdp)
                    .background(white),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = "Masuk dengan",
                    style = TextStyle(
                        color = text_secondary,
                        fontSize = 16.ssp,
                        fontFamily = fontexo,
                        fontWeight = FontWeight.Normal
                    )
                )
                Row(
                    modifier = Modifier
                        .background(white),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        text = "Google",
                        modifier = Modifier
                            .padding(end = 8.sdp),
                        style = TextStyle(
                            color = text_primary,
                            fontSize = 16.ssp,
                            fontFamily = fontexo,
                            fontWeight = FontWeight.Normal
                        )
                    )

                    Icon(
                        painter = painterResource(id = R.drawable.ic_masukdengan),
                        contentDescription = "ic_google",
                        modifier = Modifier
                            .size(20.sdp),
                        tint = Color.Unspecified
                    )
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(white)
            ) {

                // #3 ROW (Customer)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = rememberRipple(bounded = true)
                        ) {
                            navController.navigate(Screens.CustomerScreen.name)
                        }
                        .padding(16.sdp)
                        .background(white),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        text = "Customer",
                        style = TextStyle(
                            color = text_secondary,
                            fontSize = 16.ssp,
                            fontFamily = fontexo,
                            fontWeight = FontWeight.Normal
                        )
                    )
                    Row(
                        modifier = Modifier
                            .background(white),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Text(
                            text = "$customerCount Customer",
                            modifier = Modifier
                                .padding(end = 8.sdp),
                            style = TextStyle(
                                color = text_primary,
                                fontSize = 16.ssp,
                                fontFamily = fontexo,
                                fontWeight = FontWeight.Normal
                            )
                        )

                        Icon(
                            painter = painterResource(id = R.drawable.ic_right),
                            contentDescription = "ic_right",
                            modifier = Modifier
                                .size(20.sdp),
                            tint = Color.Unspecified,
                        )
                    }
                }

                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter),
                    color = line,
                    thickness = 1.sdp
                )
            }

            // #4 ROW (Keluar)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = rememberRipple(bounded = true)
                    ) {
                        onSignOut()
                    }
                    .padding(16.sdp)
                    .background(white),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = "Keluar",
                    style = TextStyle(
                        color = text_secondary,
                        fontSize = 16.ssp,
                        fontFamily = fontexo,
                        fontWeight = FontWeight.Normal
                    ),
                )

                Icon(
                    painter = painterResource(id = R.drawable.ic_right),
                    contentDescription = "ic_right",
                    modifier = Modifier
                        .size(20.sdp),
                    tint = Color.Unspecified,
                )
            }

            Text(
                modifier = Modifier
                    .padding(top = 24.sdp),
                text = "Versi Aplikasi 1.0",
                style = TextStyle(
                    color = text_secondary,
                    fontSize = 11.ssp,
                    fontFamily = fontexo,
                    fontWeight = FontWeight.Normal,
                    textAlign = TextAlign.Center,
                )
            )

            Spacer(modifier = Modifier.height(4.sdp))
        }
    }
}


@Preview
@Composable
fun PengaturanScreenPreview() {
    PengaturanScreen(
        userData = null,
        onSignOut = {},
        navController = rememberNavController()
    )
}