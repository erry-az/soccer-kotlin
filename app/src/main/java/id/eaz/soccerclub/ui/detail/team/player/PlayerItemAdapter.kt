package id.eaz.soccerclub.ui.detail.team.player

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import id.eaz.soccerclub.R
import id.eaz.soccerclub.loadImage
import id.eaz.soccerclub.vo.PlayerVo
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_club.*

class PlayerItemAdapter(private var players: List<PlayerVo> = listOf(),
                        private val listener: (PlayerVo) -> Unit) :
        RecyclerView.Adapter<PlayerItemAdapter.VH>() {

    fun setList(players: List<PlayerVo>){
        this.players = players
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = VH (
        LayoutInflater.from(parent.context)
                .inflate(R.layout.item_club, parent, false)
    )

    override fun getItemCount() = players.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(players[position])
    }


    inner class VH(override val containerView: View) : RecyclerView.ViewHolder(containerView),
            LayoutContainer {
        fun bind(player : PlayerVo){
            tv_club.text = player.strPlayer
            iv_club.loadImage(containerView.context, player.strCutout)
            itemView.setOnClickListener {
                listener(player)
            }
        }
    }
}