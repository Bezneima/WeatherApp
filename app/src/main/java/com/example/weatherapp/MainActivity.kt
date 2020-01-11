package com.example.weatherapp

import android.location.Location
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.weatherapp.Retrofit.WeatherService
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var latitude = ""
    private var longitude = ""
    private var userUrl = ""

    val weatherApiServe by lazy {
        WeatherService.create()
    }
    var disposable: Disposable? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                //                textView2.text =
                //                "Lat " + location!!.latitude.toString() + " Long " + location!!.longitude.toString()
                //                latitude = location!!.latitude.toString()
                //                longitude = location!!.longitude.toString()
                getWeather(location!!.latitude.toString(), location!!.longitude.toString())
            }
            .addOnFailureListener { textView2.text = "Error" }


    }

    private fun getWeather(lat: String, lon: String) {
        disposable =
            weatherApiServe.hitCountCheck(lat, lon)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        textView2.text = it.fact.temp.toString()
                        userUrl = it.info.url
                    },
                    { error -> (error.message) }
                )
    }

    private fun getUserCity() {

    }


}

