package com.bavuchoko.jsparkgolf.adpater

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bavuchoko.jsparkgolf.R
import com.bavuchoko.jsparkgolf.vo.GameVo

class GameRecyclerAdapter(
    private val items: Map<String, List<GameVo>>,
    private val itemClickListener: OnItemClickListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(id: Long, state: String)
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
            holder.bind(itemList[position] as GameVo, itemClickListener)
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    inner class DateViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val dateTextView: TextView = view.findViewById(R.id.dateTextView)

        @SuppressLint("SetTextI18n")
        fun bind(date: String) {
            dateTextView.text = date.substring(5,7) +"월 "+date.substring(8,10) +"일"
        }
    }

    inner class GameViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val host_name_player: TextView = view.findViewById(R.id.host_name_player)
        private val playTime: TextView = view.findViewById(R.id.play_time)
        private val fieldName: TextView = view.findViewById(R.id.field_name)
        private val progressNow: TextView = view.findViewById(R.id.progress_now)
        private val companionCount: TextView = view.findViewById(R.id.companion_count)

        @SuppressLint("SetTextI18n")
        fun bind(game: GameVo, clickListener: OnItemClickListener) {
            val hasCompanion : Boolean = if (game.players.size > 1) true else false
            host_name_player.text = game.host.name
            companionCount.text =if(hasCompanion) "${game.players.size}" else "1"
            playTime.text = game.playDate.substring(11)
            progressNow.text = "${game.progress.half}C : ${game.progress.hole}H"
            fieldName.text = game.fields.name
            itemView.setOnClickListener {
                clickListener.onItemClick(game.id, game.progress.state)
            }
        }
    }
}
