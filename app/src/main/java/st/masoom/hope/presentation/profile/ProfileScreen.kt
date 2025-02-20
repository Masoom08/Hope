package st.masoom.hope.presentation.profile

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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import st.masoom.hope.presentation.sign_in.UserData
import androidx.compose.ui.text.input.TextFieldValue

@Composable
fun ProfileScreen(
    userId: String,
    navController: NavController,
    onSignOut: () -> Unit
) {
    var userData by remember { mutableStateOf<UserData?>(null) }

    FetchUserProfile(userId) { fetchedData ->
        userData = fetchedData
    }

    var isEditingProfile by remember { mutableStateOf(false) }

    if (isEditingProfile) {
        EditProfileScreen(
            userData = userData,
            onProfileUpdated = {
                isEditingProfile = false  // Close editor after update
            }
        )
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .background(MaterialTheme.colorScheme.background),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // Profile Header
            Text(
                text = "My Profile",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Profile Picture
            val profileImage = userData?.profilePictureUrl ?: FirebaseAuth.getInstance().currentUser?.photoUrl.toString()

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
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                ProfileTextItem("Name", userData?.username ?: "Guest")
                ProfileTextItem("Email", "Not Available")
                ProfileTextItem("Job Role", "Not Specified")

                Spacer(modifier = Modifier.height(8.dp))

                // Edit Profile Button
                Button(onClick = { isEditingProfile = true }) {
                    Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit Profile")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Edit Profile")
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
}

// Fetch User Profile from Firestore
@Composable
fun FetchUserProfile(userId: String, onUserDataFetched: (UserData) -> Unit) {
    val firestore = FirebaseFirestore.getInstance()

    LaunchedEffect(userId) {
        firestore.collection("users").document(userId)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val name = document.getString("name") ?: "N/A"
                    val email = document.getString("email") ?: "N/A"
                    val jobRole = document.getString("jobRole") ?: "N/A"
                    val uploadedImageUrl = document.getString("profilePictureUrl")
                    val googleImageUrl = FirebaseAuth.getInstance().currentUser?.photoUrl?.toString()

                    val profileImageUrl = uploadedImageUrl ?: googleImageUrl ?: ""

                    val userData = UserData(name, email, jobRole)
                    onUserDataFetched(userData)
                }
            }
            .addOnFailureListener {
                // Handle failure
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

// Edit Profile Screen
@Composable
fun EditProfileScreen(
    userData: UserData?,
    onProfileUpdated: () -> Unit
) {
    var name by remember { mutableStateOf(TextFieldValue(userData?.username ?: "")) }
    var jobRole by remember { mutableStateOf(TextFieldValue( "")) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Edit Profile", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        TextField(value = name, onValueChange = { name = it }, label = { Text("Name") })
        Spacer(modifier = Modifier.height(8.dp))

        TextField(value = jobRole, onValueChange = { jobRole = it }, label = { Text("Job Role") })
        Spacer(modifier = Modifier.height(8.dp))

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = onProfileUpdated) {
            Text(text = "Save")
        }
    }
}
