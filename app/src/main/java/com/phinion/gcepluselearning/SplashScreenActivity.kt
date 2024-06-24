package com.phinion.gcepluselearning

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashScreenActivity : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        setContentView(R.layout.activity_splash_screen)

        mAuth = Firebase.auth

        CoroutineScope(Dispatchers.Main).launch {
            delay(1500)
            if (mAuth.currentUser != null) {
                //get user data from firebase
                startActivity(Intent(baseContext, MainActivity::class.java))
                finish()
            } else {
                startActivity(Intent(baseContext, SignUpActivity::class.java))
                finish()
            }
        }
    }
}
