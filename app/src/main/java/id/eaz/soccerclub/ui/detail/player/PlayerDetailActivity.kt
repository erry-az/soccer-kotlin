package id.eaz.soccerclub.ui.detail.player

import android.arch.lifecycle.Observer
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import id.eaz.soccerclub.R
import id.eaz.soccerclub.api.NetworkState
import id.eaz.soccerclub.api.Status
import id.eaz.soccerclub.loadImage
import id.eaz.soccerclub.vo.PlayerVo
import kotlinx.android.synthetic.main.activity_player_detail.*
import org.jetbrains.anko.toast
import org.koin.android.architecture.ext.viewModel

class PlayerDetailActivity : AppCompatActivity() {

    private val playerDetailVM by viewModel<PlayerDetailVM>()
    private var playerId : String? = null

    enum class EXTRA {
        PLAYER_ID
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player_detail)
        playerId = intent.getStringExtra(EXTRA.PLAYER_ID.name)
        Log.d("plda", playerId)
        subsVM()
        load()
    }

    private fun load(){
        playerDetailVM.getPlayerDetails(playerId ?: "")
    }

    private fun subsVM(){
        playerDetailVM.playerDetails.observe(this, Observer { players ->
            if (players != null && players.isNotEmpty()){
                players.map {
                    bindView(it)
                }
            }
        })

        playerDetailVM.nsPlayerDetails.observe(this, Observer {
            if(it != null)
                if(it != NetworkState.LOADING){
                    pd_pb.visibility = View.GONE
                    if(it.status == Status.FAILED) toast(it.msg ?: "")
                } else pd_pb.visibility = View.VISIBLE
        })
    }

    private fun bindView(player: PlayerVo){
        pd_tv_player_name.text = player.strPlayer
        pd_tv_player_date.text = player.dateBorn
        pd_tv_team.text = player.strTeam
        pd_tv_national.text = player.strNationality
        pd_tv_desc.text = player.strDescriptionEN

        pd_civ_player_badge.loadImage(this, player.strCutout)
        pd_iv_bg_head.loadImage(this, player.strFanart1, R.drawable.stadium_default,
                250)
    }
}
