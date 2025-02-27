package st.masoom.hope.Job.presentation.Page.Home

import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.Call

interface JoobleApiService {
    @Headers("Content-Type: application/json")
    @POST("7eabc49a-7f54-4e40-8add-02949ee8ed1b") // Replace with your API key
    fun searchJobs(@Body request: JoobleRequest): Call<JoobleResponse>
}
