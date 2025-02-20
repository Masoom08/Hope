package st.masoom.hope.Job.presentation.Page.Profile

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import st.masoom.hope.Job.presentation.sign_in.UserData

// Fetch User Profile from Firestore
fun FetchUserProfile(userId: String, onResult: (Map<String, Any>?) -> Unit) {
    val db = FirebaseFirestore.getInstance()

    db.collection("users").document(userId).get()
        .addOnSuccessListener { document ->
            if (document.exists()) {
                val userMap = document.data // This retrieves the map as stored
                onResult(userMap)
            } else {
                onResult(null) // User not found
            }
        }
        .addOnFailureListener {
            onResult(null) // Error fetching data
        }
}
