package st.masoom.hope.VideoCall

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.KeyboardType

@Composable
fun EditProfilePage(
    initialName: String,
    initialPhone: String,
    initialEmail: String,
    initialPronouns: String,
    onProfileUpdated: (String, String, String, String, String) -> Unit // Callback to pass the updated data
) {
    // State to hold the user's updated information
    var name by remember { mutableStateOf(initialName) }
    var phone by remember { mutableStateOf(initialPhone) }
    var email by remember { mutableStateOf(initialEmail) }
    var pronouns by remember { mutableStateOf(initialPronouns) }
    var gender by remember { mutableStateOf("Female") } // Default to Female, can be changed

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top=56.dp)
            .padding(16.dp)
    ) {
        Text("Edit Profile", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(16.dp))

        // Name field
        TextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Phone number field
        TextField(
            value = phone,
            onValueChange = { phone = it },
            label = { Text("Phone Number") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Email field
        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email ID") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Pronouns field
        TextField(
            value = pronouns,
            onValueChange = { pronouns = it },
            label = { Text("Pronouns (e.g., she/her, he/him)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Gender selection
        Text("Gender")
        Row {
            RadioButton(
                selected = gender == "Female",
                onClick = { gender = "Female" }
            )
            Text("Female")

            Spacer(modifier = Modifier.width(16.dp))

            RadioButton(
                selected = gender == "Male",
                onClick = { gender = "Male" }
            )
            Text("Male")

            Spacer(modifier = Modifier.width(16.dp))

            RadioButton(
                selected = gender == "Other",
                onClick = { gender = "Other" }
            )
            Text("Other")
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Submit button
        Button(onClick = { onProfileUpdated(
            name, phone, email, pronouns, gender) // Pass the updated data
        },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFF44336),
                contentColor = Color.White
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save Changes")
        }
    }
}
