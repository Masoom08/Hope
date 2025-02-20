package st.masoom.hope.Job.presentation.Page.Profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import st.masoom.hope.Job.presentation.sign_in.UserData
import androidx.compose.ui.text.input.TextFieldValue

@Composable
fun ProfileScreen(
    userId: String,
    navController: NavController,
    onSignOut: () -> Unit
) {
    var userData by remember { mutableStateOf<UserData?>(null) }
    var userMap by remember { mutableStateOf<Map<String, Any>?>(null) }

    FetchUserProfile(userId) { fetchedData ->
        userMap = fetchedData
    }

    var isEditingProfile by remember { mutableStateOf(false) }

    if (isEditingProfile) {
        EditProfileScreen(
            userProfile = userMap,
            onProfileUpdated = { updatedData ->
                updateUserProfile(userId, updatedData)
                isEditingProfile = false  // Close editor after update
            }
        )
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding()
                .background(Color.White)
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top= 70.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Spacer(modifier = Modifier.height(24.dp))

                val profileImage = userData?.profilePictureUrl
                    ?: FirebaseAuth.getInstance().currentUser?.photoUrl.toString()

                AsyncImage(
                    model = profileImage.ifEmpty { "https://via.placeholder.com/150" }, // Placeholder if no image
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Profile Info
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.Center
                ) {
                    ProfileTextItem("Name", "${userMap?.get("firstName") ?: "Guest"} ${userMap?.get("lastName") ?: ""}")
                    ProfileTextItem("Email", "Not Available")
                    ProfileTextItem("Job Role", "Not Specified")

                    Spacer(modifier = Modifier.height(8.dp))

                    // Edit Profile Button
                    Button(onClick = { isEditingProfile = true }) {
                        Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit Profile")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "Edit Profile")
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

                // Navigation Buttons
            Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                    Button(onClick = { navController.navigate("home") }) {
                        Text(text = "Home")
                    }

                Button(
                        onClick = {
                            onSignOut()
                            navController.navigate("login") {
                                popUpTo("home") { inclusive = true }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                    ) {
                        Text(text = "Sign Out", color = Color.White)
                    }
                }
            }
    }
}


// UI for Profile Text Items
@Composable
fun ProfileTextItem(label: String, value: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        Text(text = label, fontSize = 14.sp, color = Color.Gray)
        Text(text = value, fontSize = 18.sp, fontWeight = FontWeight.Medium)
    }
}
// Function to update user data in Firestore
fun updateUserProfile(userId: String, updatedData: Map<String, Any>) {
    val db = FirebaseFirestore.getInstance()
    db.collection("users").document(userId)
        .update(updatedData)
        .addOnSuccessListener {
            println("Profile updated successfully!")
        }
}
