package com.bavuchoko.jsparkgolf.ui.game.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bavuchoko.jsparkgolf.R
import com.bavuchoko.jsparkgolf.dto.response.GameResponseDto

class GameRecyclerAdapter(
    private val items: Map<String, List<GameResponseDto>>,
    private val itemClickListener: OnItemClickListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(url: String)
    }

    companion object {
        private const val VIEW_TYPE_DATE = 0
        private const val VIEW_TYPE_GAME = 1
    }

    private val itemList = mutableListOf<Any>() // 날짜 및 게임 리스트를 함께 관리

    init {
        items.forEach { (date, games) ->
            itemList.add(date) // 날짜 추가
            itemList.addAll(games) // 해당 날짜의 게임 추가
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (itemList[position] is String) VIEW_TYPE_DATE else VIEW_TYPE_GAME
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_DATE) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_date_header, parent, false)
            DateViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_game, parent, false)
            GameViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is DateViewHolder) {
            holder.bind(itemList[position] as String)
        } else if (holder is GameViewHolder) {
            holder.bind(itemList[position] as GameResponseDto, itemClickListener)
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    inner class DateViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val dateTextView: TextView = view.findViewById(R.id.dateTextView)

        fun bind(date: String) {
            dateTextView.text = date.substring(5,10)
        }
    }

    inner class GameViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(game: GameResponseDto, clickListener: OnItemClickListener) {
            itemView.setOnClickListener {
                clickListener.onItemClick(game.url)
            }
            // 게임 정보를 설정하는 추가 코드를 여기에 작성
        }
    }
}
