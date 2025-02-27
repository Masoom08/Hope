package st.masoom.hope

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import st.masoom.hope.VideoCall.AuthViewModel

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