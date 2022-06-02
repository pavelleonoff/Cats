package com.ed.cats

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ed.cats.data.Cat
import com.ed.cats.data.CatsAdapter
import com.ed.cats.data.MainViewModel
import com.ed.cats.utils.JSON
import com.ed.cats.utils.Network
import kotlinx.coroutines.*
import org.json.JSONArray


class MainActivity : AppCompatActivity() {
    private lateinit var catsRecyclerView : RecyclerView
    private lateinit var catsAdapter: CatsAdapter
    private lateinit var viewModel : MainViewModel

    companion object{
        var screenWidth = 0
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        screenWidth = resources.displayMetrics.widthPixels

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        val scope = CoroutineScope(Dispatchers.IO)

        scope.launch {
            downLoadDataFromNetwork()
        }

        catsRecyclerView = findViewById(R.id.catsRecyclerView)
        catsRecyclerView.layoutManager =  LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL ,
            false)
        catsAdapter = CatsAdapter()

        getDataFromDb()?.let { it.observe(this) {
                t -> catsAdapter.setCats(t as ArrayList<Cat>) } }

        catsRecyclerView.adapter = catsAdapter

        catsAdapter.setOnCatClickListener(object : CatsAdapter.OnCatClickListener {
            override fun onCatClick(id: Int) {
                val catId:String = catsAdapter.getCats()[id].id
                val intent = Intent(this@MainActivity,DetailActivity::class.java)
                intent.putExtra("id",catId)
                startActivity(intent)
            }

        })


    }


    private fun downLoadDataFromNetwork(){
            if (!viewModel.isExist()) {
                val catsJSON: JSONArray? = Network.getJSONFromNetwork()
                val cats: List<Cat> = JSON.getCatsFromJSON(catsJSON)
                viewModel.insertCatsToDb(cats)
            }

    }

    private fun getDataFromDb(): LiveData<List<Cat>>? {
        val cats = viewModel.getCatsFromDb()
        if (cats != null) {

            return cats
        }
        return null
    }
}
