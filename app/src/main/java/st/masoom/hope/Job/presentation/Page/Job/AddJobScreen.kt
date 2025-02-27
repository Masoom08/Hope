package st.masoom.hope.Job.presentation.Page.Job

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import st.masoom.hope.Job.presentation.Page.Notification.sendJobNotification
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddJobScreen(navController: NavController) {
    val context = LocalContext.current
    val firestore = FirebaseFirestore.getInstance()

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var salary by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf("All") }
    var location by remember { mutableStateOf("") }
    val employerId = UserManager.currentUser?.userId ?: ""

    val jobTypes = listOf("All", "Full-Time", "Part-Time", "Remote", "Internship")
    var expanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Add Job") }) }
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

            Box {
                OutlinedTextField(
                    value = selectedType,
                    onValueChange = {},
                    label = { Text("Job Type") },
                    readOnly = true,
                    trailingIcon = {
                        IconButton(onClick = { expanded = true }) {
                            Icon(Icons.Default.ArrowDropDown, contentDescription = "Dropdown")
                        }
                    }
                )
                DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    jobTypes.forEach { type ->
                        DropdownMenuItem(text = { Text(type) }, onClick = { selectedType = type; expanded = false })
                    }
                }
            }

            OutlinedTextField(value = location, onValueChange = { location = it }, label = { Text("Location") })

            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    addJobToFirestore(title, description, salary, selectedType, location, employerId, firestore, context, navController)
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3498DB)),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Post Job")
            }
        }
    }
}

fun addJobToFirestore(
    title: String, description: String, salary: String, type: String, location: String,
    employerId: String, firestore: FirebaseFirestore, context: android.content.Context, navController: NavController
) {
    val jobId = firestore.collection("jobs").document().id
    val newJob = mapOf(
        "jobId" to jobId,
        "title" to title,
        "description" to description,
        "salary" to salary,
        "type" to type,
        "location" to location,
        "employerId" to employerId,
        "createdAt" to Date(),
        "applicants" to emptyList<String>()
    )

    firestore.collection("jobs").document(jobId)
        .set(newJob)
        .addOnSuccessListener {
            firestore.collection("locations").document(location)
                .set(mapOf("name" to location), SetOptions.merge())

            Toast.makeText(context, "Job posted!", Toast.LENGTH_LONG).show()
            navController.popBackStack()

            // 🔹 Send notification to job seekers
            sendJobNotification(title, firestore)
        }
        .addOnFailureListener {
            Toast.makeText(context, "Error posting job: ${it.message}", Toast.LENGTH_LONG).show()
        }
}


// Function to remove a job (Only the Employer can delete)
fun removeJob(jobId: String, firestore: FirebaseFirestore, context: android.content.Context) {
    val jobRef = firestore.collection("jobs").document(jobId)

    jobRef.get()
        .addOnSuccessListener { document ->
            val employerId = document.getString("employerId")
            val currentUserId = UserManager.currentUser?.userId ?: ""

            if (employerId == currentUserId) {
                jobRef.delete()
                    .addOnSuccessListener {
                        Toast.makeText(context, "Job removed successfully!", Toast.LENGTH_LONG).show()
                    }
                    .addOnFailureListener {
                        Toast.makeText(context, "Error removing job: ${it.message}", Toast.LENGTH_LONG).show()
                    }
            } else {
                Toast.makeText(context, "You are not authorized to remove this job.", Toast.LENGTH_LONG).show()
            }
        }
        .addOnFailureListener {
            Toast.makeText(context, "Error fetching job details: ${it.message}", Toast.LENGTH_LONG).show()
        }
}
