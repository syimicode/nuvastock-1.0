package com.syimicode.nuvastock.auth

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.syimicode.nuvastock.R
import com.syimicode.nuvastock.ui.theme.blue
import com.syimicode.nuvastock.ui.theme.fontexo
import com.syimicode.nuvastock.ui.theme.line
import com.syimicode.nuvastock.ui.theme.text_secondary
import com.syimicode.nuvastock.ui.theme.white
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp

@Composable
fun SignInScreen(
    state: SignInState,
    onSignInClick: () -> Unit
) {
    val context = LocalContext.current

    // Menampilkan Toast jika terjadi kesalahan saat sign-in
    LaunchedEffect(key1 = state.signInError) {
        state.signInError?.let { error ->
            Toast.makeText(context, error, Toast.LENGTH_LONG).show()
        }
    }

    // Mendefinisikan tata letak antarmuka pengguna dengan menggunakan Compose
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = white)
            .padding(horizontal = 16.sdp),
        contentAlignment = Alignment.CenterStart
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = white),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                painter = painterResource(id = R.drawable.box),
                contentDescription = "3d box",
                modifier = Modifier
                    .height(80.sdp)
            )

            Spacer(
                modifier = Modifier
                    .height(4.sdp)
            )

            Text(
                text = "nuvastock",
                style = TextStyle(
                    color = blue,
                    fontSize = 32.ssp,
                    fontFamily = fontexo,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            )

            Button(
                onClick = onSignInClick,
                contentPadding = PaddingValues(
                    start = 16.sdp,
                    top = 16.sdp,
                    end = 16.sdp,
                    bottom = 16.sdp,
                ),
                shape = RoundedCornerShape(10.sdp),
                border = BorderStroke(1.sdp, color = line),
                colors = ButtonDefaults.buttonColors(containerColor = white),
                modifier = Modifier
                    .padding(top = 60.sdp)
                    .fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_google),
                    contentDescription = "icon google",
                    modifier = Modifier
                        .size(24.sdp)
                )

                Text(
                    text = "Masuk dengan Google",
                    modifier = Modifier
                        .padding(start = 16.sdp),
                    style = TextStyle(
                        color = text_secondary,
                        fontSize = 16.ssp,
                        fontFamily = fontexo,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center
                    )
                )
            }
        }

        Text(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 40.sdp),
            text = "Versi Aplikasi 1.0",
            style = TextStyle(
                color = text_secondary,
                fontSize = 11.ssp,
                fontFamily = fontexo,
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Center,
            )
        )
    }
}