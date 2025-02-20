package st.masoom.hope.Job.presentation.Page.Profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import st.masoom.hope.Job.presentation.sign_in.UserData

// Edit Profile Screen
@Composable
fun EditProfileScreen(
    userProfile: Map<String,Any>?,
    onProfileUpdated: (Map<String, Any>) -> Unit
) {
    var firstName by remember { mutableStateOf(TextFieldValue(userProfile?.get("firstName") as? String ?: "")) }
    var lastName by remember { mutableStateOf(TextFieldValue(userProfile?.get("lastName") as? String ?: "")) }
    var jobRole by remember { mutableStateOf(TextFieldValue(userProfile?.get("role") as? String ?: "")) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Edit Profile", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        TextField(value = firstName, onValueChange = { firstName = it }, label = { Text("First Name") })
        Spacer(modifier = Modifier.height(8.dp))

        TextField(value = lastName, onValueChange = { lastName = it }, label = { Text("Last Name") })
        Spacer(modifier = Modifier.height(8.dp))

        TextField(value = jobRole, onValueChange = { jobRole = it }, label = { Text("Job Role") })
        Spacer(modifier = Modifier.height(8.dp))

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            // Updated profile map
            val updatedProfile = mapOf(
                "firstName" to firstName.text,
                "lastName" to lastName.text,
                "role" to jobRole.text
            )
            onProfileUpdated(updatedProfile)  // Pass updated data
        }) {
            Text(text = "Save")
        }
    }
}