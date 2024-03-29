package edu.towson.cosc431.wheeler.tap2eat

import android.content.ComponentName
import android.support.v7.app.AppCompatActivity
import android.os.Bundle

import com.google.firebase.auth.*
import android.widget.Toast
import android.content.Intent
import android.content.IntentFilter
import android.support.v4.content.LocalBroadcastManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.common_action_bar.*


class activity_login : AppCompatActivity(), IHasActionBar {
//    private lateinit var database: FirebaseDatabase
//    private lateinit var reference: DatabaseReference

    private lateinit var mAuth: FirebaseAuth
    private val broadcastReceiver = FoodOrderBroadcastReceiver()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mAuth = FirebaseAuth.getInstance()

//            database = FirebaseDatabase.getInstance()
//            reference = database.getReference("users")

        signInBtn.setOnClickListener{signIn()}

        setSupportActionBar(my_toolbar)

       signTv.setOnClickListener { launchSignup() }
    }

    override fun onResume() {
        val filter = IntentFilter(LauncherActivity.ACTION_ORDER)
        LocalBroadcastManager
                .getInstance(this)
                .registerReceiver(broadcastReceiver, filter)

        super.onResume()
    }

    override fun onPause() {
        LocalBroadcastManager
                .getInstance(this)
                .unregisterReceiver(broadcastReceiver)

        super.onPause()
    }

    private fun launchSignup() {

        intent = Intent()
        intent.component = ComponentName(this, activity_signup::class.java)
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.profile -> {
                launchProfile()
            }
            R.id.home -> {
                launchHome()
            }
            R.id.cart -> {
                launchCart()
            }
            else -> {
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                super.onOptionsItemSelected(item)
            }
        }
        return true
    }

    override fun launchProfile() {
        intent = Intent()
        intent.component = ComponentName(this, UserProfileActivity::class.java)
        startActivity(intent)
    }

    override fun launchHome() {
        intent = Intent()
        intent.component = ComponentName(this, activity_home::class.java)
        startActivity(intent)
    }

    override fun launchCart() {
        intent = Intent()
        intent.component = ComponentName(this, CartActivity::class.java)
        startActivity(intent)
    }


    private fun signIn() {
        //get user's email & password.
        val email = nameET.getText().toString().trim()
        val pass = passwordET.getText().toString().trim()

        //check if the email is empty
        if(email.isEmpty()){
            Toast.makeText(this,"Please Enter Your Email",Toast.LENGTH_SHORT).show();
                return
        }
        //check if the password is empty
        if(pass.isEmpty()){

            Toast.makeText(this,"Please Enter Your Password",Toast.LENGTH_SHORT).show();
            return

        }
        //connecting to DB & check if the user exists
        // https://firebase.google.com/docs/auth/android/start/ ->> for more details
        mAuth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {

                        val user = mAuth.currentUser
                        finish()
                        startActivity(Intent(applicationContext, activity_home::class.java))
                    } else {
                        Log.w("LoginActivity", "signInWithEmail:failure", task.exception)
                        Toast.makeText(this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show()

                    }
                }

    }

    //    private fun signIn() {
//
//
////                get user's email & password.
//        val email = nameET.getText().toString().trim()
//        val pass = passET.getText().toString().trim()
//        val user: User
//
//            //check if the email is empty
//            if(email.isEmpty()){
//                Toast.makeText(this,"Please Enter Your Email",Toast.LENGTH_SHORT).show();
//                    return
//            }
//            //check if the password is empty
//            if(pass.isEmpty()){
//
//                Toast.makeText(this,"Please Enter Your Password",Toast.LENGTH_SHORT).show();
//                return
//
//            }
//
//        // Read from the database
//        reference.addValueEventListener(object : ValueEventListener {
//
//
//            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                if (dataSnapshot.child(email.toString()).exists()) {
//
//
//                    // This method is called once with the initial value and again
//                    // whenever data at this location is updated.
//                    val user = dataSnapshot.getValue(User::class.java)
//
//                    if(user?.email.equals(email.toString())){
//
//                        finish()
//                        startActivity(Intent(applicationContext, activity_home::class.java))
//                       Log.d("tag","Signin Secussfully")
//
//                    }else{
//                        Log.d("tag","Signin Unsecussfully")
//                    }
//
//                } else{
//
//                }
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                // Failed to read value
////                Log.w(FragmentActivity.TAG, "Failed to read value.", error.toException())
//            }
//        })
//    }



}
