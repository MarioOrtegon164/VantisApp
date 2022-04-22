package com.vantis.vantisapp.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.concredito.concreditoapp.api.RestEngine
import com.google.gson.Gson
import com.vantis.vantisapp.adapters.HeroAdapter
import com.vantis.vantisapp.databinding.FragmentListHeroesBinding
import com.vantis.vantisapp.models.Hero
import com.vantis.vantisapp.routes.HeroesRoutes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ListHeroesFragment : Fragment() {

    val TAG = "ListHeroesFragment"
    private lateinit var binding: FragmentListHeroesBinding;

    //Variables para elementos de la vista
    var rvHeroes : RecyclerView? = null
    private var progressBar :ProgressBar? = null

    //Variables para peticion con retrofit
    private val apiService: HeroesRoutes = RestEngine.getRestEngine().create(HeroesRoutes::class.java)
    val gson = Gson()

    //Variables para manejo de datos
    private lateinit var adapter:HeroAdapter
    private lateinit var hero: Hero
    var heroList: MutableList<Hero> = ArrayList()
    var isLoading = false
    private lateinit var viewFragment: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentListHeroesBinding.inflate(inflater, container, false)

        //Se oculta la toolbar
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()

        //Se obtiene las referencias de la vista
        viewFragment = binding.root;
        rvHeroes = binding.rvHeroes
        progressBar = binding.pbLoading

        initAdapter()
        populateData()

        return binding.root
    }

    //Iniciamos el adaptador del RecyclerView
    private fun initAdapter() {
        Log.d(TAG, "initAdapter")
        heroList.clear()
        adapter = HeroAdapter(heroList,viewFragment)
        binding.rvHeroes.layoutManager = LinearLayoutManager(activity)
        binding.rvHeroes.adapter = adapter
    }

    //Generamos los primeros registros para mostrar
    private fun populateData() {
        Log.d(TAG, "populateData")
        var i = 1
        while (i < 10) {
            getHero(i)
            i++
        }
        Handler(Looper.getMainLooper()).postDelayed({
            initScrollListener()
        }, 1000)
    }

    //Evento de scroll para que se llame a la funcion de loadMore
    private fun initScrollListener() {
        Log.d(TAG, "initScrollListener")

        binding.rvHeroes.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager?
                if (!isLoading) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() === heroList.size - 1) {
                        //bottom of list!
                        loadMore()
                        isLoading = true
                        progressBar!!.visibility = View.VISIBLE
                    }
                }
            }
        })
    }

    //Obtiene los siguientes registros para mostrar en el RecyclerView
    private fun loadMore() {
        Log.d(TAG, "loadMore")

        //Se agrega un elemento para actualizar la lista y luego se elimina
        var hero = heroList[0]
        heroList.add(hero)
        binding.rvHeroes.post {
            adapter.notifyItemInserted(heroList.size - 1)
            heroList.removeAt(heroList.size - 1)
            adapter.notifyItemRemoved(heroList.size - 1)
        }

        Handler(Looper.getMainLooper()).postDelayed({
            val scrollPosition: Int = heroList.size
            var currentSize = scrollPosition
            val nextLimit = currentSize + 10
            while (currentSize - 1 < nextLimit) {
                getHero(currentSize)
                currentSize++
            }
            adapter.notifyDataSetChanged()
            progressBar!!.visibility = View.GONE
            isLoading = false
        }, 4000)

    }

    //Obtiene 1 registro por ID
    private fun getHero(heroId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            Log.d(TAG, "getListHeroes $heroId")
            apiService.getHero(heroId)?.enqueue(object : Callback<Hero> {
                override fun onResponse(
                    call: Call<Hero>,
                    response: Response<Hero>
                ) {
                    val r = response.body()
                    if (r != null) {
                        if (response.isSuccessful && r.response == "success") {
                            //Log.d(TAG, "Response success  ${r}")
                            hero = r
                            heroList.add(hero)
                            adapter.notifyDataSetChanged()
                            ///Log.d(TAG, "heroList ${heroList.toString()}")
                        }
                    }
                }

                override fun onFailure(call: Call<Hero>, t: Throwable) {
                    Log.d(TAG, "Se produjo un error al realizar la peticion a la API  ${t.message}")
                }
            })
        }
    }

}