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
        private const val BREEDS = "https://api.thecatapi.com/v1/breeds"

        private fun buildURL(): URL {
            val url = Uri.parse(BREEDS)
                .buildUpon()
                .build()
            return URL(url.toString())
        }
        private fun jsonLoad(url:URL):JSONArray?{
            var connection: HttpURLConnection? = null
                var result : JSONArray? = null
                try {
                    connection = url.openConnection() as HttpURLConnection
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
            fun getJSONFromNetwork(): JSONArray? {

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


}