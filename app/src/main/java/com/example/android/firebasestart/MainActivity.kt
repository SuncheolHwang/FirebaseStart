package com.example.android.firebasestart

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.android.firebasestart.authentication.AuthActivity
import com.example.android.firebasestart.databinding.ActivityMainBinding
import com.example.android.firebasestart.firestore.FirestoreActivity
import com.example.android.firebasestart.readtimedb.MemoActivity

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.firebaseauthbtn.setOnClickListener(this)
        binding.firebaseRealTimeDbBtn.setOnClickListener(this)
        binding.firebaseCloudFireStoreBtn.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.firebaseauthbtn -> {
                val intent = Intent(this, AuthActivity::class.java)
                startActivity(intent)
            }
            R.id.firebaseRealTimeDbBtn -> {
                val intent = Intent(this, MemoActivity::class.java)
                startActivity(intent)
            }
            R.id.firebaseCloudFireStoreBtn -> {
                val intent = Intent(this, FirestoreActivity::class.java)
                startActivity(intent)
            }
        }
    }
}