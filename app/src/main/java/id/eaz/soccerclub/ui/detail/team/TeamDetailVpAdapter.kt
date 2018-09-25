package id.eaz.soccerclub.ui.detail.team

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.PagerAdapter
import id.eaz.soccerclub.ui.detail.team.info.TeamInfoFragment
import id.eaz.soccerclub.ui.detail.team.player.PlayerFragment
import id.eaz.soccerclub.vo.TeamVo

class TeamDetailVpAdapter(fm: FragmentManager)
    : FragmentStatePagerAdapter(fm) {

    private var fragments : List<Fragment> = listOf()
    private var titles  = mutableListOf<String>()
    private var activeFragment: Fragment? = null

    override fun getItem(position: Int) : Fragment {
        activeFragment = fragments[position]
        return activeFragment as Fragment
    }

    override fun getCount() = fragments.size

    override fun getItemPosition(`object`: Any): Int {
        return PagerAdapter.POSITION_NONE
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return titles[position]
    }

    fun addTitles(charSequence: CharSequence){
        titles.add(charSequence.toString())
        notifyDataSetChanged()
    }

    fun reload(team: TeamVo){
        fragments = listOf(
                TeamInfoFragment.newInstance(team.strStadium, team.strLeague, team.strTeamJersey,
                        team.strTeamLogo, team.strDescriptionEN),
                PlayerFragment.newInstance(team.idTeam)
        )
        notifyDataSetChanged()
    }
}
