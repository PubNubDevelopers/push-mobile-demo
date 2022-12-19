package com.pubnub.pushdemo

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

/**
 * The sole purpose of this class is to interact with the demos hosted on pubnub.com/demos.
 * Do not include this code if you are copy / pasting from this sample
 */
class InteractiveDemo {
    private val LOG_TAG = "PNPushDemo"

    fun actionComplete(context: Context, identifier: String?, action: String)
    {
        if (null == identifier)
        {
            Log.d(LOG_TAG, "Identifier was null, not sending demo action")
            return
        }
        //  Keys for interactive demo only
        val pub = "pub-c-c8d024f7-d239-47c3-9a7b-002f346c1849"
        val sub = "sub-c-95fe09e0-64bb-4087-ab39-7b14659aab47"
        val jsonObj = JSONObject()
        jsonObj.put("id", identifier)
        jsonObj.put("feature", action)
        val encodedJson = java.net.URLEncoder.encode(jsonObj.toString(), "utf-8").replace("+", "%20")
        val queue = Volley.newRequestQueue(context)
        val url =
            "https://ps.pndsn.com/publish/$pub/$sub/0/demo/myCallback/$encodedJson?store=0&uuid=$identifier"

        val stringRequest = StringRequest(
            Request.Method.GET, url,
            Response.Listener<String> { response ->
                Log.d(LOG_TAG, "Guided Demo Integration Success: ${response}")
            },
            Response.ErrorListener { Log.d(LOG_TAG, "Guided Demo Integration Error") })
        queue.add(stringRequest)
    }
}