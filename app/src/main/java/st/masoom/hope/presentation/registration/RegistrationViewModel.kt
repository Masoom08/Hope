package st.masoom.hope.presentation.registration

import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await


class RegistrationViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()

    suspend fun saveUserData(userId: String, fullName: String, phoneNumber: String) {
        val user = hashMapOf(
            "fullName" to fullName,
            "phoneNumber" to phoneNumber,
            "userId" to userId
        )
        db.collection("users").document(userId).set(user).await()
    }


}

