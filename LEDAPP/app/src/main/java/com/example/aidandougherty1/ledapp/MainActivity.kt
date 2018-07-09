package com.example.aidandougherty1.ledapp

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Button
import java.io.IOException
import java.util.*

class MainActivity : AppCompatActivity() {

    val handler = Handler()
    var mmSocket: BluetoothSocket? = null
    var mmDevice: BluetoothDevice? = null
    val mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (!mBluetoothAdapter.isEnabled) {
            val enableBluetooth = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableBluetooth, 0)
        }
        val pairedDevices = mBluetoothAdapter.bondedDevices
        if (pairedDevices.size > 0) {
            for (device in pairedDevices) {
                if (device.name == "Group10pi") { //Note, you will need to change this to match the name of your device{
                    mmDevice = device
                    break
                }
            }
        }
        val offButton = findViewById<View>(R.id.offButton) as Button
        val onButton = findViewById<View>(R.id.onButton) as Button

        onButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                // Perform action on temp button click
                Thread(WorkerThread("ON")).start()
                offButton.setEnabled(true)
                onButton.setEnabled(false);

            }
        })

        offButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                // Perform action on temp button click
                Thread(WorkerThread("OFF")).start()
                onButton.setEnabled(true)
                offButton.setEnabled(false)
            }
        })


    }
    fun sendBtMsg(msg: String) {
        val uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34ab") //Standard SerialPortService ID
        try {
            mmSocket = mmDevice?.createRfcommSocketToServiceRecord(uuid)
            if (!mmSocket!!.isConnected()) {
                mmSocket!!.connect()
            }
            val mmOutputStream = mmSocket!!.getOutputStream()
            mmOutputStream.write(msg.toByteArray())

        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
    internal inner class WorkerThread(private val btMsg: String) : Runnable {
        override fun run() {
            sendBtMsg(btMsg)
            Thread.sleep(1000)
            try{
                mmSocket!!.close()
            }catch(e: IOException){
                e.printStackTrace()
            }


        }
    }
}
