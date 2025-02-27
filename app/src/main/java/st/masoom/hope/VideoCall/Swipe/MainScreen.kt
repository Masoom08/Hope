package st.masoom.hope

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import st.masoom.hope.VideoCall.Swipe.Top
import st.masoom.hope.VideoCall.AuthViewModel


@Composable
fun MyApp(authViewModel: AuthViewModel) {
    val navController = rememberNavController()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    //val authViewModel : AuthViewModel by viewModels()
    Scaffold(
        topBar = {
            if (currentRoute =="home"){
                Top(navController=navController)
            }
        },
        bottomBar = { /*BottomNavigationBar(navController = navController)*/ }
    ) { innerPadding ->
        Navigation(navController = navController, modifier = Modifier.padding(innerPadding),authViewModel =authViewModel)
    }
}