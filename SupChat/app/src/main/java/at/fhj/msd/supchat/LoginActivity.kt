package at.fhj.msd.supchat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.rengwuxian.materialedittext.MaterialEditText

class LoginActivity : AppCompatActivity() {

    lateinit var password: MaterialEditText
    lateinit var email: MaterialEditText
    lateinit var btn_login: Button

    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        var toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setTitle("Login")
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        auth = Firebase.auth

        email = findViewById(R.id.email)
        password = findViewById(R.id.password)
        btn_login = findViewById(R.id.btn_login)

        btn_login.setOnClickListener(View.OnClickListener {
            var txt_email: String = email.text.toString()
            var txt_password: String = password.text.toString()

            if (TextUtils.isEmpty(txt_email) or TextUtils.isEmpty(txt_password)){
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT)
            }else{
                auth.signInWithEmailAndPassword(txt_email, txt_password).addOnCompleteListener(
                    OnCompleteListener { task ->
                        if (task.isSuccessful){
                            var intent = Intent(this, MainActivity::class.java).apply {
                                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                            }
                            startActivity(intent)
                        } else {
                            Toast.makeText(this, "Authentification failed!", Toast.LENGTH_SHORT)
                        }
                    })
            }
        })

    }
}
