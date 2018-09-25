package id.eaz.soccerclub.ui.detail.event

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import id.eaz.soccerclub.*
import id.eaz.soccerclub.api.NetworkState
import id.eaz.soccerclub.api.Status
import id.eaz.soccerclub.vo.EventVo
import kotlinx.android.synthetic.main.activity_detail.*
import org.jetbrains.anko.design.snackbar
import org.jetbrains.anko.imageResource
import org.jetbrains.anko.toast
import org.koin.android.architecture.ext.viewModel
import java.text.SimpleDateFormat
import java.util.*

class DetailActivity : AppCompatActivity() {

    enum class EXTRA {
        ID_EVENT
    }

    lateinit var idEvent: String

    private val detailVM by viewModel<DetailVM>()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        idEvent = intent.getStringExtra(EXTRA.ID_EVENT.name)

        detailVM.reqEventDetail.value = idEvent

        fab_fav.hide()

        srl_detail.setOnRefreshListener {
            detailVM.reqEventDetail.postValue(idEvent)
        }

        subsVM()
    }

    private fun subsVM(){
        detailVM.eventDetail.observe(this, Observer {
            if (it!=null && it.isNotEmpty()){
                bindView(it[0])
            }
        })

        detailVM.lvNsEventDetail.observe(this, Observer {
            if(it!=null){
                if(it != NetworkState.LOADING){
                    srl_detail.post {
                        srl_detail.isRefreshing = false
                    }
                    if (it.status == Status.FAILED)
                        toast(it.msg ?: "Unknown error")
                    else
                        fab_fav.show()
                } else {
                    srl_detail.isRefreshing = true
                    fab_fav.hide()
                }
            }
        })
    }

    private fun bindView(event: EventVo){
        if(idEvent == event.idEvent) {
            Log.d(DetailActivity::class.java.name, "eventid view : ${event.idEvent}")
            tv_detail_away.enableMarque(event.strAwayTeam)
            tv_detail_awayScore.text = event.intAwayScore ?: "-"
            tv_detail_home.enableMarque(event.strHomeTeam)
            tv_detail_homeScore.text = event.intHomeScore ?: "-"

            iv_detail_home.loadImage(this, event.homeTeam?.strTeamBadge)
            iv_detail_away.loadImage(this, event.awayTeam?.strTeamBadge)

            
            val date = event.dateEvent
            val time = event.strTime
            val format = SimpleDateFormat(when {
                time.contains("+") -> "yyyy-MM-dd HH:mm:ssZ"
                time.contains(" ") -> "yyyy-MM-dd HH:mm z"
                else -> "yyyy-MM-dd HH:mm:ss"
            }, Locale.getDefault())

            val dateTime = format.parse("$date $time")
            tv_detail_dow.text = SimpleDateFormat("EEEE",
                    Locale.getDefault()).format(dateTime)
            tv_detail_date.text = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    .format(dateTime)
            tv_detail_time.text = SimpleDateFormat("HH:mm", Locale.getDefault())
                    .format(dateTime)

            tv_dg_home.text = event.strHomeGoalDetails.ctr()
            tv_dg_away.text = event.strAwayGoalDetails.ctr()

            tv_drc_home.text = event.strHomeRedCards.ctr()
            tv_drc_away.text = event.strAwayRedCards.ctr()

            tv_dyc_home.text = event.strHomeYellowCards.ctr()
            tv_dyc_away.text = event.strAwayYellowCards.ctr()

            tv_dgk_home.text = event.strHomeLineupGoalkeeper.cstr()
            tv_dgk_away.text = event.strAwayLineupGoalkeeper.cstr()

            tv_ddf_home.text = event.strHomeLineupDefense.cstr()
            tv_ddf_away.text = event.strAwayLineupDefense.cstr()

            tv_dmf_home.text = event.strHomeLineupMidfield.cstr()
            tv_dmf_away.text = event.strAwayLineupMidfield.cstr()

            tv_dfw_home.text = event.strHomeLineupForward.cstr()
            tv_dfw_away.text = event.strAwayLineupForward.cstr()

            tv_dsb_home.text = event.strHomeLineupSubstitutes.cstr()
            tv_dsb_away.text = event.strAwayLineupSubstitutes.cstr()

            if(event.favorite)
                fab_fav.imageResource = R.drawable.ic_favorite_black_24dp
            else
                fab_fav.imageResource = R.drawable.ic_favorite_border_white_24dp

            fab_fav.setOnClickListener {
                if (event.favorite) {
                    snackbar(cl_detail, R.string.del_fav)
                    detailVM.removeFromFav(event.idEvent)
                } else {
                    snackbar(cl_detail, R.string.add_fav)
                    detailVM.addToFav(event)
                }
            }
        }
    }
}