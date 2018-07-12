package com.philips.lighting.quickstart

import java.util.Random


import android.app.Activity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

import com.philips.lighting.hue.listener.PHLightListener
import com.philips.lighting.hue.sdk.PHHueSDK
import com.philips.lighting.model.PHBridge
import com.philips.lighting.model.PHBridgeResource
import com.philips.lighting.model.PHHueError
import com.philips.lighting.model.PHLight
import com.philips.lighting.model.PHLightState
import java.io.File



/**
 * MyApplicationActivity - The starting point for creating your own Hue App.
 * Currently contains a simple view with a button to change your lights to random colours.  Remove this and add your own app implementation here! Have fun!
 *
 * @author SteveyO
 */
class MyApplicationActivity : Activity() {
    private var phHueSDK: PHHueSDK? = null
    var codeBook: HashMap<String, String> = HashMap()

    // If you want to handle the response from the bridge, create a PHLightListener object.
    internal var listener: PHLightListener = object : PHLightListener {

        override fun onSuccess() {}

        override fun onStateUpdate(arg0: Map<String, String>, arg1: List<PHHueError>) {
            Log.w(TAG, "Light has updated")
        }

        override fun onError(arg0: Int, arg1: String) {}

        override fun onReceivingLightDetails(arg0: PHLight) {}

        override fun onReceivingLights(arg0: List<PHBridgeResource>) {}

        override fun onSearchComplete() {}
    }

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitle(R.string.app_name)
        setContentView(R.layout.activity_main)
        phHueSDK = PHHueSDK.create()
        val goButton: Button
        goButton = findViewById(R.id.buttonRand) as Button
        codeBook = readFile()
        Log.d("MYTAG", "GOT HERE")
        goButton.setOnClickListener { displayMsg(convertToMorse()) }


    }


    fun randomLights() {
        val bridge = phHueSDK!!.selectedBridge

        val allLights = bridge.resourceCache.allLights
        val rand = Random()

        for (light in allLights) {
            val lightState = PHLightState()
            lightState.hue = rand.nextInt(MAX_HUE)
            // To validate your lightstate is valid (before sending to the bridge) you can use:
            // String validState = lightState.validateState();
            bridge.updateLightState(light, lightState, listener)
            //  bridge.updateLightState(light, lightState);   // If no bridge response is required then use this simpler form.
        }
    }
    fun convertToMorse(): ArrayList<String> {
        val edit = findViewById(R.id.editText) as EditText
        val msg = edit.text.toString().toUpperCase()
        var list: ArrayList<String> = ArrayList()
        for(let in msg){

            list.add(codeBook.get(""+let)+"")
        }





        return list
    }

    fun displayMsg(ls:ArrayList<String>) {
        val bridge = phHueSDK!!.selectedBridge

        val allLights = bridge.resourceCache.allLights
        val lightState = PHLightState()
        val light = allLights[3]

        for(i in ls){
            for(num in i){

                if(num == '1'){
                    lightState.hue = 18000
                    lightState.brightness = 250


                    bridge.updateLightState(light, lightState, listener)
                    Thread.sleep(1_500)


                }else if(num == '0'){
                    lightState.hue = 5000
                    lightState.brightness = 250
                    bridge.updateLightState(light, lightState, listener)
                    Thread.sleep(500)

                }

                lightState.brightness = 15

                bridge.updateLightState(light, lightState, listener)
                Thread.sleep(2_000)
            }
            println("**********************"+i)

        }



        for (light in allLights) {

            // To validate your lightstate is valid (before sending to the bridge) you can use:
            // String validState = lightState.validateState();

            //  bridge.updateLightState(light, lightState);   // If no bridge response is required then use this simpler form.
        }

    }
    fun readFile(): HashMap<String, String>{

        var map: HashMap<String, String> = HashMap()

        map.put ( "A", "01" )
        map.put("B", "1000")
        map.put("C", "1010")
        map.put("D", "100")
        map.put("E", "0")
        map.put("F", "0010")
        map.put("G", "110")
        map.put("H", "0000")
        map.put("I", "00")
        map.put("J", "0111")
        map.put("K", "101")
        map.put("L", "0100")
        map.put("M", "11")
        map.put("N", "10")
        map.put("O", "111")
        map.put("P", "0110")
        map.put("Q", "1101")
        map.put("R", "010")
        map.put("S", "000")
        map.put("T", "1")
        map.put("U", "001")
        map.put("V", "0001")
        map.put("W", "011")
        map.put("X", "1001")
        map.put("Y", "1011")
        map.put("Z", "1100")







        return map
    }

    override fun onDestroy() {
        val bridge = phHueSDK!!.selectedBridge
        if (bridge != null) {

            if (phHueSDK!!.isHeartbeatEnabled(bridge)) {
                phHueSDK!!.disableHeartbeat(bridge)
            }

            phHueSDK!!.disconnect(bridge)
            super.onDestroy()
        }
    }

    companion object {
        private val MAX_HUE = 65535
        val TAG = "QuickStart"
    }
}






