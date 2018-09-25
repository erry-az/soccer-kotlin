package id.eaz.soccerclub.ui.detail.team.info


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import id.eaz.soccerclub.R
import id.eaz.soccerclub.loadImage
import kotlinx.android.synthetic.main.fragment_team_info.*

class TeamInfoFragment : Fragment() {
    private var stadium: String? = null
    private var league: String? = null
    private var home: String? = null
    private var away: String? = null
    private var desc: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            stadium = it.getString(ARG_STADIUM)
            league = it.getString(ARG_LEAGUE)
            home = it.getString(ARG_HOME_IMG)
            away = it.getString(ARG_AWAY_IMG)
            desc = it.getString(ARG_DESC)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_team_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ti_tv_league.text = league
        ti_tv_stadium.text = stadium
        ti_tv_desc.text = desc
        ti_iv_home.loadImage(activity!!, home, R.drawable.jersey_placeholder)
        ti_iv_away.loadImage(activity!!, away, R.drawable.jersey_placeholder)
    }

    companion object {
        private const val ARG_STADIUM = "arg_stadium"
        private const val ARG_LEAGUE = "arg_league"
        private const val ARG_HOME_IMG = "arg_home"
        private const val ARG_AWAY_IMG = "arg_away"
        private const val ARG_DESC = "arg_desc"

        @JvmStatic
        fun newInstance(stadium: String?, league: String?, home: String?, away: String?,
                        desc: String?) =
                TeamInfoFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_STADIUM, stadium)
                        putString(ARG_LEAGUE, league)
                        putString(ARG_HOME_IMG, home)
                        putString(ARG_AWAY_IMG, away)
                        putString(ARG_DESC, desc)
                    }
                }
    }
}
