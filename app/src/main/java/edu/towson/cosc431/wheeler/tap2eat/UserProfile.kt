package edu.towson.cosc431.wheeler.tap2eat

import android.content.ComponentName
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.android.synthetic.main.activity_user_profile.*
import kotlinx.android.synthetic.main.common_action_bar.*

class UserProfile : AppCompatActivity(), IHasActionBar {

    val user = FirebaseAuth.getInstance().currentUser
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        setSupportActionBar(my_toolbar)

        logoutBtn.setOnClickListener{logout()}

        welcome_message.setText("Hi,"+ user?.getEmail())

            f_name_input.setText(user?.displayName)
//            password_input.setText(user?.)
                phone_input.setText(user?.phoneNumber)
        val profileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName(" Usertest")
                .build()


        user?.updateProfile(profileUpdates)
                ?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d("Tag", "User profile updated.")
                    }
                }
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
        }
        return true
    }

    private fun logout() {



        if(user != null){

            FirebaseAuth.getInstance().signOut();

            intent = Intent()
            intent.component = ComponentName(this,activity_login::class.java)
            startActivity(intent)

        }else {

            intent.component = ComponentName(this,activity_home::class.java)
            startActivity(intent)
        }
    }

    override fun launchProfile() {
        intent = Intent()
        intent.component = ComponentName(this, UserProfile::class.java)
        startActivity(intent)
    }

    override fun launchHome() {
        intent = Intent()
        intent.component = ComponentName(this, activity_home::class.java)
        startActivity(intent)
    }

}
