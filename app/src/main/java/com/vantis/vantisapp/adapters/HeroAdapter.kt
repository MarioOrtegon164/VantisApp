package com.vantis.vantisapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vantis.vantisapp.R
import com.vantis.vantisapp.holders.HeroViewHolder
import com.vantis.vantisapp.models.Hero
import java.util.ArrayList

/**
 * Created by LTI.Mario Josue Ortegon Chan on 4/8/2022.
 */
class HeroAdapter(private val heroList: MutableList<Hero>, private val viewFragment: View): RecyclerView.Adapter<HeroViewHolder>() {

    private var context: Context? = null
    private var heroes: MutableList<Hero> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeroViewHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        context = parent.context;
        return HeroViewHolder(layoutInflater.inflate(R.layout.hero_recycler_view_model,parent,false))
    }

    override fun onBindViewHolder(holder: HeroViewHolder, position: Int) {
//        val item:Prospect = prospectList[position]
        val item: Hero = heroList[position]

        holder.bind(item,context,viewFragment)
    }

    override fun getItemCount(): Int = heroList.size

    fun updateList(heroList: MutableList<Hero>, oldCount: Int, heroListSize: Int) {
        this.heroes = heroList
        notifyItemRangeInserted(oldCount, heroListSize)
    }

}