package com.ed.cats

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ed.cats.data.Cat
import com.ed.cats.adapters.CatsAdapter
import com.ed.cats.data.DBQueries
import com.ed.cats.data.MainViewModel
import kotlinx.coroutines.*



class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var catsRecyclerView : RecyclerView
    private lateinit var catsAdapter: CatsAdapter
    private lateinit var viewModel : MainViewModel
    private lateinit var pref : SharedPreferences
    private lateinit var filtersButton : TextView
    private lateinit var filterStatus : TextView
    private lateinit var filtersButtonClear : ImageView
    companion object{
        var screenWidth = 0
        var screenHeight = 0
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        pref = getSharedPreferences("Filter",MODE_PRIVATE)
        pref.edit().putString(DBQueries.rawQuery, "").apply()

        screenWidth = resources.displayMetrics.widthPixels
        screenHeight = resources.displayMetrics.heightPixels
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        filtersButton = findViewById(R.id.filtersButton)
        filterStatus = findViewById(R.id.filterStatus)
        filtersButtonClear = findViewById(R.id.filtersButtonClear)
        val dbQuery = DBQueries()
        if (!viewModel.isExist()) {
            val scope = CoroutineScope(Dispatchers.IO)
            scope.launch {
                dbQuery.downloadCatsFromNetwork(viewModel)
            }
        }
        catsRecyclerView = findViewById(R.id.catsRecyclerView)
        catsRecyclerView.layoutManager =  LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL ,
            false)
        catsAdapter = CatsAdapter()


        getCats(dbQuery)


        catsRecyclerView.adapter = catsAdapter
        catsAdapter.setOnCatClickListener(object : CatsAdapter.OnCatClickListener {
            override fun onCatClick(id: Int) {
                val catId:String = catsAdapter.getCats()[id].id
                val image:String = catsAdapter.getCats()[id].image
                val intent = Intent(this@MainActivity,DetailActivity::class.java)
                intent.putExtra("id",catId)
                intent.putExtra("image",image)
                startActivity(intent)
            }
        })
        filtersButton.setOnClickListener(this)
        filtersButtonClear.setOnClickListener(this)

    }
    private fun getCats(dbQuery:DBQueries){
        dbQuery.getActualCatsFromDB(pref,viewModel).let {
            it?.observe(this) { t ->
                catsAdapter.setCats(t as ArrayList<Cat>)
            }
        }
        filterUIChange(dbQuery)
    }
    private fun filterUIChange(dbQuery:DBQueries){
        if(dbQuery.filterOn(pref)){
            filtersButton.background = ContextCompat.getDrawable(this,R.drawable.toggle_on)
            filtersButtonClear.visibility = View.VISIBLE
            filterStatus.text = dbQuery.setFilterStatus(pref)
        }
        else{
            filtersButton.background = ContextCompat.getDrawable(this,R.drawable.toggle_off)
            filtersButtonClear.visibility = View.INVISIBLE
            filterStatus.text = ""
        }

    }

    override fun onResume() {
        super.onResume()
        val dbQuery = DBQueries()
        getCats(dbQuery)
    }

    override fun onDestroy() {
        super.onDestroy()
        pref.edit().putString(DBQueries.rawQuery, "").apply()
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.filtersButton ->{
                val intent = Intent(this,FilterActivity::class.java)
                startActivity(intent)
            }
            R.id.filtersButtonClear ->{
                val dbQuery = DBQueries()
                dbQuery .cleanFilter(pref)
                getCats(dbQuery)
            }
        }
    }


}
