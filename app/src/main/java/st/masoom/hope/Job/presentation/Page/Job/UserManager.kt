package st.masoom.hope.Job.presentation.Page.Job

object UserManager {
    var currentUser: User? = null
}

data class User(
    val userId: String = "",
    val role: String = "" // "Employer" or "Job Seeker"
)
