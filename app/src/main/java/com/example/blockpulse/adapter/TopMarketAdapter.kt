package com.example.blockpulse.adapter

import android.content.Context
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.Navigation
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.blockpulse.R
import com.example.blockpulse.databinding.TopCurrencyLayoutBinding
import com.example.blockpulse.fragment.HomeFragmentDirections
import com.example.blockpulse.fragment.models.CryptoCurrency

class TopMarketAdapter (var context :Context , val list : List<CryptoCurrency>): RecyclerView.Adapter<TopMarketAdapter.TopMarketViewHolder>() {
    inner class TopMarketViewHolder(view : View) :RecyclerView.ViewHolder(view) {

     var binding = TopCurrencyLayoutBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopMarketViewHolder {
      return TopMarketViewHolder(LayoutInflater.from(context).inflate(R.layout.top_currency_layout , parent , false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: TopMarketViewHolder, position: Int) {
        val item = list [position]
        holder.binding.topCurrencyNameTextView.text = item.name
        Glide.with(context).load(
            "https://s2.coinmarketcap.com/static/img/coins/64x64/"+ item.id + ".png"
        ).thumbnail(Glide.with(context).load(R.drawable.spinner))
            .into(holder.binding.topCurrencyImageView)
        if(item.quotes[0].percentChange24h>0){
        holder.binding.topCurrencyChangeTextView.setTextColor(context.resources.getColor(R.color.green))
            holder.binding.topCurrencyChangeTextView.text = "+ ${String.format("%.02f" , item.quotes[0].percentChange24h)} %"
        }else{
            holder.binding.topCurrencyChangeTextView.setTextColor(context.resources.getColor(R.color.red))
            holder.binding.topCurrencyChangeTextView.text = " ${String.format("%.02f ", item.quotes[0].percentChange24h)} %"
        }
        holder.itemView.setOnClickListener{
            findNavController(it).navigate(
              HomeFragmentDirections.actionHomeFragmentToDeatilsFragment(item)
            )
        }


    }
}