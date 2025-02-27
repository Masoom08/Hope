package st.masoom.hope.Job.Navigation

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun BottomNavigationBar(navController: NavController,
                        modifier: Modifier = Modifier
) {
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Jobs,
        BottomNavItem.Applications,
        BottomNavItem.Profile
    )

    NavigationBar(
        containerColor = Color(0xFF87CEFA) // Sky Blue color
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) },
                selected = currentRoute == item.route,
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.Black, // Black icon when selected
                    selectedTextColor = Color.Black, // Black text when selected
                    unselectedIconColor = Color.DarkGray, // Dark gray for unselected
                    unselectedTextColor = Color.DarkGray
                ),
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}
