package com.example.android.firebasestart.authentication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.android.firebasestart.R
import com.example.android.firebasestart.databinding.ActivityAuthBinding
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth

class AuthActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityAuthBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.firebaseui.setOnClickListener(this)
        binding.firebaseSignOut.setOnClickListener(this)

        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            binding.firebaseui.isEnabled = false
            binding.firebaseSignOut.isEnabled = true
        } else {
            binding.firebaseui.isEnabled = true
            binding.firebaseSignOut.isEnabled = false
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.firebaseui -> {
                val intent = Intent(this, FirebaseUIActivity::class.java)
                startActivity(intent)
            }
            R.id.firebaseSignOut -> {
                signOut()
            }
        }
    }

    private fun signOut() {
        Log.d("SignedInActivity", "signOut()")
        AuthUI.getInstance()
            .signOut(this)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    finish()
                }
            }
    }
}