package com.troido.bless.app.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import com.troido.bless.app.bonding.BondingActivity
import com.troido.bless.app.connect_service.ConnectServiceActivity
import com.troido.bless.app.deserialization.DeserializationActivity
import com.troido.bless.app.main.views.MainViewMvp
import com.troido.bless.app.main.views.MainViewMvpImpl
import com.troido.bless.app.scan.ScanStarterActivity
import com.troido.bless.app.scan_service.ScanServiceActivity

class MainActivity : AppCompatActivity(), MainViewMvp.Listener {

    private lateinit var viewMvp: MainViewMvp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewMvp = MainViewMvpImpl(LayoutInflater.from(this), null)
        viewMvp.registerListener(this)
        setContentView(viewMvp.rootView)
    }

    override fun onDestroy() {
        super.onDestroy()
        viewMvp.unregisterListener(this)
    }

    override fun onScanFeatureClicked() {
        val intent = Intent(this, ScanStarterActivity::class.java)
        startActivity(intent)
    }

    override fun onDeserializationFeatureClicked() {
        val intent = Intent(this, DeserializationActivity::class.java)
        startActivity(intent)
    }

    override fun onScanServiceFeatureClicked() {
        val intent = Intent(this, ScanServiceActivity::class.java)
        startActivity(intent)
    }

    override fun onBondingFeatureClicked() {
        BondingActivity.startActivity(this)
    }

    override fun onConnectServiceFeatureClicked() {
        val intent = Intent(this, ConnectServiceActivity::class.java)
        startActivity(intent)
    }
}