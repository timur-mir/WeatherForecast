package ru.presentationpatterns.weatherforecast.data.networkrepo

import android.content.Context
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import kotlinx.coroutines.suspendCancellableCoroutine
import org.json.JSONObject
import kotlin.coroutines.resumeWithException

class NetworkApi constructor(context: Context) {
    companion object {
       // @Volatile
        private var INSTANCE: NetworkApi? = null
        fun getInstance(context: Context) =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: NetworkApi(context).also {
                    INSTANCE = it
                }
            }
        fun getInstance(): NetworkApi? =
            INSTANCE ?: synchronized(this) {
              if(null== INSTANCE){
                  throw IllegalStateException(
                      NetworkApi::class.java.getSimpleName() +
                              " is not initialized, call getInstance(...) first"
                  )
              }
                return INSTANCE
            }

    }
      private  val mRequestQueue: RequestQueue by lazy {
            Volley.newRequestQueue(context)
        }


        suspend fun APIrequest(url: String): JSONObject? {

            return suspendCancellableCoroutine { continuation ->
                try {
                    // Sucess Listner
                    val success = Response.Listener<JSONObject> { response ->
                        if (continuation.isActive) {

                            continuation.resume(response,
                                onCancellation = {
                                     it.cause?.printStackTrace()
                                })
                        }
                    }

                    // Error Listner
                    val error = Response.ErrorListener { error ->
                        if (continuation.isActive) {
                            continuation.resume(null, onCancellation = {
                                 it.printStackTrace()
                            })
                        }
                    }

                    val jsonObjectRequest:JsonObjectRequest =
                        JsonObjectRequest(Request.Method.GET, url, null, success, error)

                    mRequestQueue.add(jsonObjectRequest)

                } catch (e: Exception) {
                    e.printStackTrace()
                    if (continuation.isActive) {
                        if (continuation.isActive) {
                            continuation.resumeWithException(e)

                        }
                    }
                }
            }
        }
    }

