package com.syimicode.nuvastock.screen.menu

import android.os.Build
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.android.gms.auth.api.identity.Identity
import com.syimicode.nuvastock.auth.GoogleAuthUiClient
import com.syimicode.nuvastock.auth.SignInScreen
import com.syimicode.nuvastock.auth.SignInViewModel
import com.syimicode.nuvastock.firestorebarang.BarangModel
import com.syimicode.nuvastock.firestorebarang.BarangViewModel
import com.syimicode.nuvastock.firestorebarang.ui.AddBarangScreeen
import com.syimicode.nuvastock.firestorebarang.ui.DetailBarangScreen
import com.syimicode.nuvastock.firestorebarang.ui.UpdateBarangScreen
import com.syimicode.nuvastock.firestorecustomer.CustomerViewModel
import com.syimicode.nuvastock.firestorecustomer.ui.AddCustomerScreen
import com.syimicode.nuvastock.firestorecustomer.ui.CustomerScreen
import com.syimicode.nuvastock.firestorecustomer.ui.DeleteCustomerScreen
import com.syimicode.nuvastock.firestorecustomer.ui.DetailCustomerScreen
import com.syimicode.nuvastock.firestorecustomer.ui.UpdateCustomerScreen
import com.syimicode.nuvastock.firestorepeminjaman.PeminjamanViewModel
import com.syimicode.nuvastock.firestorepeminjaman.SharedViewModelBarang
import com.syimicode.nuvastock.firestorepeminjaman.SharedViewModelCustomer
import com.syimicode.nuvastock.firestorepeminjaman.ui.AddPeminjamanScreen
import com.syimicode.nuvastock.firestorepeminjaman.ui.DetailPeminjamanScreen
import com.syimicode.nuvastock.firestorepeminjaman.ui.SelectBarangScreen
import com.syimicode.nuvastock.firestorepeminjaman.ui.SelectCustomerScreen
import com.syimicode.nuvastock.ui.theme.fontexo
import com.syimicode.nuvastock.ui.theme.light_blue
import com.syimicode.nuvastock.ui.theme.text_primary
import com.syimicode.nuvastock.ui.theme.white
import ir.kaaveh.sdpcompose.ssp
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavigation() {

    // Mendapatkan konteks aplikasi
    val applicationContext = LocalContext.current.applicationContext

    // State untuk mengelola informasi autentikasi pengguna menggunakan Google Auth
    val googleAuthUiClient by lazy {
        GoogleAuthUiClient(
            context = applicationContext,
            oneTapClient = Identity.getSignInClient(applicationContext)
        )
    }

    // NavController untuk melakukan navigasi antar-destinasi
    val navController = rememberNavController()

    // sharedViewModelCustomer untuk berbagi data customer antar layar pada fitur Peminjaman Barang
    val sharedViewModelCustomer: SharedViewModelCustomer = hiltViewModel()

    // sharedViewModelBarang untuk berbagi data barang antar layar pada fitur Peminjaman Barang
    val sharedViewModelBarang: SharedViewModelBarang = hiltViewModel()

    // State untuk menyimpan index item terpilih di Bottom Navigation
    var selectedItemIndex by rememberSaveable {
        mutableStateOf(0) // Ganti nilai awal dengan indeks BerandaScreen pada list items
    }

    // State untuk menentukan apakah Bottom Navigation Bar akan ditampilkan atau disembunyikan
    val bottomBarState = rememberSaveable { (mutableStateOf(true)) }

    // Scaffold digunakan untuk membuat tata letak
    Scaffold(
        bottomBar = {
            // AnimatedVisibility untuk mengontrol visibilitas Bottom Navigation Bar
            AnimatedVisibility(
                visible = bottomBarState.value,
                enter = EnterTransition.None,
                exit = ExitTransition.None,
                content = {

                    // Navigasi bar yang terdiri dari menu navigasi
                    NavigationBar(containerColor = white) {
                        val navBackStackEntry by navController.currentBackStackEntryAsState()
                        val currentDestination = navBackStackEntry?.destination

                        // Logika untuk menyembunyikan BottomBar saat navigasi ke setiap Screen
                        when (navBackStackEntry?.destination?.route) {
                            Screens.SignInScreen.name,
                            Screens.CustomerScreen.name,
                            Screens.AddCustomerScreen.name,
                            Screens.AddBarangScreen.name,
                            Screens.DetailBarangScreen.name,
                            Screens.AddPeminjamanScreen.name,
                            Screens.DetailPeminjamanScreen.name -> {
                                bottomBarState.value = false // Menyembunyikan BottomBar
                            }
                        }

                        // Menampilkan setiap item pada Bottom Navigation
                        items.forEachIndexed { index, navItems ->
                            NavigationBarItem(
                                // Menampilkan setiap item pada Bottom Navigation
                                selected = currentDestination?.hierarchy?.any { it.route == navItems.route } == true,
                                onClick = {
                                    // Melakukan navigasi ke halaman yang dipilih
                                    navController.navigate(navItems.route) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                    selectedItemIndex = index
                                },
                                icon = {
                                    Icon(
                                        painter = painterResource(
                                            id = if (
                                                index == selectedItemIndex) navItems.selectedIcon else navItems.unselectedIcon
                                        ),
                                        tint = Color.Unspecified,
                                        contentDescription = null
                                    )
                                },
                                label = {
                                    Text(
                                        text = navItems.title,
                                        style = TextStyle(
                                            color = text_primary,
                                            fontSize = 11.ssp,
                                            fontFamily = fontexo,
                                            fontWeight = FontWeight.Normal,
                                            textAlign = TextAlign.Center
                                        )
                                    )
                                },
                                colors = NavigationBarItemDefaults.colors(
                                    indicatorColor = light_blue
                                )
                            )
                        }

                    }
                }
            )
        }
    ) { paddingValues: PaddingValues ->
        // Menangani navigasi antar destinasi
        NavHost(
            navController = navController,
            startDestination = Screens.SignInScreen.name,
            modifier = Modifier
                .padding(paddingValues),
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None }
        ) {

            // Mendefinisikan composable untuk setiap halaman
            composable(
                route = Screens.SignInScreen.name
            ) {

                LaunchedEffect(Unit) {
                    // Menyembunyikan BottomBar saat navigasi ke halaman SignInScreen
                    bottomBarState.value = false
                }

                // Mengambil ViewModel menggunakan viewModel() dari Compose
                val viewModel = viewModel<SignInViewModel>()

                // Mengambil state dari ViewModel sebagai StateFlow menggunakan collectAsState()
                val state by viewModel.state.collectAsState()

                val lifecycleOwner = LocalLifecycleOwner.current

                // Cek apakah pengguna sudah login
                // Jika pengguna sudah login, navigasikan ke BerandaScreen
                LaunchedEffect(key1 = Unit) {
                    if (googleAuthUiClient.getSignedInUser() != null) {
                        navController.navigate(Screens.BerandaScreen.name)
                    }
                }

                // Menggunakan rememberLauncherForActivityResult untuk menangani hasil dari ActivityResultContracts.StartIntentSenderForResult
                val launcher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.StartIntentSenderForResult(),
                    onResult = { result ->
                        if (result.resultCode == ComponentActivity.RESULT_OK) {
                            lifecycleOwner.lifecycleScope.launch {
                                // Melakukan sign-in dengan intent yang diberikan
                                val signInResult = googleAuthUiClient.signInWithIntent(
                                    intent = result.data ?: return@launch
                                )
                                // Mengupdate ViewModel dengan hasil sign-in
                                viewModel.onSignInResult(signInResult)
                            }
                        }
                    }
                )

                // Menampilkan pesan Toast jika proses sign-in berhasil
                LaunchedEffect(key1 = state.isSignInSuccessful) {
                    if (state.isSignInSuccessful) {
                        Toast.makeText(
                            applicationContext,
                            " Anda Berhasil Masuk! ",
                            Toast.LENGTH_LONG
                        ).show()

                        // Navigasi ke BerandaScreen setelah sign-in berhasil
                        navController.navigate(Screens.BerandaScreen.name)
                        viewModel.resetState()
                    }
                }

                // Menampilkan tampilan untuk proses sign-in
                SignInScreen(
                    state = state,
                    onSignInClick = {
                        lifecycleOwner.lifecycleScope.launch {
                            // Memulai proses sign-in dengan GoogleAuthUiClient
                            val signInIntentSender = googleAuthUiClient.signIn()

                            // Memulai launcher untuk aktivitas sign-in
                            launcher.launch(
                                IntentSenderRequest.Builder(
                                    signInIntentSender ?: return@launch
                                ).build()
                            )
                        }
                    }
                )
            }

            composable(
                route = Screens.BerandaScreen.name
            ) {

                LaunchedEffect(Unit) {
                    // Menampilkan kembali BottomBar saat navigasi ke halaman BerandaScreen
                    bottomBarState.value = true
                }

                BerandaScreen(navController)
            }

            composable(
                route = Screens.BarangScreen.name
            ) {

                val viewModel: BarangViewModel = hiltViewModel()

                LaunchedEffect(Unit) {
                    // Menampilkan kembali BottomBar saat navigasi ke halaman BarangScreen
                    bottomBarState.value = true
                }

                LaunchedEffect(viewModel) {
                    viewModel.getItems()
                }

                BarangScreen(
                    navController = navController,
                    viewModel = viewModel
                )
            }

            composable(
                route = Screens.PeminjamanScreen.name
            ) {

                val viewModel: PeminjamanViewModel = hiltViewModel()

                LaunchedEffect(Unit) {
                    // Menampilkan kembali BottomBar saat navigasi ke halaman PeminjamanScreen
                    bottomBarState.value = true
                }

                LaunchedEffect(viewModel) {
                    viewModel.getItems()
                }

                PeminjamanScreen(
                    navController = navController,
                    viewModel = viewModel
                )
            }

            composable(
                route = Screens.PengaturanScreen.name
            ) {

                LaunchedEffect(Unit) {
                    // Menampilkan kembali BottomBar saat navigasi ke halaman PengaturanScreen
                    bottomBarState.value = true
                }

                val lifecycleOwner = LocalLifecycleOwner.current

                PengaturanScreen(
                    userData = googleAuthUiClient.getSignedInUser(),
                    onSignOut = {
                        lifecycleOwner.lifecycleScope.launch {
                            googleAuthUiClient.signOut()
                            Toast.makeText(
                                applicationContext,
                                " Anda Telah Keluar! ",
                                Toast.LENGTH_LONG
                            ).show()

                            // Navigasi ke SignInScreen setelah proses onSignOut berhasil
                            navController.navigate(Screens.SignInScreen.name)
                        }
                    },
                    navController = navController
                )
            }

            // Fragment CustomerScreen
            composable(
                route = Screens.CustomerScreen.name
            ) {

                LaunchedEffect(Unit) {
                    // Menyembunyikan BottomBar saat navigasi ke halaman CustomerScreen
                    bottomBarState.value = false
                }

                val viewModel: CustomerViewModel = hiltViewModel()

                val navBackStackEntry by navController.currentBackStackEntryAsState()

                val customerAdded =
                    navBackStackEntry?.savedStateHandle?.get<Boolean>("customerAdded")

                LaunchedEffect(customerAdded) {
                    customerAdded?.let {
                        viewModel.getItems()
                        navBackStackEntry?.savedStateHandle?.set(
                            "customerAdded",
                            false
                        )
                    }
                }

                LaunchedEffect(viewModel) {
                    viewModel.getItems()
                }

                CustomerScreen(
                    navController = navController,
                    viewModel = viewModel
                )
            }

            // Fragment AddCustomerScreen
            composable(
                route = Screens.AddCustomerScreen.name
            ) {

                LaunchedEffect(Unit) {
                    // Menyembunyikan BottomBar saat navigasi ke halaman AddCustomerScreen
                    bottomBarState.value = false
                }

                LaunchedEffect(true) {
                    navController.previousBackStackEntry?.savedStateHandle?.set(
                        "customerAdded",
                        true
                    )
                }

                AddCustomerScreen(navController)
            }

            // Fragment DetailCustomerScreen
            composable(
                route = "${Screens.DetailCustomerScreen.name}/{customerId}",
                arguments = listOf(navArgument("customerId") { type = NavType.StringType })
            ) { navBackStackEntry ->
                val viewModel: CustomerViewModel = hiltViewModel()
                val customerId = navBackStackEntry.arguments?.getString("customerId") ?: ""

                DetailCustomerScreen(
                    navController = navController,
                    viewModel = viewModel,
                    customerId = customerId
                )

                LaunchedEffect(viewModel) {
                    viewModel.getItems()
                }
            }

            // Fragment DeleteCustomerScreen
            composable(
                route = Screens.DeleteCustomerScreen.name,
                arguments = listOf(navArgument("customerId") { type = NavType.StringType })
            ) { navBackStackEntry ->

                val openDialog = remember { mutableStateOf(false) }

                val viewModel: CustomerViewModel = hiltViewModel()
                val customerId = navBackStackEntry.arguments?.getString("customerId") ?: ""

                val res = viewModel.res.value
                val selectedCustomer = res.data.find { it.key == customerId }

                if (selectedCustomer != null) {
                    val namaCustomer = selectedCustomer.item?.namaCustomer ?: ""

                    DeleteCustomerScreen(
                        openDialogBox = openDialog,
                        customerId = namaCustomer,
                    )
                }
            }

            // Fragment UpdateCustomerScreen
            composable(
                route = "${Screens.UpdateCustomerScreen.name}/{customerId}",
                arguments = listOf(navArgument("customerId") { type = NavType.StringType })
            ) { navBackStackEntry ->

                val viewModel: CustomerViewModel = hiltViewModel()
                val customerId = navBackStackEntry.arguments?.getString("customerId") ?: ""

                // Dapatkan data customer dari Firestore berdasarkan customerId
                val selectedCustomer = viewModel.res.value.data.find { it.key == customerId }

                val isDialog = remember { mutableStateOf(false) }
                val isRefresh = remember { mutableStateOf(false) }

                UpdateCustomerScreen(
                    navController = navController,
                    viewModel = viewModel,
                    selectedCustomer = selectedCustomer,
                    isDialog = isDialog,
                    isRefresh = isRefresh
                )
            }

            // Fragment AddBarangScreen
            composable(
                route = Screens.AddBarangScreen.name
            ) {

                LaunchedEffect(Unit) {
                    // Menyembunyikan BottomBar saat navigasi ke halaman AddBarangScreen
                    bottomBarState.value = false
                }

                AddBarangScreeen(navController)
            }

            // Fragment DetailBarangScreen
            composable(
                route = "${Screens.DetailBarangScreen.name}/{barangId}",
                arguments = listOf(navArgument("barangId") { type = NavType.StringType })
            ) { navBackStackEntry ->

                LaunchedEffect(Unit) {
                    // Menyembunyikan BottomBar saat navigasi ke halaman DetailBarangScreen
                    bottomBarState.value = false
                }

                val viewModel: BarangViewModel = hiltViewModel()
                val barangId = navBackStackEntry.arguments?.getString("barangId") ?: ""

                DetailBarangScreen(
                    navController = navController,
                    viewModel = viewModel,
                    barangId = barangId
                )

                LaunchedEffect(viewModel) {
                    viewModel.getItems()
                }
            }

            // Fragment UpdateBarangScreen
            composable(
                route = "${Screens.UpdateBarangScreen.name}/{barangId}",
                arguments = listOf(navArgument("barangId") { type = NavType.StringType })
            ) { navBackStackEntry ->

                val viewModel: BarangViewModel = hiltViewModel()
                val barangId = navBackStackEntry.arguments?.getString("barangId") ?: ""

                // Dapatkan data barang dari Firestore berdasarkan barangId
                val selectedBarang = viewModel.res.value.data.find { it.key == barangId }

                val isDialog = remember { mutableStateOf(false) }
                val isRefresh = remember { mutableStateOf(false) }

                UpdateBarangScreen(
                    navController = navController,
                    viewModel = viewModel,
                    selectedBarang = selectedBarang,
                    isDialog = isDialog,
                    isRefresh = isRefresh
                )
            }

            // Fragment AddPeminjamanScreen
            composable(
                route = Screens.AddPeminjamanScreen.name
            ) {

                LaunchedEffect(Unit) {
                    // Menyembunyikan BottomBar saat navigasi ke halaman AddPeminjamanScreen
                    bottomBarState.value = false
                }

                // Reset pelanggan yang dipilih ke null saat kembali ke halaman sebelumnya
                BackHandler {
                    navController.navigateUp()
                    sharedViewModelCustomer.resetSelectedCustomer()
                    sharedViewModelBarang.resetSelectedBarang()
                }

                val viewModel: PeminjamanViewModel = hiltViewModel()

                AddPeminjamanScreen(
                    navController = navController,
                    sharedViewModelCustomer = sharedViewModelCustomer,
                    viewModel = viewModel,
                    sharedViewModelBarang = sharedViewModelBarang
                )
            }

            // Fragment SelectCustomerScreen
            composable(
                route = Screens.SelectCustomerScreen.name
            ) {

                val viewModel: CustomerViewModel = hiltViewModel()

                SelectCustomerScreen(
                    navController = navController,
                    viewModel = viewModel,
                    sharedViewModelCustomer = sharedViewModelCustomer
                )
            }

            // Fragment SelectBarangScreen
            composable(
                route = Screens.SelectBarangScreen.name
            ) {

                val viewModel: BarangViewModel = hiltViewModel()

                SelectBarangScreen(
                    navController = navController,
                    viewModel = viewModel,
                    sharedViewModelBarang = sharedViewModelBarang
                )
            }

            // Fragment DetailPeminjamanScreen
            composable(
                route = "${Screens.DetailPeminjamanScreen.name}/{pesananId}",
                arguments = listOf(navArgument("pesananId") { type = NavType.StringType })
            ) { navBackStackEntry ->

                val viewModel: PeminjamanViewModel = hiltViewModel()
                val pesananId = navBackStackEntry.arguments?.getString("pesananId") ?: ""

                LaunchedEffect(Unit) {
                    // Menampilkan kembali BottomBar saat navigasi ke halaman PeminjamanScreen
                    bottomBarState.value = false
                }

                DetailPeminjamanScreen(
                    navController = navController,
                    viewModel = viewModel,
                    pesananId = pesananId
                )
            }

        }
    }
}