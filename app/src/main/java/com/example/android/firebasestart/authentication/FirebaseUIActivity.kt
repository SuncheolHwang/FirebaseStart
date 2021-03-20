package com.example.android.firebasestart.authentication

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.android.firebasestart.R
import com.example.android.firebasestart.databinding.ActivityFirebaseUiBinding
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse

class FirebaseUIActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        const val RC_SIGN_IN = 1000
    }

    private lateinit var binding: ActivityFirebaseUiBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFirebaseUiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.firebaseuiauthbtn.setOnClickListener(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)

            if (resultCode == RESULT_OK) {
//                val user = FirebaseAuth.getInstance().currentUser
                val intent = Intent(this, SignedInActivity::class.java)
                intent.putExtra("FirebaseUI", data)
                startActivity(intent)
            } else {

            }
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.firebaseuiauthbtn -> {
                signIn()
            }
        }
    }

    /**
     * 인증요청청
    */

    private fun signIn() {
        startActivityForResult(
            AuthUI.getInstance().createSignInIntentBuilder()
                .setTheme(getSelectedTheme())
                .setLogo(getSelectedLogo())
                .setAvailableProviders(getSelectedProviders())
                .setTosAndPrivacyPolicyUrls("https://naver.com", "https://google.com")
                .setIsSmartLockEnabled(true)
                .build(),
            RC_SIGN_IN)
    }

    /**
     * 테마 정보
     */
    private fun getSelectedTheme() = AuthUI.getDefaultTheme()

    /**
     * 로고 이미지
     */
    private fun getSelectedLogo() = AuthUI.NO_LOGO

    /**
     * 인증 서비스
     */
    private fun getSelectedProviders(): List<AuthUI.IdpConfig> {
        val selectedProviders = mutableListOf<AuthUI.IdpConfig>()

        binding.apply {
            if (googleProvider.isChecked) {
                selectedProviders.add(AuthUI.IdpConfig.GoogleBuilder().build())
            }
            if (facebookProvider.isChecked) {
                selectedProviders.add(AuthUI.IdpConfig.FacebookBuilder().build())
            }
            if (twitterProvider.isChecked) {
                selectedProviders.add(AuthUI.IdpConfig.TwitterBuilder().build())
            }
            if (emailProvider.isChecked) {
                selectedProviders.add(AuthUI.IdpConfig.EmailBuilder().build())
            }
        }

        return selectedProviders
    }
}