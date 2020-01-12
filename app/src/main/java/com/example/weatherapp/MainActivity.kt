package com.example.weatherapp

import android.R.interpolator.linear
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.location.Location
import android.media.Image
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.weatherapp.Interfaces.WeatherService
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import org.jsoup.Jsoup
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var latitude = ""
    private var longitude = ""
    private var userUrl = ""
    private var temperature = ""
    private var cityName = ""

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
                        textView2.text =
                            it.now_dt.substring(0, 10) + " " + it.now_dt.substring(12, 16)
                        Vtemperature.text = it.fact.temp.toString() + "℃"
                        userUrl = it.info.url
                        it.forecasts[0].hours.forEach {
                            addHour(it.hour.toString(), it.temp.toString(),it.condition)
                        }

                        getCityName(userUrl)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({
                                textView.text = it
                            }, {
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

    fun getTimeStamp(timeinMillies: Long): String {
        var date: String = ""
        val formatter = SimpleDateFormat("HH:mm yyyy.MM.dd")
        date = formatter.format(Date(timeinMillies))
        return date
    }

    fun addHour(hour: String, temperature: String,icon:String) {
        var linearVToAdd = findViewById<LinearLayout>(R.id.VHours)
        val newLinearLayout = LinearLayout(this)
        newLinearLayout.orientation = LinearLayout.VERTICAL

        val paramsLinear = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        val paramsTextView = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        val paramsImageView = LinearLayout.LayoutParams(
            180, 180
        )
        newLinearLayout.layoutParams = paramsLinear

        var newImageView = ImageView(this)
        newImageView.layoutParams = paramsImageView
        newImageView.setImageResource(loadWeatherPicture(icon))

        val hourTextView = TextView(this)
        hourTextView.text = hour
        hourTextView.setTextColor(Color.WHITE)
        hourTextView.gravity = Gravity.CENTER
        hourTextView.layoutParams = paramsTextView

        val temperatureTextView = TextView(this)
        temperatureTextView.text = temperature + "℃"
        temperatureTextView.setTextColor(Color.WHITE)
        temperatureTextView.gravity = Gravity.CENTER
        temperatureTextView.layoutParams = paramsTextView

        newLinearLayout.addView(hourTextView)
        newLinearLayout.addView(newImageView)
        newLinearLayout.addView(temperatureTextView)
        linearVToAdd.addView(newLinearLayout)


    }

    fun loadWeatherPicture(weatherType: String): Int {
        when (weatherType) {
            "clear" -> {
                return R.drawable.clear
            }
            "partly-cloudy" -> {
                return R.drawable.partlycloudy
            }
            "cloudy" -> {
                return R.drawable.partlycloudy
            }
            "overcast" -> {
                return R.drawable.overcast
            }
            "partly-cloudy-and-light-rain" -> {
                return R.drawable.partlycloudyandlightrain
            }
            "partly-cloudy-and-rain" -> {
                return R.drawable.partlycloudyandrain
            }
            "overcast-and-rain" -> {
                return R.drawable.overcastandrain
            }
            "overcast-thunderstorms-with-rain" -> {
                return R.drawable.overcastthunderstormswithrain
            }
            "cloudy-and-light-rain" -> {
                return R.drawable.partlycloudyandrain
            }
            "overcast-and-light-rain" -> {
                return R.drawable.overcastandrain
            }
            "cloudy-and-rain" -> {
                return R.drawable.partlycloudyandrain
            }
            "overcast-and-wet-snow" -> {
                return R.drawable.overcastandwetsnow
            }
            "partly-cloudy-and-light-snow" -> {
                return R.drawable.partlycloudyandlightsnow
            }
            "partly-cloudy-and-snow" -> {
                return R.drawable.partlycloudyandsnow
            }
            "overcast-and-snow" -> {
                return R.drawable.overcastandwetsnow
            }
            "cloudy-and-light-snow" -> {
                return R.drawable.partlycloudyandlightsnow
            }
            "overcast-and-light-snow" -> {
                return R.drawable.overcastandwetsnow
            }
            "cloudy-and-snow" -> {
                return R.drawable.partlycloudyandsnow
            }
            else -> {
                return R.drawable.clear
            }
        }
    }
}

