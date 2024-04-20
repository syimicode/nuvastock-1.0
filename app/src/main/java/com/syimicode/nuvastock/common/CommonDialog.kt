package com.syimicode.nuvastock.common

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import com.syimicode.nuvastock.ui.theme.blue
import ir.kaaveh.sdpcompose.sdp

@Composable
fun CommonDialog() {

    Dialog(onDismissRequest = { }) {
        CircularProgressIndicator(
            modifier = Modifier
                .size(60.sdp)
                .clip(CircleShape),
            color = blue,
            strokeWidth = 5.sdp,
        )
    }
}

@Preview
@Composable
fun CommonDialogPreview() {
    CommonDialog()
}