package com.example.mobapps

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
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

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getMainExecutor
import java.lang.StringBuilder


class MainActivity : AppCompatActivity(), LocationListener {
    //Old Binding with Activity Main
    //private lateinit var binding: ActivityMainBinding
    private lateinit var binding: CountrySelectBinding
    // Firebase Database
    private lateinit var database: DatabaseReference
    // Keeping track of # of times USA clicked
    private var usaClickCount = 0
    private var koreaClickCount = 0
    private var indiaClickCount = 0
    private var mexicoClickCount = 0
    private var germanyClickCount = 0
    private var indonesiaClickCount = 0
    private var egyptClickCount = 0
    private var russiaClickCount = 0
    //testing fragment backstack
    var showData = false

    //dealing with GPS
    private lateinit var locationManager: LocationManager
    private lateinit var tvGpsLocation: TextView
    private val locationPermissionCode = 2

    private var spotifyAppRemote: SpotifyAppRemote? = null
    var connectionParams = ConnectionParams.Builder(SpotifyUserCred.clientId)
        .setRedirectUri(SpotifyUserCred.redirectUri)
        .showAuthView(true)
        .build()

    fun usaFn() {
        usaClickCount++
        val intent = Intent(this, ReadDataSpotify::class.java)
        intent.putExtra("playlistURI","spotify:playlist:37i9dQZEVXbLp5XoPON0wI")
        intent.putExtra("countryName", "USA")
        startActivity(intent)
        Toast.makeText(this, "You Picked USA!", Toast.LENGTH_SHORT).show()
    }
    fun koreaFn() {
        koreaClickCount++
        val intent = Intent(this, ReadDataSpotify::class.java)
        intent.putExtra("playlistURI","spotify:playlist:37i9dQZEVXbJZGli0rRP3r")
        intent.putExtra("countryName", "South Korea")
        startActivity(intent)
        Toast.makeText(this, "You Picked Korea!", Toast.LENGTH_SHORT).show()
    }
    fun indiaFn() {
        indiaClickCount++
        val intent = Intent(this, ReadDataSpotify::class.java)
        intent.putExtra("playlistURI","spotify:playlist:37i9dQZEVXbMWDif5SCBJq")
        intent.putExtra("countryName", "India")
        startActivity(intent)
        Toast.makeText(this, "You Picked India!", Toast.LENGTH_SHORT).show()
    }
    fun mexicoFn() {
        mexicoClickCount++
        val intent = Intent(this, ReadDataSpotify::class.java)
        intent.putExtra("playlistURI","spotify:playlist:37i9dQZEVXbKUoIkUXteF6")
        intent.putExtra("countryName", "Mexico")
        startActivity(intent)
        Toast.makeText(this, "You Picked Mexico!", Toast.LENGTH_SHORT).show()
    }
    fun germanyFn() {
        germanyClickCount++
        val intent = Intent(this, ReadDataSpotify::class.java)
        intent.putExtra("playlistURI","spotify:playlist:37i9dQZEVXbK8BKKMArIyl")
        intent.putExtra("countryName", "Germany")
        startActivity(intent)
        Toast.makeText(this, "You Picked Germany!", Toast.LENGTH_SHORT).show()
    }
    fun indonesiaFn() {
        indonesiaClickCount++
        val intent = Intent(this, ReadDataSpotify::class.java)
        intent.putExtra("playlistURI","spotify:playlist:37i9dQZEVXbIZK8aUquyx8")
        intent.putExtra("countryName", "Indonesia")
        startActivity(intent)
        Toast.makeText(this, "You Picked Indonesia!", Toast.LENGTH_SHORT).show()
    }
    fun egyptFn() {
        egyptClickCount++
        val intent = Intent(this, ReadDataSpotify::class.java)
        intent.putExtra("playlistURI","spotify:playlist:37i9dQZEVXbMy2EcFg5F9m")
        intent.putExtra("countryName", "Egypt")
        startActivity(intent)
        Toast.makeText(this, "You Picked Egypt!", Toast.LENGTH_SHORT).show()
    }
    fun russiaFn() {
        russiaClickCount++
        val intent = Intent(this, ReadDataSpotify::class.java)
        intent.putExtra("playlistURI","spotify:playlist:37i9dQZEVXbNALwC1jxb5m")
        intent.putExtra("countryName", "Russia")
        startActivity(intent)
        Toast.makeText(this, "You Picked Russia!", Toast.LENGTH_SHORT).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(null, "onCreate() for Second Fragment")
        binding = CountrySelectBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //TODO: Change this to an individual fragment
        val buttonGPS = findViewById<Button>(R.id.getCountryBtn)
        buttonGPS.setOnClickListener {
            getLocation()
            Log.d("LiGPS", "are we clicked?")
            Toast.makeText(this, "You Picked Your Location! Calculating...", Toast.LENGTH_LONG).show()
        }

        //Login/Register Button
        val button = findViewById<Button>(R.id.USABtn)
        button.setOnClickListener {
            usaFn()
        }

        val button2 = findViewById<Button>(R.id.KoreaBtn)
        button2.setOnClickListener{
            koreaFn()
        }

        val button3 = findViewById<Button>(R.id.IndiaBtn)
        button3.setOnClickListener{
            indiaFn()
        }

        val button4 = findViewById<Button>(R.id.MexicoBtn)
        button4.setOnClickListener{
            mexicoFn()
        }

        val button5 = findViewById<Button>(R.id.GermanyBtn)
        button5.setOnClickListener{
            germanyFn()
        }

        val button6 = findViewById<Button>(R.id.IndonesiaBtn)
        button6.setOnClickListener{
            indonesiaFn()
        }

        val button7 = findViewById<Button>(R.id.EgyptBtn)
        button7.setOnClickListener{
            egyptFn()
        }

        val button8 = findViewById<Button>(R.id.RussiaBtn)
        button8.setOnClickListener{
            russiaFn()
        }

        val buttonData = findViewById<Button>(R.id.DataBtn)
        buttonData.setOnClickListener {
//            var mFragment: Fragment? = null
//            mFragment = DataFragment()
            val fragmentManager: FragmentManager = supportFragmentManager
//
//            fragmentManager.beginTransaction()
//                .replace(R.id.frameLayout, mFragment).commit()
            val frag = DataFragment()
            val transaction = fragmentManager.beginTransaction()


            if (showData) {
                binding.DataBtn.text = "Show User Data"
                transaction.remove(frag)
                transaction.commit()
                supportFragmentManager.popBackStack()
                Log.d("DataFragment", "Removed Data Fragment! Nooo!")
                showData = false
            } else {
                binding.DataBtn.text = "Hide User Data"
                transaction.replace(R.id.frag_container, frag)
                transaction.addToBackStack("added")
                transaction.commit()
                Log.d("DataFragment", "Data Fragment! Yay!")
                showData = true
            }
            val x = supportFragmentManager.backStackEntryCount
            Log.d("Backstack count", x.toString())
//            if(supportFragmentManager.backStackEntryCount > 0) {
//                val x = supportFragmentManager.getBackStackEntryAt(0)
//                val str = x.toString()
//                Log.d("DataFragment", str)
//            }

        }

    }

