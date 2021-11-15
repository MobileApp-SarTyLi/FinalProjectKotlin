package com.example.mobapps
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.mobapps.databinding.ActivityReadBinding
import com.example.mobapps.databinding.SpotifyPlayBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector
import com.spotify.android.appremote.api.SpotifyAppRemote
import com.spotify.protocol.types.Track

class ReadDataSpotifyKorea : AppCompatActivity() {

    private lateinit var binding : SpotifyPlayBinding
    private val clientId = "88c3bb0cc633461eb1fd330fa1232997"
    private val redirectUri = "sar-li-ty-login-test://callback"
    private var spotifyAppRemote: SpotifyAppRemote? = null
    var connectionParams = ConnectionParams.Builder(clientId)
        .setRedirectUri(redirectUri)
        .showAuthView(true)
        .build()

    //Firebase Database
    //private lateinit var database : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SpotifyPlayBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.pauseBtn.setOnClickListener {
            //Pause song
            spotifyAppRemote?.let {
                it.playerApi.pause()
            }
        }

        binding.skipBtn.setOnClickListener{
            spotifyAppRemote?.let {
                it.playerApi.skipNext()
            }
        }

        binding.resumeBtn.setOnClickListener{
            spotifyAppRemote?.let {
                it.playerApi.resume()
            }
        }

        binding.readdataBtn.setOnClickListener{
            var tracTitleName = ""
            var artistName = ""
            //Display song title
            spotifyAppRemote?.let {
                it.playerApi.subscribeToPlayerState().setEventCallback {
                    val track: Track = it.track
                    tracTitleName = track.name
                    artistName = track.artist.name

                }
            }
            //Display text on screen
            binding.spTitle.text = tracTitleName
            binding.spArtistName.text = artistName

        }
    }

    override fun onStart() {
        super.onStart()

        SpotifyAppRemote.connect(this, connectionParams, object : Connector.ConnectionListener {
            override fun onConnected(appRemote: SpotifyAppRemote) {
                spotifyAppRemote = appRemote
                Log.d("MainActivity", "Connected! Yay!")
                // Now you can start interacting with App Remote
                connected()
            }

            override fun onFailure(throwable: Throwable) {
                Log.e("MainActivity", throwable.message, throwable)
                // Something went wrong when attempting to connect! Handle errors here
            }
        })
    }

    private fun connected(){
        spotifyAppRemote?.let {
            // Play a playlist Korea Global
            val playlistURI = "spotify:playlist:37i9dQZEVXbJZGli0rRP3r"

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
}