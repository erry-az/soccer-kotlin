package id.eaz.soccerclub.ui.main

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.PagerAdapter
import id.eaz.soccerclub.ui.main.event.EventDetailFragment
import id.eaz.soccerclub.ui.main.event.EventFragment
import id.eaz.soccerclub.ui.main.favourite.FavFragment
import id.eaz.soccerclub.ui.main.team.TeamFragment

class MainVpAdapter(fm: FragmentManager)
    : FragmentStatePagerAdapter(fm) {

    private var fragments : List<Fragment> = listOf()
    private var activeFragment: Fragment? = null

    override fun getItem(position: Int) : Fragment {
        activeFragment = fragments[position]
        return activeFragment as Fragment
    }

    override fun getCount() = fragments.size

    override fun getItemPosition(`object`: Any): Int {
        return PagerAdapter.POSITION_NONE
    }

    fun reload(league: String){
        fragments = listOf(EventFragment.newInstance(),
                TeamFragment.newInstance(league), FavFragment.newInstance())
        notifyDataSetChanged()
    }

    fun search(query: String?){
        fragments = listOf(EventDetailFragment.newInstance(query = query),
                TeamFragment.newInstance(query = query), FavFragment.newInstance())
        notifyDataSetChanged()
    }
}