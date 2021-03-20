package com.example.android.firebasestart.cloudstorage

import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.android.firebasestart.databinding.ActivityCloudStorageBinding

class CloudStorageActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityCloudStorageBinding

    companion object {
        const val REQUEST_CODE = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCloudStorageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.also {
            it.uploadBtn.setOnClickListener(this)
            it.downloadBtn.setOnClickListener(this)
            it.metaInfoBtn.setOnClickListener(this)
            it.deleteBtn.setOnClickListener(this)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_CODE)
                Toast.makeText(this,
                    "안드로이드 6.0부터 마시멜로부터 일부 권한에 대해 사용자에게 동의 필요!",
                    Toast.LENGTH_LONG).show()

                binding.apply {
                    uploadBtn.isEnabled = false
                    downloadBtn.isEnabled = false
                    metaInfoBtn.isEnabled = false
                    deleteBtn.isEnabled = false
                }
            }
        }
    }

    override fun onClick(v: View?) {
        TODO("Not yet implemented")
    }
}