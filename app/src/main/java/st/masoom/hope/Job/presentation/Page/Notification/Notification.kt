package st.masoom.hope.Job.presentation.Page.Notification

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

// FCM API Interface
interface FCMService {
    @Headers("Content-Type: application/json")
    @POST("fcm/send")
    fun sendNotification(
        @Body notification: FCMNotification,
        @Header("Authorization") serverKey: String // Pass at runtime
    ): Call<Void>
}
// Data Classes for FCM Payload
data class FCMNotification(
    val to: String,
    val notification: NotificationData
)

data class NotificationData(
    val title: String,
    val body: String
)

fun sendJobNotification(jobTitle: String, firestore: FirebaseFirestore) {
    firestore.collection("users").get().addOnSuccessListener { documents ->
        for (document in documents) {
            val token = document.getString("fcmToken")
            if (token != null) {
                sendNotificationToUser(token, jobTitle)
            }
        }
    }.addOnFailureListener { exception ->
        Log.e("FCM", "Error getting user tokens", exception)
    }
}


fun sendNotificationToUser(userToken: String, jobTitle: String) {
    val retrofit = Retrofit.Builder()
        .baseUrl("https://fcm.googleapis.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val service = retrofit.create(FCMService::class.java)

    val notification = FCMNotification(
        to = userToken,
        notification = NotificationData(
            title = "New Job Posted!",
            body = "A new job for $jobTitle is available."
        )
    )

    val serverKey = "a764b0420dec11b1b22cb38b6b9422e06f8aae3e" // Load this from a secure source

    service.sendNotification(notification, serverKey).enqueue(object : Callback<Void> {
        override fun onResponse(call: Call<Void>, response: Response<Void>) {
            Log.d("FCM", "Notification sent successfully! Response: ${response.code()}")
        }

        override fun onFailure(call: Call<Void>, t: Throwable) {
            Log.e("FCM", "Error sending notification", t)
        }
    })
}

