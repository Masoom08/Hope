package st.masoom.hope.Job.presentation.Page.Job

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddJobScreen(navController: NavController) {
    val context = LocalContext.current
    val firestore = FirebaseFirestore.getInstance()

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var salary by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    val employerId = UserManager.currentUser?.userId ?: ""

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Add Job") })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Title") })
            OutlinedTextField(value = description, onValueChange = { description = it }, label = { Text("Description") })
            OutlinedTextField(value = salary, onValueChange = { salary = it }, label = { Text("Salary") })
            OutlinedTextField(value = location, onValueChange = { location = it }, label = { Text("Location") })

            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    addJobToFirestore(title, description, salary, location, employerId, firestore, context, navController)
                },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3498DB)), // Your theme color
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Post Job")
            }
        }
    }
}

fun addJobToFirestore(title: String, description: String, salary: String, location: String, employerId: String, firestore: FirebaseFirestore, context: android.content.Context, navController: NavController) {
    val jobId = firestore.collection("jobs").document().id
    val newJob = Job(
        jobId = jobId,
        title = title,
        description = description,
        salary = salary,
        location = location,
        employerId = employerId,
        createdAt = Date(),
        applicants = emptyList()
    )

    firestore.collection("jobs").document(jobId)
        .set(newJob)
        .addOnSuccessListener {
            Toast.makeText(context, "Job posted!", Toast.LENGTH_LONG).show()
            navController.popBackStack()
        }
        .addOnFailureListener {
            Toast.makeText(context, "Error posting job: ${it.message}", Toast.LENGTH_LONG).show()
        }
}
