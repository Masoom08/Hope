package st.masoom.hope.Job.Navigation

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import androidx.compose.material3.TopAppBarDefaults


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(title: String) {
    TopAppBar(
        title = { Text(title, color = Color.Black) },
        colors = TopAppBarDefaults.mediumTopAppBarColors(containerColor = Color(0xFF87CEFA)) // Sky blue color
    )
}
