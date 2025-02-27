package st.masoom.hope.Job.presentation.Page.Home

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://jooble.org/api/"

    val api: JoobleApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(JoobleApiService::class.java)
    }
}
