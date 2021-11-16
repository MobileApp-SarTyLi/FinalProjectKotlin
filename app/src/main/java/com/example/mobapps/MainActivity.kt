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
import com.example.mobapps.databinding.CountrySelectBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;

import com.spotify.protocol.client.Subscription;
import com.spotify.protocol.types.Album
import com.spotify.protocol.types.PlayerState;
import com.spotify.protocol.types.Track;

class MainActivity : AppCompatActivity() {
    //Old Binding with Activity Main
    //private lateinit var binding: ActivityMainBinding
    private lateinit var binding: CountrySelectBinding
    // Firebase Database
    private lateinit var database: DatabaseReference
    // Keeping track of # of times USA Clicked
    private var usaClickCount = 0
    private var koreaClickCount = 0
    private var indiaClickCount = 0

    private var spotifyAppRemote: SpotifyAppRemote? = null
    var connectionParams = ConnectionParams.Builder(SpotifyUserCred.clientId)
        .setRedirectUri(SpotifyUserCred.redirectUri)
        .showAuthView(true)
        .build()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(null, "onCreate() for Second Fragment")
        binding = CountrySelectBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        val userName = "Test"
//        database = FirebaseDatabase.getInstance().getReference("User")
//        val User = User(0, 0,  0,userName)
//        database.child(userName).setValue(User)

        //TODO: Change this to an individual fragment
        //Login/Register Button
        val button = findViewById<Button>(R.id.USABtn)
        button.setOnClickListener {
            usaClickCount++

            val intent = Intent(this, ReadDataSpotify::class.java)
            intent.putExtra("playlistURI","spotify:playlist:37i9dQZEVXbLp5XoPON0wI")
            intent.putExtra("countryName", "USA")
            startActivity(intent)

            Toast.makeText(this, "You Picked USA!", Toast.LENGTH_SHORT).show()
        }

        val button2 = findViewById<Button>(R.id.KoreaBtn)
        button2.setOnClickListener{
            koreaClickCount++
            val intent = Intent(this, ReadDataSpotify::class.java)
            intent.putExtra("playlistURI","spotify:playlist:37i9dQZEVXbJZGli0rRP3r")
            intent.putExtra("countryName", "South Korea")
            startActivity(intent)

            Toast.makeText(this, "You Picked Korea!", Toast.LENGTH_SHORT).show()
        }

        val button3 = findViewById<Button>(R.id.IndiaBtn)
        button3.setOnClickListener{
            indiaClickCount++
            val intent = Intent(this, ReadDataSpotify::class.java)
            intent.putExtra("playlistURI","spotify:playlist:37i9dQZEVXbMWDif5SCBJq")
            intent.putExtra("countryName", "India")
            startActivity(intent)

            Toast.makeText(this, "You Picked India!", Toast.LENGTH_SHORT).show()
        }

    }

    override fun onStart() {
        super.onStart()
//        val connectionParams = ConnectionParams.Builder(clientId)
//            .setRedirectUri(redirectUri)
//            .showAuthView(true)
//            .build()

        // Update to Firebase
        database = FirebaseDatabase.getInstance().getReference("User")
        val user = mapOf<String, Int>(
            "usaVisited" to usaClickCount,
            "koreaVisited" to koreaClickCount,
            "indiaVisited" to indiaClickCount
        )

        database.child("Test").updateChildren(user)


        SpotifyAppRemote.connect(this, connectionParams, object : Connector.ConnectionListener {
            override fun onConnected(appRemote: SpotifyAppRemote) {
                spotifyAppRemote = appRemote
                Log.d("MainActivity", "Connected! Yay!")
                // Now you can start interacting with App Remote
                //connected()
            }

            override fun onFailure(throwable: Throwable) {
                Log.e("MainActivity", throwable.message, throwable)
                // Something went wrong when attempting to connect! Handle errors here
            }
        })
    }

    private fun connected(){
        spotifyAppRemote?.let {
            // Play a playlist
            val playlistURI = "spotify:playlist:37i9dQZF1DX5g856aiKiDS"
            it.playerApi.play(playlistURI)



//            // Subscribe to PlayerState
//            it.playerApi.subscribeToPlayerState().setEventCallback {
//                val track: Track = it.track
//                Log.d("MainActivity", track.name + " by " + track.artist.name)
//            }


        }



    }

    override fun onStop(){
        super.onStop()
        spotifyAppRemote?.let {
            SpotifyAppRemote.disconnect(it)
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