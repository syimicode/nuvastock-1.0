package com.syimicode.nuvastock.auth

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SignInViewModel : ViewModel() {
    // MutableStateFlow yang menyimpan status dari proses sign-in
    private val _state = MutableStateFlow(SignInState())

    // StateFlow sebagai representasi immutable dari _state
    val state = _state.asStateFlow()

    // Fungsi untuk menangani hasil dari proses sign-in
    fun onSignInResult(result: SignInResult) {
        // Mengupdate _state dengan mengubah isSignInSuccessful berdasarkan hasil sign-in
        _state.update {
            it.copy(
                isSignInSuccessful = result.data != null,
                signInError = result.errorMessage
            )
        }
    }

    // Fungsi untuk mereset state menjadi default (mengembalikan ke SignInState default)
    fun resetState() {
        _state.update { SignInState() }
    }
}