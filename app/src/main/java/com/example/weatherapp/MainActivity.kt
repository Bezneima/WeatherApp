package com.example.weatherapp

import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.weatherapp.Interfaces.WeatherService
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.util.concurrent.Callable


class MainActivity : AppCompatActivity() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var latitude = ""
    private var longitude = ""
    private var userUrl = ""

    val weatherApiServe by lazy {
        WeatherService.create()
    }
    val parserYandex by lazy {
        WeatherService.create()
    }
    var disposable: Disposable? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
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

                        getCityName(userUrl)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({
                                textView.text = it
                            },{
                                Log.e("SHITHAPPEN", it.message)
                            })
                    },
                    { error -> (error.message) }
                )
    }

    private fun getCityName(cityUrl: String): Observable<String> {
        return Observable.fromCallable {
            val doc = Jsoup.connect(cityUrl).get()
            return@fromCallable doc.select("h1").text()
        }
    }
}

