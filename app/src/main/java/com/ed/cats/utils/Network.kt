package com.ed.cats.utils

import android.net.Uri
import android.os.AsyncTask
import android.os.AsyncTask.execute
import android.util.Log
import androidx.loader.content.AsyncTaskLoader
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.ExecutionException

class Network {
    companion object {
        val API_KEY = "54d58c14-8dd5-4adc-a2a4-d1c5ba8af8e4"
        val BREEDS = "https://api.thecatapi.com/v1/breeds"
        //val BREEDS = "?limit=1"

        private fun buildURL(): URL {
            val url = Uri.parse(BREEDS)
                .buildUpon()
                .build()
            return URL(url.toString())
        }

        open class JSONLoadTask : AsyncTask<URL, Void, JSONArray>() {
            override fun doInBackground(vararg p0: URL?): JSONArray? {
                var connection: HttpURLConnection? = null
                var result : JSONArray? = null
                try {
                    connection = p0[0]?.openConnection() as HttpURLConnection
                    val inputStream : InputStream = connection.getInputStream();
                    val inputStreamReader = InputStreamReader(inputStream);
                    val reader = BufferedReader(inputStreamReader);
                    var builder : StringBuilder = StringBuilder();
                    var line : String? = reader.readLine();
                    while (line != null) {
                        builder.append(line);
                        line = reader.readLine();
                    }
                    result = JSONArray(builder.toString())
                } catch (e:IOException ) {
                    e.printStackTrace();
                } catch (e:JSONException ) {
                    e.printStackTrace();
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
                return result;
            }
        }
        fun getJSONFromNetwork(): JSONArray? {

            var result:JSONArray? = null
            try {
                result = JSONLoadTask().execute(buildURL()).get()
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