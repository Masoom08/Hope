package st.masoom.hope

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import st.masoom.hope.VideoCall.AuthViewModel
import st.masoom.hope.VideoCall.EditProfilePage

@Composable
fun Profile(authViewModel: AuthViewModel, navController: NavController){


    var userName by remember { mutableStateOf("Rahul Kumar") }
    var phoneNumber by remember { mutableStateOf("945477777") }
    var emailId by remember { mutableStateOf("abc@gmail.com") }
    var pronouns by remember { mutableStateOf("he/his") }
    var gender by remember { mutableStateOf("Male") }

    var isEditingProfile by remember { mutableStateOf(false) }

    if (isEditingProfile) {
        // Show the edit profile page
        EditProfilePage(
            initialName = userName,
            initialPhone = phoneNumber,
            initialEmail = emailId,
            initialPronouns = pronouns,
            onProfileUpdated = { updatedName, updatedPhone, updatedEmail, updatedPronouns, updatedGender ->
                // Update the profile data with the new values
                userName = updatedName
                phoneNumber = updatedPhone
                emailId = updatedEmail
                pronouns = updatedPronouns
                gender = updatedGender
                // Navigate back to profile page
                isEditingProfile = false
            }
        )
    } else {
        // Show the profile page
        val context = LocalContext.current
        Column(
            modifier = androidx.compose.ui.Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = androidx.compose.ui.Modifier.height(16.dp))

            Box(
                modifier = androidx.compose.ui.Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .background(Color(0xFFF9FCF8))
                    .padding(16.dp)
            ) {

                Text(
                    text = "My Profile",
                    style = TextStyle(
                        color = Color.Black,
                        textAlign = TextAlign.Center,
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp
                    )
                )
            }


            Column(
                modifier = androidx.compose.ui.Modifier
                    .padding(16.dp)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.Start
            ) {
                Row {
                    Box(
                        modifier = androidx.compose.ui.Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.TopStart
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.farmer), // Replace with your image resource
                            contentDescription = "Profile Picture",
                            contentScale = ContentScale.Crop,
                            modifier = androidx.compose.ui.Modifier
                                .size(100.dp)
                                .clip(CircleShape)
                        )
                    }

                    Spacer(modifier = androidx.compose.ui.Modifier.width(32.dp))

                    Column {

                        Row(verticalAlignment = Alignment.CenterVertically) {

                            Text(
                                text = "Edit Profile",
                                style = MaterialTheme.typography.bodyLarge
                            )
                            IconButton(onClick = { isEditingProfile = true }) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "Edit Profile"
                                )
                            }
                        }
                        Text(
                            text = "Name: $userName",
                            fontFamily = FontFamily.SansSerif,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Text(
                            text = "Phone No.: $phoneNumber",
                            fontFamily = FontFamily.SansSerif,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Text(
                            text = "Email: $emailId",
                            fontFamily = FontFamily.SansSerif,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Text(
                            text = "Pronouns: $pronouns",
                            fontFamily = FontFamily.SansSerif,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Text(
                            text = "Gender: $gender",
                            fontFamily = FontFamily.SansSerif,
                            style = MaterialTheme.typography.bodyLarge
                        )


                    }

                }

                Spacer(modifier = androidx.compose.ui.Modifier.height(32.dp))


                Column(
                    modifier = androidx.compose.ui.Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row {

                        Button(
                            onClick = {navController.navigate("map")
                                // Use the context to call the openMap function
                                //openMap(context, "28.6139", "77.2090")  // Example coordinates (New Delhi)
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFF44336),
                                contentColor = Color.Black
                            ),
                            modifier = androidx.compose.ui.Modifier
                                .width(150.dp)
                                .padding(vertical = 4.dp)
                        ) {
                            Text(text = "My Location")
                        }

                        Spacer(modifier = androidx.compose.ui.Modifier.width(32.dp))
                        Button(
                            onClick = { authViewModel.signout()
                                navController.navigate("login") {
                                    popUpTo("home") { inclusive = true }
                                }
                                      },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFF44336),
                                contentColor = Color.Black
                            ),
                            modifier = androidx.compose.ui.Modifier
                                .width(150.dp)
                                .padding(vertical = 4.dp)
                        ) {
                            Text(text = "Sign Out")
                        }
                    }
                }
            }
        }
    }
}