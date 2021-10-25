package com.example.mobapps

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import androidx.viewpager.widget.ViewPager
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import com.example.mobapps.ui.main.SectionsPagerAdapter
import com.example.mobapps.databinding.ActivityMainBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(null, "onCreate() for Second Fragment")
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //TODO: Change this to an individual fragment
        //Login/Register Button
        val button = findViewById<Button>(R.id.registerBtn)
        button.setOnClickListener {

            val firstName = binding.firstName.text.toString()
            val lastName = binding.lastName.text.toString()
            val age = binding.age.text.toString()
            val userName = binding.userName.text.toString()

            database = FirebaseDatabase.getInstance().getReference("User")
            val User = User(firstName, lastName, age, userName)
            database.child(userName).setValue(User).addOnSuccessListener {

                binding.firstName.text.clear()
                binding.lastName.text.clear()
                binding.age.text.clear()
                binding.userName.text.clear()

                Toast.makeText(this, "Success!", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
            }

        }

        val button2 = findViewById<Button>(R.id.nextPageBtn)
        button2.setOnClickListener{
            val intent = Intent(this, ReadData::class.java)
            startActivity(intent)
        }

        binding.readdataBtn.setOnClickListener {

            val userName : String = binding.etusername.text.toString()
            if  (userName.isNotEmpty()){

                readData(userName)

            }else{

                Toast.makeText(this,"PLease enter Username",Toast.LENGTH_SHORT).show()

            }

        }
        binding.updateBtn.setOnClickListener{
            val userName = binding.userName.text.toString()
            val firstName = binding.firstName.text.toString()
            val lastName = binding.lastName.text.toString()
            val age = binding.age.text.toString()

            updateData(userName,firstName,lastName,age)
        }
        binding.deldataBtn.setOnClickListener{
            val userName : String = binding.etusername.text.toString()
            if  (userName.isNotEmpty()){

                delData(userName)

            }else{

                Toast.makeText(this,"PLease enter Username",Toast.LENGTH_SHORT).show()

            }
        }

    }

    private fun readData(userName: String) {

        database = FirebaseDatabase.getInstance().getReference("User")
        database.child(userName).get().addOnSuccessListener {

            if (it.exists()){

                val firstname = it.child("firstName").value
                val lastName = it.child("lastName").value
                val age = it.child("age").value
                binding.etusername.text.clear()
                binding.tvFirstName.text = firstname.toString()
                binding.tvLastName.text = lastName.toString()
                binding.tvAge.text = age.toString()
                Toast.makeText(this,"Successfully Read",Toast.LENGTH_SHORT).show()

            }else{

                Toast.makeText(this,"User Doesn't Exist",Toast.LENGTH_SHORT).show()


            }

        }.addOnFailureListener{

            Toast.makeText(this,"Failed",Toast.LENGTH_SHORT).show()


        }



    }
    private fun updateData(userName: String, firstName: String, lastName: String, age: String) {

        database = FirebaseDatabase.getInstance().getReference("User")
        val user = mapOf<String,String>(
            "firstName" to firstName,
            "lastName" to lastName,
            "age" to age
        )

        database.child(userName).updateChildren(user).addOnSuccessListener {

            binding.userName.text.clear()
            binding.firstName.text.clear()
            binding.lastName.text.clear()
            binding.age.text.clear()
            Toast.makeText(this,"Successfully Updated",Toast.LENGTH_SHORT).show()


        }.addOnFailureListener{

            Toast.makeText(this,"Failed to Update",Toast.LENGTH_SHORT).show()

        }}
    private fun delData(userName: String){
        database = FirebaseDatabase.getInstance().getReference("User")
        //Delete here
        database.child(userName).removeValue().addOnSuccessListener {
            binding.etusername.text.clear()
            Toast.makeText(this,"Successfully Deleted",Toast.LENGTH_SHORT).show()
                //Toast.makeText(this,"User Doesn't Exist",Toast.LENGTH_SHORT).show()

        }.addOnFailureListener{

            Toast.makeText(this,"Failed",Toast.LENGTH_SHORT).show()


        }
    }




//        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
//        val viewPager: ViewPager = binding.viewPager
//        viewPager.adapter = sectionsPagerAdapter
//        val tabs: TabLayout = binding.tabs
//        tabs.setupWithViewPager(viewPager)
//        val fab: FloatingActionButton = binding.fab
//
//        fab.setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                    .setAction("Action", null).show()
//        }

}