package id.eaz.soccerclub.ui.main.favourite

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import id.eaz.soccerclub.R
import kotlinx.android.synthetic.main.fragment_event.*

class FavFragment : Fragment() {
    companion object {
        @JvmStatic
        fun newInstance() : FavFragment {
            return FavFragment()
        }
    }

    private lateinit var favVpAdapter: FavVpAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_event, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        favVpAdapter = FavVpAdapter(childFragmentManager)
        favVpAdapter.addTitles(getString(R.string.Matches))
        favVpAdapter.addTitles(getString(R.string.Teams))
        favVpAdapter.reload()
        vp_event.adapter = favVpAdapter
        tab_event.setupWithViewPager(vp_event)
    }
}
