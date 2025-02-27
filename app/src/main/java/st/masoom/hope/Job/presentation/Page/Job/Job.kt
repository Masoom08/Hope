package st.masoom.hope.Job.presentation.Page.Job

import com.google.firebase.firestore.ServerTimestamp
import java.util.*

data class Job(
    val jobId: String = "",
    val title: String = "",
    val description: String = "",
    val salary: String = "",
    val location: String = "",
    val type: String = "",
    val employerId: String = "",
    @ServerTimestamp val createdAt: Date? = null,
    val applicants: List<String> = emptyList()
)
