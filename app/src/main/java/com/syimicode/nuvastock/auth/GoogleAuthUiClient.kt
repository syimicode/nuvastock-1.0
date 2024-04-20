package com.syimicode.nuvastock.auth

import android.app.PendingIntent.CanceledException
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.BeginSignInRequest.GoogleIdTokenRequestOptions
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.syimicode.nuvastock.R
import kotlinx.coroutines.tasks.await
import java.util.concurrent.CancellationException

class GoogleAuthUiClient(
    private val context: Context,
    private val oneTapClient: SignInClient
) {
    // Inisialisasi objek Firebase Authentication
    private val auth = Firebase.auth

    // Fungsi untuk memulai proses sign-in
    suspend fun signIn(): IntentSender? {
        val result = try {
            // Memulai proses sign-in menggunakan oneTapClient dengan membangun permintaan sign-in
            oneTapClient.beginSignIn(
                buildSignInRequest()
            ).await()
        } catch (e: Exception) {
            // Menangani kesalahan yang mungkin terjadi selama proses sign-in
            e.printStackTrace()
            if (e is CanceledException) throw e
            null
        }
        // Mengembalikan IntentSender jika berhasil dimulai, jika tidak, akan mengembalikan null
        return result?.pendingIntent?.intentSender
    }

    // Fungsi untuk melakukan sign-in dengan Intent yang diberikan
    suspend fun signInWithIntent(intent: Intent): SignInResult {
        // Mendapatkan credential sign-in dari Intent yang diberikan
        val credential = oneTapClient.getSignInCredentialFromIntent(intent)
        val googleIdToken = credential.googleIdToken
        // Mendapatkan credential Google dari token yang didapat
        val googleCredentials = GoogleAuthProvider.getCredential(googleIdToken, null)

        return try {
            // Melakukan sign-in ke Firebase Authentication dengan credential yang didapat
            val user = auth.signInWithCredential(googleCredentials).await().user
            // Mengembalikan hasil sign-in berupa data pengguna (jika berhasil) atau null (jika gagal)
            SignInResult(
                data = user?.run {
                    UserData(
                        userId = uid,
                        username = displayName,
                        profilePictureUrl = photoUrl?.toString()
                    )
                },
                errorMessage = null
            )
        } catch (e: Exception) {
            // Menangani kesalahan yang mungkin terjadi selama proses sign-in
            e.printStackTrace()
            if (e is CanceledException) throw e
            // Mengembalikan hasil sign-in dengan data null dan pesan kesalahan (jika ada)
            SignInResult(
                data = null,
                errorMessage = e.message
            )
        }
    }

    // Fungsi untuk melakukan sign-out dari layanan otentikasi
    suspend fun signOut() {
        try {
            // Melakukan sign-out dari oneTapClient
            oneTapClient.signOut().await()
            // Melakukan sign-out dari Firebase Authentication
            auth.signOut()
        } catch (e: Exception) {
            // Menangani kesalahan yang mungkin terjadi selama proses sign-out
            e.printStackTrace()
            if (e is CancellationException) throw e
        }
    }

    // Fungsi untuk mendapatkan informasi pengguna yang telah sign-in
    fun getSignedInUser(): UserData? = auth.currentUser?.run {
        // Mengembalikan data pengguna yang telah sign-in (jika ada)
        UserData(
            userId = uid,
            username = displayName,
            profilePictureUrl = photoUrl?.toString()
        )
    }

    // Fungsi untuk membangun permintaan sign-in
    private fun buildSignInRequest(): BeginSignInRequest {
        return BeginSignInRequest.Builder()
            .setGoogleIdTokenRequestOptions(
                // Mengatur opsi permintaan sign-in, seperti token Google ID, opsi pemfilteran, dan ID klien server
                GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setFilterByAuthorizedAccounts(false)
                    .setServerClientId(context.getString(R.string.web_client_id))
                    .build()
            )
            .setAutoSelectEnabled(true)
            .build()
    }
}