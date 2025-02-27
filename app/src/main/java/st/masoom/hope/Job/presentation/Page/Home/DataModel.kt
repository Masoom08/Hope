package st.masoom.hope.Job.presentation.Page.Home

data class JoobleRequest(val keywords: String, val location: String)
data class JoobleResponse(val jobs: List<JobItem>)
data class JobItem(
    val title: String,
    val company: String,
    val location: String,
    val salary: String,
    val snippet: String,
    val link: String = "" // Add this field
)
