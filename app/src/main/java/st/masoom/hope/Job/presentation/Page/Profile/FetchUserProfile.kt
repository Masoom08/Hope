package st.masoom.hope.Job.presentation.Page.Profile

import com.google.firebase.firestore.FirebaseFirestore

fun FetchUserProfile(userId: String, onResult: (Map<String, Any>?) -> Unit) {
    val db = FirebaseFirestore.getInstance()
    db.collection("users").document(userId).get()
        .addOnSuccessListener { document ->
            if (document.exists()) {
                val userData = document.data
                onResult(userData) // Correctly passing fetched data
            } else {
                println("No user profile found!")
                onResult(null)
            }
        }
        .addOnFailureListener { e ->
            println("Failed to fetch profile: ${e.message}")
            onResult(null)
        }
}