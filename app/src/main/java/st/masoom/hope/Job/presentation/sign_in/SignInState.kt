package st.masoom.hope.Job.presentation.sign_in

data class SignInState (
    val isSignInSuccessful :Boolean = false,
    val signInError : String? = null
)