package id.eaz.soccerclub.ui.main.event

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.PagerAdapter

class EventVpAdapter(fm: FragmentManager): FragmentStatePagerAdapter(fm) {

    private var fragments = listOf<Fragment>()
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

    fun reload(){
        fragments = listOf(EventDetailFragment.newInstance(EventVM.TYPE.UPCOMING),
                EventDetailFragment.newInstance(EventVM.TYPE.PAST))
        notifyDataSetChanged()
    }
}