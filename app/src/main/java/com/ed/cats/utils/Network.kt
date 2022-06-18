package com.ed.cats.utils

import android.net.Uri
import org.json.JSONArray
import org.json.JSONException
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.ExecutionException


class Network {
    companion object {
        internal const val BASE = "https://api.thecatapi.com/v1"
        internal const val BREEDS = "breeds"
        internal const val IMAGES = "images"
        internal const val KEY = "aeecb068-4f7f-4851-91df-03a3c5b8367f"
    }
        private fun buildURL(): URL {
            val url = Uri.parse(BASE)
                .buildUpon()
                .appendPath(BREEDS)
                .build()
            return URL(url.toString())
        }
        private fun jsonLoad(url:URL):JSONArray?{
            var connection: HttpURLConnection? = null
                var result : JSONArray? = null
                try {
                    connection = url.openConnection() as HttpURLConnection
                    connection.setRequestProperty("x-api-key",KEY)
                    val inputStream : InputStream = connection.inputStream
                    val inputStreamReader = InputStreamReader(inputStream)
                    val reader = BufferedReader(inputStreamReader)
                    val builder : StringBuilder = StringBuilder()
                    var line : String? = reader.readLine()
                    while (line != null) {
                        builder.append(line)
                        line = reader.readLine()
                    }
                    result = JSONArray(builder.toString())
                } catch (e:IOException ) {
                    e.printStackTrace()
                } catch (e:JSONException ) {
                    e.printStackTrace()
                } finally {
                    connection?.disconnect()
                }
                return result
        }
            internal fun getJSONFromNetwork(): JSONArray? {

            var result:JSONArray? = null
            try {
                result = jsonLoad(buildURL())
            }
            catch (e: ExecutionException){
                e.printStackTrace()
            }
            catch (e:InterruptedException){
                e.printStackTrace()
            }
            return result
        }

    }


