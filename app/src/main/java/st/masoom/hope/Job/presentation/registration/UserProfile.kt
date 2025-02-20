package st.masoom.hope.Job.presentation.registration

data class UserProfile(
    val userId: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val role: String = "",
    val profilePicBase64: String = "" // Store profile picture in Base64 format
)

