package com.example.mobapps
import android.content.Intent
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
import java.net.URI
import android.R

import android.widget.TextView




class ReadDataSpotify : AppCompatActivity() {

    private lateinit var binding : SpotifyPlayBinding
    private var spotifyAppRemote: SpotifyAppRemote? = null
    var connectionParams = ConnectionParams.Builder(SpotifyUserCred.clientId)
        .setRedirectUri(SpotifyUserCred.redirectUri)
        .showAuthView(true)
        .build()
    var trackTitleName = ""
    var artistName = ""
    var trackNumber = 1
    var revealCount = 0
    var countryName = ""

    //Firebase Database
    //private lateinit var database : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SpotifyPlayBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // set country title
        countryName = intent.getStringExtra("countryName").toString()
//        var unicode = 0x1F1F8;
//        var emoji = getEmoji(unicode);
        binding.countryTitle.text = "Guess the #$trackNumber Top $countryName Song"

        // clear song and artist
        binding.spTitle.text = ""
        binding.spArtistName.text = ""

        spotifyAppRemote?.let {
            // repeat the same song until user presses a button
            it.playerApi.setRepeat(2)
            //it.playerApi.toggleRepeat()

            // get song title and artist name
            it.playerApi.subscribeToPlayerState().setEventCallback {
                val track: Track = it.track
                trackTitleName = track.name
                artistName = track.artist.name

                binding.spTitle.text = trackTitleName
                binding.spArtistName.text = artistName
            }
        }

        binding.pauseBtn.setOnClickListener {
            //Pause song
            spotifyAppRemote?.let {
                it.playerApi.pause()
            }
        }

        binding.skipBtn.setOnClickListener{
            trackNumber++
            binding.countryTitle.text = "Guess the #$trackNumber Top $countryName Song"
            spotifyAppRemote?.let {
                it.playerApi.skipNext()
                // repeat the same song until user presses a button
                it.playerApi.setRepeat(2)
                it.playerApi.toggleRepeat()
                it.playerApi.subscribeToPlayerState().setEventCallback {
                    val track: Track = it.track
                    trackTitleName = track.name
                    artistName = track.artist.name
                }
                // clear song
                binding.spTitle.text = ""
                binding.spArtistName.text = ""
            }
        }

        binding.playBtn.setOnClickListener{
            spotifyAppRemote?.let {
                it.playerApi.resume()
            }
        }

        binding.readdataBtn.setOnClickListener{

            //Display song title
            spotifyAppRemote?.let {
                it.playerApi.subscribeToPlayerState().setEventCallback {
                    trackTitleName = ""
                    artistName = ""
                    val track: Track = it.track
                    trackTitleName = track.name
                    artistName = track.artist.name
                    if(revealCount == 0) {
                        binding.spTitle.text = trackTitleName
                        binding.spArtistName.text = artistName
                        revealCount++
                    }
//                    //Display text on screen
//                    binding.spTitle.text = trackTitleName
//                    binding.spArtistName.text = artistName

                }
                if(revealCount > 0) {
                    //Display text on screen
                    binding.spTitle.text = trackTitleName
                    binding.spArtistName.text = artistName
                    revealCount++
                }

            }
//            //Display text on screen
//            binding.spTitle.text = trackTitleName
//            binding.spArtistName.text = artistName

        }
    }

    override fun onStart() {
        super.onStart()

        SpotifyAppRemote.connect(this, connectionParams, object : Connector.ConnectionListener {
            override fun onConnected(appRemote: SpotifyAppRemote) {
                spotifyAppRemote = appRemote
                Log.d("MainActivity", "Connected! Yay!")
                // Now you can start interacting with App Remote
                val playlistURI = intent.getStringExtra("playlistURI").toString()
                connected(playlistURI)
            }

            override fun onFailure(throwable: Throwable) {
                Log.e("MainActivity", throwable.message, throwable)
                // Something went wrong when attempting to connect! Handle errors here
            }
        })
    }

    private fun connected(playlistURI : String){
        spotifyAppRemote?.let {
            // Play a playlist USA Global
//            val playlistURI = uri

            it.playerApi.play(playlistURI)
            // repeat the same song until user presses a button
            it.playerApi.setRepeat(2)
            //it.playerApi.toggleRepeat()

//            // Subscribe to PlayerState
//            it.playerApi.subscribeToPlayerState().setEventCallback {
//                val track: Track = it.track
//                Log.d("MainActivity", track.name + " by " + track.artist.name)
//            }


        }



    }


//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//        val textView = findViewById<TextView>(R.id.tv)
//        val text = "Hey$emoji"
//        textView.text = text
//    }

    private fun getEmoji(uni: Int): String? {
        return String(Character.toChars(uni))
    }

//    public String getEmoji(int unicode) {
//        return new String(Character.toChars(unicode));
//    }

    override fun onPause() {
        super.onPause()
        trackTitleName = ""
        artistName = ""
        binding.spTitle.text = ""
        binding.spArtistName.text = ""
    }

    override fun onStop(){
        super.onStop()
        trackTitleName = ""
        artistName = ""
        binding.spTitle.text = ""
        binding.spArtistName.text = ""
        spotifyAppRemote?.let {
            SpotifyAppRemote.disconnect(it)
        }
    }
}