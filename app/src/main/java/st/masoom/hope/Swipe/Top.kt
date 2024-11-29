package st.masoom.hope.Swipe

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import st.masoom.hope.R // Replace with your actual package for drawable resources

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Top(navController: NavController) {
    TopAppBar(
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Video Call",
                    style = TextStyle(
                        color = Color.Black,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.ExtraBold
                    ),
                    modifier = Modifier.padding(vertical = 0.dp)
                )
                Image(
                    imageVector = Icons.Default.Person,  // Replace with your actual drawable resource
                    contentDescription = "Profile Image",
                    modifier = Modifier
                        .size(40.dp) // Adjust size as needed
                        .clip(CircleShape)
                        .clickable {
                            navController.navigate("profile") // Navigates to the profile screen
                        }
                        .padding(8.dp),
                    contentScale = ContentScale.Crop
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(0xFFF44336),
            titleContentColor = Color.Black
        )
    )
}
