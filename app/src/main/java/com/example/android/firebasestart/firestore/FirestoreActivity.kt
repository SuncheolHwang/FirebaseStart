package com.example.android.firebasestart.firestore

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.example.android.firebasestart.R
import com.example.android.firebasestart.databinding.ActivityFirestoreBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class FirestoreActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityFirestoreBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFirestoreBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            firestoreAddDataBtn.setOnClickListener(this@FirestoreActivity)
            firestoreSetDataBtn.setOnClickListener(this@FirestoreActivity)
            firestoreDeleteDocBtn.setOnClickListener(this@FirestoreActivity)
            firestoreDelteFieldBtn.setOnClickListener(this@FirestoreActivity)
            firestoreSeldataBtn.setOnClickListener(this@FirestoreActivity)
            firestoreSelWhereDataBtn.setOnClickListener(this@FirestoreActivity)
            firestoreListenerDataBtn.setOnClickListener(this@FirestoreActivity)
            firestoreListenerQueryDataBtn.setOnClickListener(this@FirestoreActivity)
        }
    }

    override fun onClick(v: View?) {
        val user = FirebaseAuth.getInstance().currentUser
        if (user == null) {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("경고")
            builder.setMessage("사용자 인증이 되지 않았습니다. Firebase 인증에서 로그인 후 사용하세요.")
            builder.setPositiveButton("확인", object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface?, which: Int) {

                }
            })
            builder.show()
            return
        }

        when (v?.id) {
            R.id.firestoreAddDataBtn -> addData()
            R.id.firestoreSetDataBtn -> setData()
            R.id.firestoreDeleteDocBtn -> deleteDoc()
            R.id.firestoreDelteFieldBtn -> deleteField()
            R.id.firestoreSeldataBtn -> selectDoc()
            R.id.firestoreSelWhereDataBtn -> selectWhere()
            R.id.firestoreListenerDataBtn -> listenerDoc()
            R.id.firestoreListenerQueryDataBtn -> listenerQueryDoc()
        }
    }

    private fun addData() {
        val db = FirebaseFirestore.getInstance()

        val member: HashMap<String, Any> = hashMapOf()
        member["name"] = "홍길동"
        member["address"] = "수원시"
        member["age"] = 25
        member["id"] = "hong"
        member["pwd"] = "hello!"

        db.collection("user")
            .add(member)
            .addOnSuccessListener { documentReference ->
                Log.d("namjinha", "Document ID = ${documentReference.get()}")
            }
            .addOnFailureListener {
                Log.d("namjinha", "Document Error!!")
            }
    }

    private fun setData() {
        val db = FirebaseFirestore.getInstance()

        val member: HashMap<String, Any> = hashMapOf()
        member["name"] = "나야나"
        member["address"] = "경기도"
        member["age"] = 25
        member["id"] = "my"
        member["pwd"] = "hello!"

        db.collection("user")
            .document("userinfo")
            .set(member)
            .addOnSuccessListener {
                Log.d("namjinha", "Document successfully written!")
            }
            .addOnFailureListener {
                Log.d("namjinha", "Document Error!!")
            }
    }

    private fun deleteDoc() {
        val db = FirebaseFirestore.getInstance()
        db.collection("user").document("userinfo")
            .delete()
            .addOnSuccessListener {
                Log.d("namjinha", "Document successfully deleted!")
            }
            .addOnFailureListener {
                Log.w("namjinha", "Error deleting document", it)
            }
    }

    private fun deleteField() {
        val db = FirebaseFirestore.getInstance()

        val docRef = db.collection("user").document("userinfo")

        val updates: HashMap<String, Any> = hashMapOf()
        updates["address"] = FieldValue.delete()

        docRef.update(updates)
            .addOnCompleteListener {
                Log.d("namjinha", "DocumentSnapshot successfully deleted!")
            }
    }

    private fun selectDoc() {
        val db = FirebaseFirestore.getInstance()

        val docRef = db.collection("user").document("userinfo")
        docRef.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val document = task.result
                if (document!!.exists()) {
                    Log.d("namjinha", "DocumentSnapshot data: ${document.data}")

                    val userInfo = document.toObject(UserInfo::class.java)
                    Log.d("namjinha", "name = ${userInfo?.name}")
                    Log.d("namjinha", "address = ${userInfo?.address}")
                    Log.d("namjinha", "id = ${userInfo?.id}")
                    Log.d("namjinha", "pwd = ${userInfo?.pwd}")
                    Log.d("namjinha", "age = ${userInfo?.age}")
                } else {
                    Log.d("namjinha", "No such document")
                }
            } else {
                Log.d("namjinha", "get failed with", task.exception)
            }
        }
    }

    private fun selectWhere() {
        val db = FirebaseFirestore.getInstance()
        db.collection("user")
                .whereEqualTo("age", 25)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        for (document in task.result!!) {
                            Log.d("namjinha", "${document.id} => ${document.data}")

                            val userInfo = document.toObject(UserInfo::class.java)
                            Log.d("namjinha", "name = ${userInfo.name}")
                            Log.d("namjinha", "address = ${userInfo.address}")
                            Log.d("namjinha", "id = ${userInfo.id}")
                            Log.d("namjinha", "pwd = ${userInfo.pwd}")
                            Log.d("namjinha", "age = ${userInfo.age}")
                        }
                    } else {
                        Log.d("namjinha", "get failed with", task.exception)
                    }
                }
    }

    private fun listenerDoc() {
        val db = FirebaseFirestore.getInstance()
        val docRef = db.collection("user").document("userinfo")
        docRef.addSnapshotListener { value, error ->
            if (error != null) {
                Log.w("namjinha", "Listen failed.", error)
                return@addSnapshotListener
            }

            if (value != null && value.exists()) {
                Log.d("namjinha", "Current data: ${value.data}")
            } else {
                Log.d("namjinha", "Current data: null")
            }
        }
    }

    private fun listenerQueryDoc() {
        Log.d("namjinha", "listenerQueryDoc in")

        val db = FirebaseFirestore.getInstance()
        db.collection("user")
                .whereEqualTo("id", "hong")
                .addSnapshotListener { value, error ->
                    Log.d("namjinha", "listenerQueryDoc in 1")

                    if (error != null) {
                        Log.w("namjinha", "listen:error", error)
                        return@addSnapshotListener
                    }

                    for (dc in value!!.documentChanges) {
                        Log.d("namjinha", "listenerQueryDoc dc.getType() = ${dc.type}")

                        when (dc.type) {
                            DocumentChange.Type.ADDED -> Log.d("namjinha", "New city: ${dc.document.data}")
                            DocumentChange.Type.MODIFIED -> Log.d("namjinha", "Modified city: ${dc.document.data}")
                            DocumentChange.Type.REMOVED -> Log.d("namjinha", "Removed city: ${dc.document.data}")
                        }
                    }
                }
    }
}