package st.masoom.hope

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.google.android.gms.auth.api.identity.Identity
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import st.masoom.hope.Job.Navigation.BottomNavigationBar
import st.masoom.hope.Job.Navigation.TopBar
import st.masoom.hope.Job.presentation.Page.ApplicationScreen
import st.masoom.hope.Job.presentation.Page.HomeScreen
import st.masoom.hope.Job.presentation.Page.Job.AddJobScreen
import st.masoom.hope.Job.presentation.Page.Job.JobScreen
import st.masoom.hope.Job.presentation.Page.Profile.ProfileScreen
import st.masoom.hope.Job.presentation.registration.RegistrationScreen
import st.masoom.hope.Job.presentation.sign_in.GoogleAuthUiClient
import st.masoom.hope.Job.presentation.sign_in.SignInScreen
import st.masoom.hope.Job.presentation.sign_in.SignInViewModel
import st.masoom.hope.ui.theme.HopeTheme


class MainActivity : ComponentActivity() {

    private val googleAuthUiClient by lazy {
        GoogleAuthUiClient(
            context = applicationContext,
            oneTapClient = Identity.getSignInClient(applicationContext)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            HopeTheme {
                    /*
                    MainScreen(
                        googleAuthUiClient = googleAuthUiClient,
                        lifecycleScope = lifecycleScope // Pass lifecycleScope
                    )
                     */
                    val context = LocalContext.current
                    val navController = rememberNavController()
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentRoute = navBackStackEntry?.destination?.route

                    Scaffold(
                        topBar = {
                            if (currentRoute != "sign_in" && currentRoute != "registration") {
                                TopBar(title = "Hope Job App")
                            }
                        },
                        bottomBar = {
                            if (currentRoute != "sign_in" && currentRoute != "registration") {
                                BottomNavigationBar(navController)
                            }
                        }
                    ) { innerPadding ->
                        NavHost(
                            navController = navController,
                            startDestination = "sign_in",
                            modifier = Modifier.padding(innerPadding)
                        ) {
                            composable("sign_in") {
                                val viewModel = viewModel<SignInViewModel>()
                                val state by viewModel.state.collectAsStateWithLifecycle()
                                /*
                                LaunchedEffect(key1 = Unit) {
                                    if (googleAuthUiClient.getSignedInUser() != null) {
                                        navController.navigate("registration") {
                                            popUpTo("sign_in") { inclusive = true }
                                        }
                                    }
                                }*/
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

                                val launcher = rememberLauncherForActivityResult(
                                    contract = ActivityResultContracts.StartIntentSenderForResult(),
                                    onResult = { result ->
                                        if (result.resultCode == RESULT_OK) {
                                            lifecycleScope.launch {
                                                val signInResult =
                                                    googleAuthUiClient.signInWithIntent(
                                                        intent = result.data ?: return@launch
                                                    )
                                                viewModel.onSignInResult(signInResult)
                                            }
                                        }
                                    }
                                )

                                LaunchedEffect(key1 = state.isSignInSuccessful) {
                                    if (state.isSignInSuccessful) {
                                        Toast.makeText(
                                            applicationContext,
                                            "Sign in successful",
                                            Toast.LENGTH_LONG
                                        ).show()

                                        navController.navigate("profile") {
                                            popUpTo("sign_in") { inclusive = true }
                                        }
                                        viewModel.resetState()
                                    }
                                }

                                SignInScreen(
                                    state = state,
                                    onSignInClick = {
                                        lifecycleScope.launch {
                                            val signInIntentSender = googleAuthUiClient.signIn()
                                            launcher.launch(
                                                IntentSenderRequest.Builder(
                                                    signInIntentSender ?: return@launch
                                                ).build()
                                            )
                                        }
                                    }
                                )
                            }
                            composable("registration") {
                                RegistrationScreen(
                                    navController = navController,
                                    userData = googleAuthUiClient.getSignedInUser(),
                                    onRegistrationComplete = {
                                        navController.navigate("profile") {
                                            popUpTo("registration") { inclusive = true }
                                        }
                                    }
                                )
                            }
                            composable("home") { HomeScreen(navController) }
                            composable("jobs") {
                                val userId = googleAuthUiClient.getSignedInUser()?.userId ?: ""
                                JobScreen(navController,userId)
                            }
                            composable("add_job") {
                                AddJobScreen(navController)
                            }
                            composable("applications") { ApplicationScreen(navController) }

                            composable("profile") {
                                val signedInUser = googleAuthUiClient.getSignedInUser()
                                ProfileScreen(
                                    userId = signedInUser?.userId ?: "",
                                    navController = navController,
                                    onSignOut = {
                                        lifecycleScope.launch {
                                            googleAuthUiClient.signOut()
                                            Toast.makeText(context, "Signed out", Toast.LENGTH_LONG)
                                                .show()
                                            navController.popBackStack()
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }