package id.eaz.soccerclub.ui.detail.team.player

import android.arch.lifecycle.Observer
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ethanhua.skeleton.RecyclerViewSkeletonScreen
import com.ethanhua.skeleton.Skeleton
import org.koin.android.architecture.ext.viewModel
import id.eaz.soccerclub.R
import id.eaz.soccerclub.api.NetworkState
import id.eaz.soccerclub.api.NetworkState.Companion.LOADING
import kotlinx.android.synthetic.main.fragment_item.*
import org.jetbrains.anko.support.v4.onRefresh
import org.jetbrains.anko.support.v4.toast
import id.eaz.soccerclub.api.Status
import id.eaz.soccerclub.ui.detail.player.PlayerDetailActivity
import id.eaz.soccerclub.vo.PlayerVo
import id.eaz.soccerclub.ui.detail.player.PlayerDetailActivity.EXTRA
import org.jetbrains.anko.startActivity

class PlayerFragment : Fragment(){

    companion object {
        private const val ARG_TEAM_ID = "arg_team_id"
        @JvmStatic
        fun newInstance(team: String?) : PlayerFragment {
            val args = Bundle().apply {
                putString(ARG_TEAM_ID, team)
            }

            val playerFragment = PlayerFragment()
            playerFragment.arguments = args

            return playerFragment
        }
    }

    private var teamId: String? = null

    private val playerVM by viewModel<PlayerVM>()
    private lateinit var skeletonList : RecyclerViewSkeletonScreen
    private val playerItemAdapter = PlayerItemAdapter {
        activity?.startActivity<PlayerDetailActivity>(EXTRA.PLAYER_ID.name to it.idPlayer)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        arguments?.let {
            teamId = it.getString(ARG_TEAM_ID)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_item, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rv_event.layoutManager = LinearLayoutManager(activity,
                LinearLayoutManager.VERTICAL, false)
        rv_event.isNestedScrollingEnabled = false
        rv_event.adapter = playerItemAdapter

        skeletonList = Skeleton.bind(rv_event).shimmer(false).adapter(playerItemAdapter)
                .load(R.layout.item_club_skeleton).show()

        srl_event.onRefresh {
            load()
        }

        subsVM()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        load()
    }

    private fun load(){
        playerVM.reqPlayers(teamId ?: "")
    }

    private fun subsVM(){
        playerVM.nsReqPlayers.observe(this, nsObserver)
        playerVM.playerList.observe(this, Observer {
            if(it != null) bindPlayers(it)
            else showEmpty()
        })
    }

    private val nsObserver: Observer<NetworkState> = Observer {
        if(it!=null)
            if (it != LOADING){
                skeletonList.hide()
                if(it.status == Status.FAILED)
                    toast(it.msg ?: "Unknown Error")
            } else skeletonList.show()
    }

    private fun bindPlayers(players: List<PlayerVo>){
        playerItemAdapter.setList(players)

        srl_event.post {
            if(srl_event != null)
            srl_event.isRefreshing = false
        }

        if (!players.isEmpty()) {
            rv_event.visibility = View.VISIBLE
            ll_event_empty.visibility = View.GONE
        }
    }

    private fun showEmpty(){
        rv_event.visibility = View.GONE
        ll_event_empty.visibility = View.VISIBLE
    }
}