    //assisting with GPS
    private fun getLocation() {
        Log.d("LiGPS", "We are getting location...")
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), locationPermissionCode)
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5f, this)
        Log.d("LiGPS", "Requested location updates.")
        //locationManager.getCurrentLocation(LocationManager.GPS_PROVIDER, null, this.mainExecutor, )
        //locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, this)
        val loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)

        ///TODO MAY BE A DUPLICATE HERE
        if (loc != null) {
            Log.d("LiGPS", "Obtained last known location.")
            val str = closestCountryCalculator(loc.latitude,loc.longitude)
            //tvGpsLocation.text = "Closest: " + str
            if (str == "USA") {
                usaFn()
            } else if (str == "Korea") {
                koreaFn()
            } else if (str == "Germany") {
                germanyFn()
            } else if (str == "India") {
                indiaFn()
            } else if (str == "Mexico") {
                mexicoFn()
            } else if (str == "Indonesia") {
                indonesiaFn()
            } else if (str == "Egypt") {
                egyptFn()
            } else if (str == "Russia") {
                russiaFn()
            } else {
                Toast.makeText(this, "Oops! There was an error.", Toast.LENGTH_SHORT)
            }
            locationManager.removeUpdates(this)
        } else {
            Log.d("LiGPS", "The last known loc was null.")
        }



    }
    override fun onLocationChanged(location: Location) {
        //this will change title which we dont want butttt just for testing...
        //tvGpsLocation = findViewById(R.id.textView2)
        Log.d("LiGPS", "Did we come in here?")
//        val locationAddress = LocationAddress()
//        locationAddress.getAddress...

        //tvGpsLocation.text = "Latitude: " + location.latitude + " , Longitude: " + location.longitude
        //val arr1 = FloatArray(1)
        //Location.distanceBetween(location.latitude, location.longitude, 0.0, 0.0, arr1)
        val str = closestCountryCalculator(location.latitude,location.longitude)
        //tvGpsLocation.text = "Closest: " + str
        if (str == "USA") {
            usaFn()
        } else if (str == "Korea") {
            koreaFn()
        } else if (str == "Germany") {
            germanyFn()
        } else if (str == "India") {
            indiaFn()
        } else if (str == "Mexico") {
            mexicoFn()
        } else if (str == "Indonesia") {
            indonesiaFn()
        } else if (str == "Egypt") {
            egyptFn()
        } else if (str == "Russia") {
            russiaFn()
        } else {
            Toast.makeText(this, "Oops! There was an error.", Toast.LENGTH_SHORT)
        }
        locationManager.removeUpdates(this)

    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        //this next line was auto added ---
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == locationPermissionCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
            }
            else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }
   //val coords = mapOf("Germany" to arrayOf(51.1657, 10.4515), "USA" to arrayOf(37.0902, 95.7129))
    //self written in order to get the closest country
    fun closestCountryCalculator(lat: Double, lon: Double): String {
       Log.d("LiGPS", "Calculating closest country..")
       val coords = mapOf("Germany" to arrayOf(51.1657, 10.4515), "USA" to arrayOf(37.0902, -95.7129), "Korea" to arrayOf(35.9078, 127.7669), "India" to arrayOf(20.5937, 78.9629), "Mexico" to arrayOf(23.6345, -102.5528), "Indonesia" to arrayOf(-0.7893, 113.9213), "Egypt" to arrayOf(26.8206, 30.8025), "Russia" to arrayOf(61.5240, 105.3188))
       var min = 99999999999.99.toFloat()
       var closestLoc = "Nowhere"
       var arr = FloatArray(1)
       coords.forEach {
           Location.distanceBetween(lat, lon, it.value[0], it.value[1], arr)
           if (arr[0] < min) {
               min = arr[0]
               closestLoc = it.key
           }
       }
       return closestLoc
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
            "indiaVisited" to indiaClickCount,
            "mexicoVisited" to mexicoClickCount,
            "germanyVisited" to germanyClickCount,
            "indonesiaVisited" to indonesiaClickCount,
            "egyptVisited" to egyptClickCount,
            "russiaVisited" to russiaClickCount,
        )

        database.child("Test").updateChildren(user)

        database.child("Test").get().addOnSuccessListener {
            val usa = it.child("usaVisited").value
            Log.d("HELLO", "usa Visited : $usa")
        }



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