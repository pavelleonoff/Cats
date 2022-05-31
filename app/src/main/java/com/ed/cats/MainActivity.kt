package com.ed.cats

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ed.cats.data.Cats
import com.ed.cats.data.CatsAdapter
import com.ed.cats.data.MainViewModel
import com.ed.cats.utils.JSON
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class MainActivity : AppCompatActivity() {
    private lateinit var catsRecyclerView : RecyclerView
    private lateinit var catsAdapter: CatsAdapter
    private lateinit var viewModel : MainViewModel



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        runBlocking {
            launch { downLoadDataFromNetwork() }
        }


        catsRecyclerView = findViewById(R.id.catsRecyclerView)
        catsRecyclerView.layoutManager =  LinearLayoutManager(this, LinearLayoutManager.VERTICAL ,false)
        catsAdapter = CatsAdapter()

        getDataFromDb()?.let { it.observe(this) { t -> catsAdapter.setCats(t as ArrayList<Cats>) } }

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

    suspend fun downLoadDataFromNetwork(){
        if(!viewModel.isExist()) {
            val cats : List<Cats> = JSON.getCatsFromJSON()
            viewModel.insertCatsToDb(cats)
       }
    }
    fun getDataFromDb(): LiveData<List<Cats>>? {
        var cats = viewModel.getCatsFromDb()
        if (cats != null) {

            return cats
        }
        return null
    }
}
