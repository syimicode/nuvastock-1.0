package com.syimicode.nuvastock.screen.menu

import com.syimicode.nuvastock.R

data class NavItems(
    val title: String,
    val route: String,
    val selectedIcon: Int,
    val unselectedIcon: Int
)

val items = listOf(
    NavItems(
        title = "Beranda",
        route = Screens.BerandaScreen.name,
        selectedIcon = R.drawable.ic_home_filled,
        unselectedIcon = R.drawable.ic_home
    ),

    NavItems(
        title = "Barang",
        route = Screens.BarangScreen.name,
        selectedIcon = R.drawable.ic_barang_filled,
        unselectedIcon = R.drawable.ic_barang
    ),

    NavItems(
        title = "Peminjaman",
        route = Screens.PeminjamanScreen.name,
        selectedIcon = R.drawable.ic_peminjaman_filled,
        unselectedIcon = R.drawable.ic_peminjaman
    ),

    NavItems(
        title = "Pengaturan",
        route = Screens.PengaturanScreen.name,
        selectedIcon = R.drawable.ic_setting_filled,
        unselectedIcon = R.drawable.ic_setting
    )
)