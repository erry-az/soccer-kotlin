package id.eaz.soccerclub.ui.main.event

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import id.eaz.soccerclub.R
import id.eaz.soccerclub.api.ApiClient
import id.eaz.soccerclub.api.service.TeamApi
import id.eaz.soccerclub.enableMarque
import id.eaz.soccerclub.loadImage
import id.eaz.soccerclub.vo.EventVo
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_event.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class EventItemAdapter(private var eventList: List<EventVo> = listOf(),
                       private val listener: (EventVo) -> Unit) :
        RecyclerView.Adapter<EventItemAdapter.VH>() {

    val teamApi = ApiClient().retrofit.create(TeamApi::class.java)

    fun setList(eventList: List<EventVo>){
        this.eventList = eventList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = VH(
            LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_event, parent, false)
    )


    override fun getItemCount() = eventList.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(eventList[position])
    }

    inner class VH(override val containerView: View) : RecyclerView.ViewHolder(containerView),
            LayoutContainer {
        fun bind(event: EventVo){
            tv_ie_away.enableMarque(event.strAwayTeam)
            tv_ie_awayScore.text = event.intAwayScore ?: "-"
            tv_ie_home.enableMarque(event.strHomeTeam)
            tv_ie_homeScore.text = event.intHomeScore ?: "-"

            loadEmblem(event.idHomeTeam, iv_ie_home)
            loadEmblem(event.idAwayTeam, iv_ie_away)

            if(!event.dateEvent.isBlank() && !event.strTime.isBlank()){
                val date = event.dateEvent
                val time = event.strTime
                val format = SimpleDateFormat(when {
                    time.contains("+") -> "yyyy-MM-dd HH:mm:ssZ"
                    time.contains(" ") -> "yyyy-MM-dd HH:mm z"
                    else -> "yyyy-MM-dd HH:mm:ss"
                }, Locale.getDefault())

                val dateTime = format.parse("$date $time")
                tv_ie_dow.text = SimpleDateFormat("EEEE",
                        Locale.getDefault()).format(dateTime)
                tv_ie_date.text = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                        .format(dateTime)
                tv_ie_time.text = SimpleDateFormat("HH:mm", Locale.getDefault())
                        .format(dateTime)
            }

            itemView.setOnClickListener {
                listener(event)
            }
        }

        private fun loadEmblem(teamId: String, iv: ImageView){
            teamApi.teamDetail(teamId).enqueue(object : Callback<TeamApi.TeamRes>{
                override fun onResponse(call: Call<TeamApi.TeamRes>,
                                        response: Response<TeamApi.TeamRes>) {
                    val teamBody = response.body()
                    val teamRes = teamBody?.teams
                    if(teamRes == null)
                        iv.loadImage(containerView.context, null)
                    else
                        teamRes.map {
                            iv.loadImage(containerView.context, it.strTeamBadge)
                        }
                }

                override fun onFailure(call: Call<TeamApi.TeamRes>, t: Throwable) {
                    iv.loadImage(containerView.context, null)
                }
            })
        }
    }
}