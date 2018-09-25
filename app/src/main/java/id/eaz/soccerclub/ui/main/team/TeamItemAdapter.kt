package id.eaz.soccerclub.ui.main.team

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import id.eaz.soccerclub.R
import id.eaz.soccerclub.loadImage
import id.eaz.soccerclub.vo.TeamVo
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_club.*

class TeamItemAdapter(private var teams: List<TeamVo> = listOf(),
                      private val listener: (TeamVo) -> Unit) :
        RecyclerView.Adapter<TeamItemAdapter.VH>() {

    fun setList(teams: List<TeamVo>){
        this.teams = teams
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = VH (
        LayoutInflater.from(parent.context)
                .inflate(R.layout.item_club, parent, false)
    )

    override fun getItemCount() = teams.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(teams[position])
    }


    inner class VH(override val containerView: View) : RecyclerView.ViewHolder(containerView),
            LayoutContainer {
        fun bind(team : TeamVo){
            tv_club.text = team.strTeam
            iv_club.loadImage(containerView.context, team.strTeamBadge)
            itemView.setOnClickListener {
                listener(team)
            }
        }
    }
}