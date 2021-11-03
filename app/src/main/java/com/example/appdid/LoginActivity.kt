package com.example.appdid

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.appdid.databinding.ActivityLoginBinding
import com.example.appdid.databinding.ActivityMainBinding
import com.example.appdid.utility.MyApplication
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class LoginActivity : AppCompatActivity() {

    private var mBindnig:ActivityLoginBinding? =null // 여기서 타입이 .xml 이름 +Binding임,즉 activity_second이면 ActivitySecondBinding
    private val  binding get() = mBindnig!!
    private lateinit var loginResultLauncher: ActivityResultLauncher<Intent> // 콜백 Launch
    private val firebaseAuth=FirebaseAuth.getInstance() //Auth 설정

    override fun onStart() {
        super.onStart()
        loginResultLauncher=registerForActivityResult(ActivityResultContracts.StartActivityForResult())
        {
            if(it.resultCode== RESULT_OK) // 로그인 인텐트 For Result
            {
                val result= Auth.GoogleSignInApi.getSignInResultFromIntent(it.data)
                result.let {
                    if (it!!.isSuccess) {
                        it.signInAccount?.displayName //이름
                        it.signInAccount?.email //이메일
                        Log.e("Value", it.signInAccount?.email!!)
                        firebaseLogin(result!!.signInAccount!!)
                        MyApplication.prefs.setString("name",it.signInAccount!!.displayName!!) //이름
                        MyApplication.prefs.setString("email",it.signInAccount!!.email!!) //email
                        val intent:Intent=Intent(applicationContext,MainActivity::class.java)
                        Toast.makeText(this,"Google Login Success",Toast.LENGTH_SHORT).show()
                        startActivity(intent)

                        // 기타 등등
                    } else  {
                        Log.e("Value", "error")
                        // 에러 처리
                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBindnig= ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.signInBtn.setOnClickListener {
            loginResultLauncher.launch(googleSignInIntent) //로그인 작업 시작

        }

    }

    private val googleSignInIntent by lazy {
        //인텐트 생성
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build()
        GoogleSignIn.getClient(this, gso).signInIntent

    }
    private fun firebaseLogin(googleAccount: GoogleSignInAccount) { // FireBase에 Login 정보 저장

        val credential = GoogleAuthProvider.getCredential(googleAccount.idToken, null)

        firebaseAuth.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful) {
                it.result?.user?.displayName //사용자 이름
            } else {
                //error 처리
            }
        }.addOnFailureListener {
            //error 처리
        }
    }

}