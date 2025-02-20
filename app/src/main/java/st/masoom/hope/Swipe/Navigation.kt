package st.masoom.hope

import android.app.Activity.RESULT_OK
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.DrawableRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import st.masoom.hope.presentation.sign_in.SignInScreen
import st.masoom.hope.presentation.sign_in.SignInViewModel

@Composable
fun Navigation(navController: NavHostController, modifier: Modifier , authViewModel: AuthViewModel){
    NavHost(
        navController= navController ,
        startDestination ="login" ,builder={
        composable("login"){ Login(modifier, navController, authViewModel ) }
        composable("phone"){ Phone(navController) }
        composable("signup"){ SignUp( navController, authViewModel ) }
        composable("home"){Home()}
        composable("profile"){Profile(authViewModel, navController)}
            composable("map") { Map(navController) }
    })
}