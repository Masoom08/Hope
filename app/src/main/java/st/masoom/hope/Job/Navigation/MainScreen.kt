package st.masoom.hope.Job.Navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.CoroutineScope
import st.masoom.hope.Job.presentation.sign_in.GoogleAuthUiClient

@Composable
fun MainScreen(
    googleAuthUiClient: GoogleAuthUiClient,
    lifecycleScope: CoroutineScope
) {
    /*
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    //val lifecycleScope = rememberCoroutineScope() // Use rememberCoroutineScope()

    Scaffold(
        topBar = {
            if (currentRoute != "sign_in" && currentRoute != "registration") {
                TopBar(title = "Hope Job App") // Show TopBar only after login
            }
        },
        bottomBar = {
            if (currentRoute != "sign_in" && currentRoute != "registration") {
                BottomNavigationBar(
                    navController = navController,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    ) { innerPadding ->
        NavGraph(navController = navController, googleAuthUiClient = googleAuthUiClient, lifecycleScope = lifecycleScope, modifier = Modifier.padding(innerPadding))
    }

     */
}
