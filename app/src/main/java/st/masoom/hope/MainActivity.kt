package st.masoom.hope

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import st.masoom.hope.Swipe.Top
import st.masoom.hope.ui.theme.HopeTheme

class MainActivity : ComponentActivity() {

    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        //val authViewModel : AuthViewModel by viewModels()

        setContent {
            HopeTheme {
               MyApp(authViewModel = authViewModel)

            }
        }
    }
}


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