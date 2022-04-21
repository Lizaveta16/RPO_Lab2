package com.lizaveta16.feedthecat.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.lizaveta16.feedthecat.extensions.Extensions.toast
import com.lizaveta16.feedthecat.R
import com.lizaveta16.feedthecat.databinding.ActivitySignInBinding


class SignInActivity : AppCompatActivity() {

    lateinit var binding : ActivitySignInBinding
    lateinit var launcher: ActivityResultLauncher<Intent>
    private lateinit var auth: FirebaseAuth
    private lateinit var user: FirebaseUser
    lateinit var userEmail: String
    lateinit var userPassword: String
    lateinit var inputsArray: Array<EditText>


    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        inputsArray = arrayOf(binding.etEmail, binding.etPassword)
        initButtons()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN)
            .requestServerAuthCode(getString(R.string.default_web_client_id))
            .build()

        googleSignInClient =  GoogleSignIn.getClient(this, gso)

        auth = Firebase.auth
        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
            try {
                val account = task.getResult(ApiException::class.java)
                if (account != null) {
                    firebaseAuthWithGoogle(account.idToken!!)
                }
            } catch (e: ApiException) {
                Log.d("MyLog", "Api exception")
            }
        }

        checkAuthState()
    }

    private fun initButtons() = with(binding){
        btnSignIn.setOnClickListener{
            signIn()
        }

        btnSignUp.setOnClickListener{
            signUp()
        }

        btnGoogleSignIn.setOnClickListener{
            signInWithGoogle()
        }
    }

    private fun signUp() = with(binding) {
        if (notEmpty()) {
            userEmail = etEmail.text.toString().trim()
            userPassword = etPassword.text.toString().trim()

            auth.createUserWithEmailAndPassword(userEmail, userPassword)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        startActivity(Intent(this@SignInActivity, MainActivity::class.java))
                        finish()
                    } else {
                        toast("Failed to authenticate!")
                    }
                }
        } else if (!notEmpty()) {
            inputsArray.forEach { input ->
                if (input.text.toString().trim().isEmpty()) {
                    input.error = "${input.hint} is required"
                }
            }
        } else {

        }
    }

    private fun signIn() = with(binding) {
        userEmail = etEmail.text.toString().trim()
        userPassword = etPassword.text.toString().trim()

        if (notEmpty()) {
            auth.signInWithEmailAndPassword(userEmail, userPassword)
                .addOnCompleteListener { signIn ->
                    if (signIn.isSuccessful) {
                        startActivity(Intent(this@SignInActivity, MainActivity::class.java))
                        finish()
                    } else {
                        toast("Sign in failed")
                    }
                }
        } else {
            inputsArray.forEach { input ->
                if (input.text.toString().trim().isEmpty()) {
                    input.error = "${input.hint} is required"
                }
            }
        }
    }

    private fun notEmpty(): Boolean = binding.etEmail.text.toString().trim().isNotEmpty() &&
            binding.etPassword.text.toString().trim().isNotEmpty()

    private fun getClient() : GoogleSignInClient {
        val gso = GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        return GoogleSignIn.getClient(this, gso)
    }

    private fun signInWithGoogle(){
        val signInClient = getClient()
        launcher.launch(signInClient.signInIntent)
    }

    private fun firebaseAuthWithGoogle(idToken : String){
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener(){
            if(it.isSuccessful){
                Log.d("MyLog", "GoogleSignIn")
                checkAuthState()
            } else {
                Log.d("MyLog", "SignIn error")
            }
        }
    }

//    private fun firebaseAuthWithPlayGames(acct: GoogleSignInAccount) {
//        //Log.d(TAG, "firebaseAuthWithPlayGames:" + acct.id!!)
//
//        val auth = Firebase.auth
//        val credential = PlayGamesAuthProvider.getCredential(acct.serverAuthCode!!)
//        auth.signInWithCredential(credential)
//            .addOnCompleteListener(this) { task ->
//                if (task.isSuccessful) {
//                    // Sign in success, update UI with the signed-in user's information
//                    //Log.d(TAG, "signInWithCredential:success")
//                    user = auth.currentUser
//                } else {
//                    // If sign in fails, display a message to the user.
//                    //Log.w(TAG, "signInWithCredential:failure", task.exception)
//                    Toast.makeText(baseContext, "Authentication failed.",
//                        Toast.LENGTH_SHORT).show()
//                }
//
//                // ...
//            }
//    }

    private fun checkAuthState(){
        if(auth.currentUser != null){
            val i = Intent(this, MainActivity::class.java)
            startActivity(i)
            finish()
        }
    }

}