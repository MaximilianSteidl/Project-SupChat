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
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.rengwuxian.materialedittext.MaterialEditText
import java.lang.ref.PhantomReference

class RegisterActivity : AppCompatActivity() {

    lateinit var username: MaterialEditText
    lateinit var password: MaterialEditText
    lateinit var email: MaterialEditText
    lateinit var btn_register:Button

    lateinit var auth:FirebaseAuth
    lateinit var reference: DatabaseReference
    lateinit var userID:String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_activity)

        var toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setTitle("Register")
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        username=findViewById(R.id.username)
        email=findViewById(R.id.email)
        password=findViewById(R.id.password)
        btn_register=findViewById(R.id.btn_register)

        auth= Firebase.auth

        btn_register.setOnClickListener(View.OnClickListener {
            var txt_username = username.text.toString()
            var txt_email = email.text.toString()
            var txt_password = password.text.toString()

            if (TextUtils.isEmpty(txt_username) or TextUtils.isEmpty(txt_email) or TextUtils.isEmpty(txt_password)){
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT)
            }else if (txt_password.length < 6){
                Toast.makeText(this, "Password must be at 6 characters", Toast.LENGTH_SHORT)
            }else{
                register(txt_username, txt_email, txt_password);
            }
        })
    }

    private fun register(username:String, email:String, password:String){

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this){ task ->
                if(task.isSuccessful){
                    var firebaseUser = auth.currentUser
                    userID= firebaseUser!!.uid

                    reference=FirebaseDatabase.getInstance().getReference("Users").child(userID)

                    val hashmap:HashMap<String,String> = HashMap<String, String>()
                    hashmap.put("id",userID)
                    hashmap.put("username", username)
                    hashmap.put("imageURL", "default")

                    reference.setValue(hashmap).addOnCompleteListener(this){ task ->
                        if(task.isSuccessful){
                            var intent = Intent(this, MainActivity::class.java).apply {
                                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                            }
                            startActivity(intent)

                        }
                    }
                }else{
                    Toast.makeText(this, "You can't register with this Email or Password!", Toast.LENGTH_SHORT )
                }
            }
    }


}
