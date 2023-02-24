package com.example.shapefile

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private var usaFips: GeoGsonCode? = null
    private var koreaCDs: GeoGsonCode? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        usaFips =
            GeoGsonCode(this.assets.open("GeoGsonUSA.json").bufferedReader().readText())
        Log.i(TAG, "USA Yakutat: ${usaFips?.getFipsCode(59.58303529504184, -139.02601606047415)}")
        Log.i(TAG, "USA USA Maui: ${usaFips?.getFipsCode(20.80238970466989, -156.2357097930059)}")
        Log.i(TAG, "USA San Mateo: ${usaFips?.getFipsCode(37.56882299708181, -122.4422072722358)}")
        Log.i(TAG, "USA Jefferson: ${usaFips?.getFipsCode(39.60014489481597, -105.21724005437149)}")
        Log.i(TAG, "USA Suffolk: ${usaFips?.getFipsCode(40.870295803273606, -72.76429866100507)}")

        koreaCDs =
            GeoGsonCode(this.assets.open("GeoGsonKorea.json").bufferedReader().readText())
        Log.i(TAG, "한국 중구: ${koreaCDs?.getFipsCode(37.4799269,126.4810742)}")
        Log.i(TAG, "한국 홍천군: ${koreaCDs?.getFipsCode(37.7634384,128.4689656)}")
        Log.i(TAG, "한국 영암군: ${koreaCDs?.getFipsCode(34.7644091,126.6051393)}")
        Log.i(TAG, "한국 영도구: ${koreaCDs?.getFipsCode(35.0791158,129.0620721)}")
        Log.i(TAG, "한국 제주시: ${koreaCDs?.getFipsCode(33.3722372,126.5330191)}")
    }

    companion object {
        const val TAG = "MainActivity"
    }
}