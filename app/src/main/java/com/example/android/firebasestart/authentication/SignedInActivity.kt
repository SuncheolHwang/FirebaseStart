package com.example.android.firebasestart.authentication

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.android.firebasestart.R
import com.example.android.firebasestart.databinding.ActivitySignedinBinding
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.*

class SignedInActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivitySignedinBinding

    var mIdpResponse: IdpResponse? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignedinBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser == null) {
            finish()
            return
        }

        Log.d("SignedInActivity", "Intent: $intent")
        mIdpResponse = IdpResponse.fromResultIntent(intent)
        Log.d("SignedInActivity", "mIdpResponse: $mIdpResponse")

        setContentView(binding.root)
        populateProfile()
        populateIdpToken()

        binding.signOut.setOnClickListener(this)
        binding.deleteAccount.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.sign_out -> signOut()
            R.id.delete_account -> deleteAccountClicked()
        }
    }

    private fun signOut() {
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        finish()
                    } else {
                    }
                }
    }

    private fun deleteAccountClicked() {
        val dialog = AlertDialog.Builder(this)
                .setMessage("Are you sure you wan to delete this account?")
                .setPositiveButton("Yes, nuke it!") { dialog, which
                    -> deleteAccount()
                }
                .setNegativeButton("No", null)
                .create()
    }

    private fun deleteAccount() {
        AuthUI.getInstance()
                .delete(this)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        finish()
                    } else {
                    }
                }
    }

    private fun populateProfile() {
        val user = FirebaseAuth.getInstance().currentUser

        binding.apply {
            userEmail.text = if(TextUtils.isEmpty(user.email)) "No email" else user.email
            userDisplayName.text = if(TextUtils.isEmpty(user.displayName)) "No display name" else user.displayName
        }

        val providerList = StringBuilder(100)
        providerList.append("Providers used: ")

        if (user.providerData == null || user.providerData.isEmpty()) {
            providerList.append("none")
        } else {
            for (profile in user.providerData) {
                var providerId = profile.providerId
                if (GoogleAuthProvider.PROVIDER_ID == providerId) {
                    providerList.append("Google")
                } else if (FacebookAuthProvider.PROVIDER_ID == providerId) {
                    providerList.append("Facebook")
                } else if (TwitterAuthProvider.PROVIDER_ID == providerId) {
                    providerList.append("Twitter")
                } else if (EmailAuthProvider.PROVIDER_ID == providerId) {
                    providerList.append("Email")
                } else {
                    providerList.append(providerId)
                }
            }
        }
        binding.userEnabledProvider.text = providerList
    }

    private fun populateIdpToken() {
        var token: String? = null

        if (mIdpResponse != null) {
            token = mIdpResponse!!.idpToken
        }

        if (token == null) {
            binding.idpTokenLayout.visibility = View.GONE
        } else {
            binding.idpToken.text = token
        }
    }
}