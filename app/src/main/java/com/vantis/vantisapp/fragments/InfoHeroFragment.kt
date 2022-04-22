package com.vantis.vantisapp.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.concredito.concreditoapp.api.RestEngine
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import com.vantis.vantisapp.R
import com.vantis.vantisapp.adapters.HeroExpandableListAdapter
import com.vantis.vantisapp.databinding.FragmentInfoHeroBinding
import com.vantis.vantisapp.models.Hero
import com.vantis.vantisapp.models.PowerStat
import com.vantis.vantisapp.routes.HeroesRoutes
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class InfoHeroFragment : Fragment() {

    val TAG = "InfoHeroFragment"
    lateinit var  binding: FragmentInfoHeroBinding

    //Variables para peticion con retrofit
    private val apiService: HeroesRoutes = RestEngine.getRestEngine().create(HeroesRoutes::class.java)
    val gson = Gson()

    //Variables para manejo de datos
    private lateinit var hero: Hero
    private var expandableListAdapter: HeroExpandableListAdapter? = null
    private var heroDataList: List<String>? = null
    var powerStats:ArrayList<PowerStat> = ArrayList()

    //Variables para elementos de la vista
    var chart: BarChart? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentInfoHeroBinding.inflate(inflater, container, false)
        return binding.root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Se obtiene el id del heroe del fragmento anterior
        val heroId = arguments?.getString("id")!!
        getHero(heroId.toInt())
    }

    //Obtiene 1 registro por ID
    private fun getHero(heroId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            apiService.getHero(heroId)?.enqueue(object : Callback<Hero> {
                override fun onResponse(
                    call: Call<Hero>,
                    response: Response<Hero>
                ) {
                    val r = response.body()
                    if (r != null) {
                        if (response.isSuccessful && r.response == "success") {
                            hero = r
                            configDataHero(hero)
                            initStyleBarChart(hero)
                        }
                    }
                }

                override fun onFailure(call: Call<Hero>, t: Throwable) {
                    Log.d(TAG, "Se produjo un error al realizar la peticion a la API  ${t.message}")
                }
            })
        }
    }

    //Se configura la info del heroe (Imagen,Nombre)
    private fun configDataHero(hero: Hero){

        //Mostrar imagen del heroe
        Picasso.get().load(hero.image.url) .resize(220, 220)
            .transform(CropCircleTransformation())
            .error(R.drawable.no_image)
            .into(binding.ivHeroInfoImage)

        //Mostrar nombre del heroe
        binding.tvHeroInfoName.text = hero.name

        //Configurar los demas datos del heroe para mostrar en un expandible list view
        setupExpandableListView()
    }

    //Inicia la configuracion de grafica para las estats de cada heroe
    private fun initStyleBarChart(hero: Hero) {
        val heroStats = hero.powerstats
        chart = binding.bcStatsHero

        //Se validan si los stats vienen con valor nulo
        if (heroStats.intelligence == "null"){
            heroStats.intelligence = "0"
        }

        if (heroStats.strength == "null"){
            heroStats.strength = "0"
        }

        if (heroStats.speed == "null"){
            heroStats.speed = "0"
        }

        if (heroStats.durability == "null"){
            heroStats.durability = "0"
        }

        if (heroStats.power == "null"){
            heroStats.power = "0"
        }

        if (heroStats.combat == "null"){
            heroStats.combat = "0"
        }

        //Se crea el objeto powerStats para mostrar en la grafica
        powerStats.add(PowerStat("Inteligencia",heroStats.intelligence.toFloat()))
        powerStats.add(PowerStat("Fuerza",heroStats.strength.toFloat()))
        powerStats.add(PowerStat("Velocidad",heroStats.speed.toFloat()))
        powerStats.add(PowerStat("Durabilidad",heroStats.durability.toFloat()))
        powerStats.add(PowerStat("Poder",heroStats.power.toFloat()))
        powerStats.add(PowerStat("Combate",heroStats.combat.toFloat()))

        configStyleBarChart()

        //Se inicializa el objeto donde se almacenaran los datos a mostrar en la grafica
        val entries: ArrayList<BarEntry> = ArrayList()

        //Se almacenan los datos
        for (i in powerStats.indices) {
            val powerStat = powerStats[i]
            entries.add(BarEntry(i.toFloat(), powerStat.powerStatValue))
        }

        val barDataSet = BarDataSet(entries, "")
        barDataSet.setColors(*ColorTemplate.COLORFUL_COLORS)

        val data = BarData(barDataSet)
        chart!!.data = data

        //Renderizar la grafica
        chart!!.invalidate()

    }

    //Se configuran los estilos de la grafica de las estats del heroe
    private fun configStyleBarChart() {

        chart = binding.bcStatsHero
        chart?.axisLeft?.setDrawGridLines(false)
        val xAxis: XAxis = binding.bcStatsHero.xAxis
        xAxis.setDrawGridLines(false)
        xAxis.setDrawAxisLine(false)

        //remover y-axis derecha
        chart!!.axisRight.isEnabled = false

        //remover legenda de grafica
        chart!!.legend.isEnabled = false

        //remover la descripcion de la grafica
        binding.bcStatsHero.description.isEnabled = false


        //Agregar animacion
        chart?.animateY(1000)

        // Se configuran los estilos para cada texto de cada barra de la grafica
        xAxis.position = XAxis.XAxisPosition.BOTTOM_INSIDE
        xAxis.valueFormatter = StatsTittlesAxisFormatter()
        xAxis.setDrawLabels(true)
        xAxis.granularity = 1f
        xAxis.labelRotationAngle = +90f

    }

    //Se configuran los textos para cada barra
    inner class StatsTittlesAxisFormatter : IndexAxisValueFormatter() {

        override fun getAxisLabel(value: Float, axis: AxisBase?): String {
            val index = value.toInt()
            return if (index < powerStats.size) {
                powerStats[index].powerStatName
            } else {
                ""
            }
        }
    }

    //Se configura el expandible list view para la demas info del heroe
    private fun setupExpandableListView() {
        val expandableListView = binding.expandableListView
        val listData = getRelatedInfoHero()

        heroDataList = ArrayList(listData.keys)
        expandableListAdapter = HeroExpandableListAdapter(context, heroDataList as ArrayList<String>, listData)
        expandableListView.setAdapter(expandableListAdapter)

        //Eventos onclick para las listas
        expandableListView.setOnGroupExpandListener { groupPosition ->}

        expandableListView.setOnGroupCollapseListener { groupPosition ->}

        expandableListView.setOnChildClickListener { parent, v, groupPosition, childPosition, id -> false }
    }

    //Se configura la demas info del heroe para el expandible list view (Biografia,Apariencia,Trabajo y conexiones)
    private fun getRelatedInfoHero(): HashMap<String, List<String>> {
        val listData = HashMap<String, List<String>>()

        //Se configura la Biografia
        val heroBiography = ArrayList<String>()
        var heroName: String = if (hero.biography.fullName != "") hero.biography.fullName else hero.name

        heroBiography.add("Nombre completo: $heroName")
        heroBiography.add("Alter egos: ${hero.biography.alterEgos}")
        heroBiography.add("Aliados: ${hero.biography.aliases}")
        heroBiography.add("Lugar de nacimiento: ${hero.biography.placeOfBirth}")
        heroBiography.add("Primera aparición: ${hero.biography.firstAppearance}")
        heroBiography.add("Alineación: ${hero.biography.alignment}")

        //Se configura la Apariencia
        val heroAppearance = ArrayList<String>()
        heroAppearance.add("Género: ${hero.appearance.gender}")
        heroAppearance.add("Raza: ${if (hero.appearance.race != "null") hero.appearance.race else "Desconocido"}")
        heroAppearance.add("Altura: ${hero.appearance.height}")
        heroAppearance.add("Peso: ${hero.appearance.weight}")
        heroAppearance.add("Color de ojos: ${hero.appearance.eyeColor}")
        heroAppearance.add("Color de pelo: ${hero.appearance.hairColor}")

        //Se configura el Trabajo
        val heroWork = ArrayList<String>()
        heroWork.add("Ocupación: ${hero.work.occupation}")
        heroWork.add("Base: ${hero.work.base}")

        //Se configura las conexiones
        val heroConnections = ArrayList<String>()
        heroConnections.add("Grupo de afiliación: ${hero.connections.groupAffiliation}")
        heroConnections.add("Parientes: ${hero.connections.relatives}")

        listData["Apariencia"] = heroAppearance
        listData["Biografia"] = heroBiography
        listData["Conexiones"] = heroConnections
        listData["Trabajo"] = heroWork

        return listData
    }

}

