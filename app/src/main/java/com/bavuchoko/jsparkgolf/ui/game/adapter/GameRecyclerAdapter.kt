package com.bavuchoko.jsparkgolf.ui.game.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bavuchoko.jsparkgolf.R
import com.bavuchoko.jsparkgolf.dto.response.GameResponseDto

class GameRecyclerAdapter(
    val items: ArrayList<GameResponseDto>,
    private val itemClickListener: OnItemClickListener) : RecyclerView.Adapter<GameRecyclerAdapter.ViewHolder>(){

    interface OnItemClickListener{
        fun onItemClick(url:String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_game, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val game = items[position]
        // 클릭 리스너 설정
        holder.bind(game, itemClickListener)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(game: GameResponseDto, clickListener: OnItemClickListener) {
            itemView.setOnClickListener {
                clickListener.onItemClick(game.url)
            }
        }
    }
}