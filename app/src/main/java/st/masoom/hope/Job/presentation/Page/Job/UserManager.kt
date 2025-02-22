package st.masoom.hope.Job.presentation.Page.Job

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

object UserManager {
    var currentUser: User? = null

    fun loadCurrentUser(onComplete: (() -> Unit)? = null) {
        val firebaseUser = FirebaseAuth.getInstance().currentUser
        if (firebaseUser != null) {
            FirebaseFirestore.getInstance().collection("users")
                .document(firebaseUser.uid)
                .get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        currentUser = document.toObject(User::class.java)
                        Log.d("UserManager", "User data loaded: $currentUser")
                    } else {
                        Log.e("UserManager", "User document not found in Firestore")
                    }
                    onComplete?.invoke()  // Callback if needed
                }
                .addOnFailureListener { e ->
                    Log.e("UserManager", "Error loading user data: ${e.message}")
                    onComplete?.invoke()
                }
        }
    }
}

data class User(
    val userId: String = "",
    val role: String = "" // "Employer" or "Job Seeker"
)
