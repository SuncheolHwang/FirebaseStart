package com.example.android.firebasestart.readtimedb

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.android.firebasestart.R
import com.example.android.firebasestart.databinding.ActivityMemoBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import java.util.*
import kotlin.collections.ArrayList

class MemoActivity : AppCompatActivity(), View.OnClickListener, MemoViewListener {
    private lateinit var binding: ActivityMemoBinding

    lateinit var memoItems: ArrayList<MemoItem>
    lateinit var memoAdapter: MemoAdapter

    lateinit var username: String
    val firebaseDatabase = Firebase.database
    val databaseReference = firebaseDatabase.reference
    val mUser = FirebaseAuth.getInstance().currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMemoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
        initView()
    }

    override fun onStart() {
        super.onStart()

        addChildEvent()
        addValueEventListener()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.memoBtn -> regMemo()
            R.id.reguser -> writeNewUser()
        }
    }

    override fun onItemClick(position: Int, view: View) {

    }

    private fun init() {
        memoItems = arrayListOf()

        username = "user_${Random().nextInt(1000)}"
    }

    private fun initView() {
        binding.memoBtn.setOnClickListener(this)
        binding.reguser.setOnClickListener(this)

        memoAdapter = MemoAdapter(this, memoItems, this)
        binding.memoList.also {
            it.layoutManager = LinearLayoutManager(this)
            it.adapter = memoAdapter
        }
    }

    private fun regMemo() {
        if (binding.memoTitle.text.toString().isEmpty() ||
                binding.memoContents.text.toString().isEmpty()) {
            Toast.makeText(this, "메모 제목 또는 메모 내용이 입력되지 않았습니다. 입력 후 다시 시작해주세요.",
                    Toast.LENGTH_LONG).show()
            return
        }

        val item = MemoItem()
        item.user = username
        item.memoTitle = binding.memoTitle.text.toString()
        item.memoContents = binding.memoContents.text.toString()

        databaseReference.child("memo").child(mUser.uid).push().setValue(item)
    }

    private fun writeNewUser() {
        val user = FirebaseAuth.getInstance().currentUser

        if (user != null) {
            val name = user.displayName
            val email = user.email
            val uid = user.uid
            Log.d("namjinha", "name = $name")
            Log.d("namjinha", "email = $email")
            Log.d("namjinha", "uid = $uid")

            val userInfo = UserInfo()
            userInfo.userpwd = "1234"
            userInfo.username = name
            userInfo.emailaddr = email

            databaseReference.child("users").child(uid).setValue(userInfo)
        } else {
            Log.d("namjinha", "user null")
        }
    }

    private fun addChildEvent() {
        databaseReference.child("memo").child("uid").addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                Log.d("namjinha", "addChildEvent in")
                val item = snapshot.getValue<MemoItem>()

                if (item != null) {
                    memoItems.add(item)
                }
                memoAdapter.notifyDataSetChanged()
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) { }

            override fun onChildRemoved(snapshot: DataSnapshot) { }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) { }

            override fun onCancelled(error: DatabaseError) { }
        })
    }

    private fun addValueEventListener() {
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d("namjinha", "Value = ${snapshot.value}")
                val item = snapshot.getValue(MemoItem::class.java)

                if (item != null) {
                    memoItems.add(item)
                }
                memoAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) { }
        })
    }
}