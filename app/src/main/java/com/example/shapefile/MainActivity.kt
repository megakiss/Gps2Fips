package com.example.shapefile

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private var usaFips: UsaFips? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        usaFips =
            UsaFips(this.assets.open("cb_2018_us_county_20m.json").bufferedReader().readText())
        Log.i(TAG, "Yakutat: ${usaFips?.getFipsCode(59.58303529504184, -139.02601606047415)}")
        Log.i(TAG, "Maui: ${usaFips?.getFipsCode(20.80238970466989, -156.2357097930059)}")
        Log.i(TAG, "San Mateo: ${usaFips?.getFipsCode(37.56882299708181, -122.4422072722358)}")
        Log.i(TAG, "Jefferson: ${usaFips?.getFipsCode(39.60014489481597, -105.21724005437149)}")
        Log.i(TAG, "Suffolk: ${usaFips?.getFipsCode(40.870295803273606, -72.76429866100507)}")
    }

    companion object {
        const val TAG = "MainActivity"
    }
}