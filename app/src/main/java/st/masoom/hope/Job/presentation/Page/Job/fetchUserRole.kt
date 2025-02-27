package st.masoom.hope.Job.presentation.Page.Job

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore

fun fetchUserRole(userId: String, onRoleFetched: (String) -> Unit) {
    FirebaseFirestore.getInstance().collection("users").document(userId)
        .get()
        .addOnSuccessListener { document ->
            val role = document.getString("role") ?: "Job Seeker"  // Default to "Job Seeker"
            onRoleFetched(role)  // Pass the role back to UI
        }
        .addOnFailureListener {
            Log.e("UserFetch", "Error getting user role: ${it.message}")
            onRoleFetched("Job Seeker") // Default if fetch fails
        }
}
