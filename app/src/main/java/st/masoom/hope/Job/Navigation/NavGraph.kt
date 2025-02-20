package st.masoom.hope.Job.Navigation

import android.widget.Toast
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import st.masoom.hope.Job.presentation.Page.ApplicationScreen
import st.masoom.hope.Job.presentation.Page.HomeScreen
import st.masoom.hope.Job.presentation.Page.Job.JobScreen
import st.masoom.hope.Job.presentation.Page.Profile.ProfileScreen
import st.masoom.hope.Job.presentation.registration.RegistrationScreen
import st.masoom.hope.Job.presentation.sign_in.GoogleAuthUiClient
import st.masoom.hope.Job.presentation.sign_in.SignInScreen
import st.masoom.hope.Job.presentation.sign_in.SignInViewModel

@Composable
fun NavGraph(
    navController: NavHostController,
    googleAuthUiClient: GoogleAuthUiClient,
    lifecycleScope: CoroutineScope, // Pass lifecycleScope
    modifier: Modifier
) {
    val context = LocalContext.current
    NavHost(navController = navController, startDestination = "sign_in") {

        composable("sign_in") {
            val viewModel: SignInViewModel = viewModel()
            val state by viewModel.state.collectAsState()

            val context = LocalContext.current

            LaunchedEffect(Unit) {
                val userId = googleAuthUiClient.getSignedInUser()?.userId
                if (userId != null) {
                    val db = FirebaseFirestore.getInstance()
                    val userDoc = db.collection("users").document(userId).get().await()
                    if (userDoc.exists()) {
                        navController.navigate("profile") {
                            popUpTo("sign_in") { inclusive = true }
                        }
                    } else {
                        navController.navigate("registration")
                    }
                }
            }

            SignInScreen(
                state = state,
                onSignInClick = {
                    lifecycleScope.launch {
                        val signInIntentSender = googleAuthUiClient.signIn()
                        signInIntentSender?.let {
                            it.sendIntent(context, 0, null, null, null)
                        }
                    }
                }
            )
        }

        composable("registration") {
            RegistrationScreen(
                navController = navController,
                userData = googleAuthUiClient.getSignedInUser(),
                onRegistrationComplete = { navController.navigate("profile") }
            )
        }
        composable("home") { HomeScreen(navController) }
        //composable("jobs") { JobScreen(navController) }
        composable("applications") { ApplicationScreen(navController) }

        composable("profile") {
            val signedInUser = googleAuthUiClient.getSignedInUser()
            ProfileScreen(
                userId = signedInUser?.userId ?: "",
                navController = navController,
                onSignOut = {
                    lifecycleScope.launch {
                        googleAuthUiClient.signOut()
                        Toast.makeText(context, "Signed out", Toast.LENGTH_LONG).show()
                        navController.popBackStack()
                    }
                }
            )
        }
    }
}