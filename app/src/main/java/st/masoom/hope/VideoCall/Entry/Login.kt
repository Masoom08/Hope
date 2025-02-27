package st.masoom.hope

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import st.masoom.hope.VideoCall.AuthState
import st.masoom.hope.VideoCall.AuthViewModel

@Composable
fun Login(modifier: Modifier , navController: NavController, authViewModel: AuthViewModel) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    val authState = authViewModel.authState.observeAsState()
    val context = LocalContext.current

    LaunchedEffect(authState.value){
        when(authState.value){
            is  AuthState.Authenticated -> navController.navigate("home")
            is AuthState.Error -> Toast.makeText(context ,
                (authState.value as AuthState.Error).message , Toast.LENGTH_SHORT).show()
            else -> Unit
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Login",
            style = TextStyle(
                color = Color.Black,
                fontSize = 32.sp,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.ExtraBold
            )
        )

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text(text ="Email/Username",
                style = TextStyle(
                    color = Color.Gray,
                    fontSize = 16.sp,
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.Light
                )
            ) },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password",
                style = TextStyle(
                    color = Color.Gray,
                    fontSize = 16.sp,
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.Light
                )
            ) },
            modifier = Modifier
                .fillMaxWidth() // Adjusts to take up the full width of the parent container.
        )


        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {authViewModel.login(username, password)
                //navController.navigate("home")
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFF44336),
                contentColor = Color.Black),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Login")
        }
        Spacer(modifier = Modifier.height(8.dp))
        TextButton(onClick = { navController.navigate("phone") }) {
            Text("Don't have an account, Create a new Account")

        }



        if (errorMessage.isNotEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = errorMessage, color = MaterialTheme.colorScheme.error)
        }
    }
}



