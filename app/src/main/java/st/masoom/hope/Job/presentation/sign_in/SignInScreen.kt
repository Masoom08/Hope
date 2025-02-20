package st.masoom.hope.Job.presentation.sign_in

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp
import st.masoom.hope.R

@Composable
fun SignInScreen(
    state: SignInState,
    onSignInClick: () -> Unit
) {
    val context = LocalContext.current
    LaunchedEffect(key1 = state.signInError) {
        state.signInError?.let { error ->
            Toast.makeText(context, error, Toast.LENGTH_LONG).show()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Include an image
            Image(
                painter = painterResource(id = R.drawable.image), // Replace with actual image
                contentDescription = "Sign In Image",
                modifier = Modifier
                    .size(200.dp) // Adjust size as needed
                    .padding(bottom = 24.dp)
            )

            // Sign In button
            Button(
                onClick = onSignInClick,
                modifier = Modifier
                    .fillMaxWidth(0.8f) // Broad horizontal button
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF87CEFA)) // Sky Blue color
            ) {
                Text(text = "Sign in", fontSize = 18.sp, color = Color.Black)
            }
        }
    }
}