package id.eaz.soccerclub.ui.main.team

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
import id.eaz.soccerclub.R
import id.eaz.soccerclub.api.NetworkState
import id.eaz.soccerclub.api.NetworkState.Companion.LOADING
import id.eaz.soccerclub.api.Status.FAILED
import id.eaz.soccerclub.ui.detail.team.TeamDetailActivity
import id.eaz.soccerclub.vo.TeamVo
import kotlinx.android.synthetic.main.fragment_item.*
import org.jetbrains.anko.support.v4.onRefresh
import org.jetbrains.anko.support.v4.toast
import org.koin.android.architecture.ext.viewModel
import id.eaz.soccerclub.ui.detail.team.TeamDetailActivity.EXTRA
import org.jetbrains.anko.startActivity

class TeamFragment : Fragment() {

    companion object {
        private const val ARG_LEAGUE_STR = "arg_league_id"
        private const val ARG_QUERY = "arg_query"
        private const val ARG_FAV = "arg_fav"
        @JvmStatic
        fun newInstance(league: String = "", isFav: Boolean = false, query: String? = null)
                : TeamFragment {
            val args = Bundle().apply {
                putString(ARG_LEAGUE_STR, league)
                putString(ARG_QUERY, query)
                putBoolean(ARG_FAV, isFav)
            }

            val teamFragment = TeamFragment()
            teamFragment.arguments = args

            return teamFragment
        }
    }

    private lateinit var league: String
    private var isFav = false
    private var query: String? = null

    private val teamVM by viewModel<TeamVM>()
    private lateinit var skeletonList : RecyclerViewSkeletonScreen

    private val teamItemAdapter = TeamItemAdapter {
        activity?.startActivity<TeamDetailActivity>(EXTRA.ID_TEAM.name to it.idTeam)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        arguments?.let {
            league = it.getString(ARG_LEAGUE_STR)
            isFav = it.getBoolean(ARG_FAV)
            query = it.getString(ARG_QUERY)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_item, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        load()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rv_event.layoutManager = LinearLayoutManager(activity,
                LinearLayoutManager.VERTICAL, false)
        rv_event.isNestedScrollingEnabled = false
        rv_event.adapter = teamItemAdapter

        skeletonList = Skeleton.bind(rv_event).shimmer(false).adapter(teamItemAdapter)
                .load(R.layout.item_club_skeleton).show()

        srl_event.onRefresh {
            load()
        }

        subsVM()
    }

    private fun load(){
        when {
            isFav -> teamVM.loadFavs()
            !isFav && query != null -> teamVM.searchTeams(query?:"o")
            else -> teamVM.reqTeamList.postValue(league)
        }
    }

    private fun subsVM(){
        if(isFav)
            teamVM.teamFavs.observe(this, Observer {
                skeletonList.hide()
                if (it != null) {
                    if(it.isEmpty())
                        showEmpty()
                    else
                        bindEvents(it)
                }
                else showEmpty()
            })
        else {
            if(query != null) {
                teamVM.nsSearchTeams.observe(this, nsObserver)
                teamVM.teamFounds.observe(this, Observer {
                    if (it != null) bindEvents(it)
                    else showEmpty()
                })
            } else {
                teamVM.nsReqTeamList.observe(this, nsObserver)
                teamVM.teamList.observe(this, Observer {
                    if (it != null) bindEvents(it)
                    else showEmpty()
                })
            }
        }
    }

    private val nsObserver: Observer<NetworkState> = Observer {
        if(it!=null)
            if (it != LOADING){
                skeletonList.hide()
                if(it.status == FAILED)
                    toast(it.msg ?: "Unknown Error")
            } else skeletonList.show()
    }

    private fun bindEvents(teams: List<TeamVo>){
        teamItemAdapter.setList(teams)

        srl_event.post {
            srl_event.isRefreshing = false
        }

        if (!teams.isEmpty()) {
            rv_event.visibility = View.VISIBLE
            ll_event_empty.visibility = View.GONE
        }
    }

    private fun showEmpty(){
        rv_event.visibility = View.GONE
        ll_event_empty.visibility = View.VISIBLE
    }
}
