package st.masoom.hope

import android.app.Activity
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks
import java.util.concurrent.TimeUnit


@Composable
fun Phone(navController: NavController) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    val context = LocalContext.current
    val activity = context as? Activity // Safely cast to Activity
    var receivedverificationId by remember { mutableStateOf("") }
    var resendToken by remember { mutableStateOf("") }

    val auth = FirebaseAuth.getInstance()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "What's your mobile number?",
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
            label = { Text(text ="Mobile Number",
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
            label = { Text("OTP", style = TextStyle(color = Color.Gray, fontSize = 16.sp, fontFamily = FontFamily.SansSerif, fontWeight = FontWeight.Light)) },
            modifier = Modifier.fillMaxWidth() // Adjusts to take up the full width of the parent container.
        )


        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {val phoneNumber = "+1${username}"  // Modify with actual country code and phone number
                val options = PhoneAuthOptions.newBuilder(auth)
                    .setPhoneNumber(phoneNumber)       // Phone number to verify
                    .setTimeout(60L, TimeUnit.SECONDS)  // Timeout for OTP
                    .setActivity(context as Activity)   // Activity to associate with the verification process
                    .setCallbacks(object : OnVerificationStateChangedCallbacks() {
                        override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
                            // OTP automatically verified, use the credential to sign in
                        }

                        override fun onVerificationFailed(e: FirebaseException) {
                            // Handle verification failure (e.g., show error message)
                            errorMessage = "OTP verification failed"
                        }

                        override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
                            receivedverificationId = verificationId
                            resendToken = token.toString() // Show UI to input OTP
                        }
                    })
                    .build()

                PhoneAuthProvider.verifyPhoneNumber(options)
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFF44336),
                contentColor = Color.Black),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Send OTP")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = {val credential = PhoneAuthProvider.getCredential(receivedverificationId, password)
                auth.signInWithCredential(credential)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // OTP verified successfully
                            navController.navigate("home")
                        } else {
                            // Show error message
                            errorMessage = "OTP verification failed"
                        }
                    }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFF44336),
                contentColor = Color.Black),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Next")
        }
        TextButton(onClick = { navController.navigate("login") }) {
            Text("Already have an account")

        }



        if (errorMessage.isNotEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = errorMessage, color = MaterialTheme.colorScheme.error)
        }
    }
}



