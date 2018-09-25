package id.eaz.soccerclub.ui.detail.team

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.v7.app.AppCompatActivity
import android.view.View
import id.eaz.soccerclub.R
import id.eaz.soccerclub.api.NetworkState
import id.eaz.soccerclub.api.Status
import id.eaz.soccerclub.loadImage
import id.eaz.soccerclub.vo.TeamVo
import kotlinx.android.synthetic.main.activity_team_detail.*
import org.jetbrains.anko.design.snackbar
import org.jetbrains.anko.imageResource
import org.jetbrains.anko.toast
import org.koin.android.architecture.ext.viewModel


class TeamDetailActivity : AppCompatActivity(), AppBarLayout.OnOffsetChangedListener {

    enum class EXTRA {
        ID_TEAM
    }

    private val PERCENTAGE_TO_ANIMATE_AVATAR = 20
    private var mIsAvatarShown = true
    private var mMaxScrollSize: Int = 0
    private lateinit var teamId: String

    private val teamDetailVM by viewModel<TeamDetailVM>()
    private lateinit var teamDetailVpAdapter: TeamDetailVpAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_team_detail)

        teamId = intent.getStringExtra(EXTRA.ID_TEAM.name)

        td_app_bar.addOnOffsetChangedListener(this)
        mMaxScrollSize = td_app_bar.totalScrollRange
        teamDetailVpAdapter = TeamDetailVpAdapter(supportFragmentManager)
        load()
        subsVM()
    }

    private fun load(){
        teamDetailVM.reqTeamDetails(teamId)
    }

    private fun subsVM(){
        teamDetailVM.teamDetails.observe(this, Observer { teams ->
            teams?.map {
                bindView(it)
            }
        })

        teamDetailVM.nsReqTeamDetails.observe(this, Observer {
            if(it != null)
            if(it != NetworkState.LOADING){
                showLoading(false)
                if(it.status == Status.FAILED)
                    toast(it.msg ?: "unknown")
            } else
                showLoading(true)
        })
    }

    private fun bindView(teamVo: TeamVo){
        teamDetailVpAdapter.reload(teamVo)
        teamDetailVpAdapter.addTitles(getString(R.string.info))
        teamDetailVpAdapter.addTitles(getString(R.string.Player))
        td_view_pager.adapter = teamDetailVpAdapter
        td_tab_layout.setupWithViewPager(td_view_pager)
        td_civ_team_badge.loadImage(this, teamVo.strTeamBadge)
        td_tv_team_name.text = teamVo.strTeam
        td_tv_team_date.text = teamVo.intFormedYear
        td_iv_bg_head.loadImage(this, teamVo.strStadiumThumb,
                R.drawable.stadium_default, 250)
        if (teamVo.favourites)
            td_fab_fav.imageResource = R.drawable.ic_favorite_black_24dp
        else
            td_fab_fav.imageResource = R.drawable.ic_favorite_border_white_24dp

        td_fab_fav.setOnClickListener {
            if (teamVo.favourites) {
                snackbar(td_cl_detail, R.string.del_fav)
                teamDetailVM.removeFav(teamVo.idTeam)
            } else {
                snackbar(td_cl_detail, R.string.add_fav)
                teamDetailVM.addToFav(teamVo)
            }
        }
    }

    private fun showLoading(load: Boolean){
        td_fab_fav_loading.visibility = if (load) View.VISIBLE else View.GONE
    }

    override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
        if (mMaxScrollSize == 0)
            mMaxScrollSize = appBarLayout?.totalScrollRange ?: 0

        val percentage = Math.abs(verticalOffset) * 100 / mMaxScrollSize

        if (percentage >= PERCENTAGE_TO_ANIMATE_AVATAR && mIsAvatarShown) {
            mIsAvatarShown = false

            td_civ_team_badge.animate()
                    .scaleY(0F).scaleX(0F)
                    .setDuration(200)
                    .start()
        }

        if (percentage <= PERCENTAGE_TO_ANIMATE_AVATAR && !mIsAvatarShown) {
            mIsAvatarShown = true

            td_civ_team_badge.animate()
                    .scaleY(1F).scaleX(1F)
                    .start()
        }
    }
}
