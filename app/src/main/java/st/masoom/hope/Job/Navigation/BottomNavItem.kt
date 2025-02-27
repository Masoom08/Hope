package st.masoom.hope.Job.Navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(val route: String, val icon: ImageVector, val label: String) {
    object Home : BottomNavItem("home", Icons.Default.Home, "Home")
    object Jobs : BottomNavItem("jobs", Icons.Default.Email, "Jobs")
    object Applications : BottomNavItem("applications", Icons.Default.Lock, "Application")
    object Profile : BottomNavItem("profile", Icons.Default.Person, "Profile")
}
