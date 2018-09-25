package id.eaz.soccerclub.ui.main.event

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import id.eaz.soccerclub.R
import kotlinx.android.synthetic.main.fragment_event.*

class EventFragment : Fragment() {

    companion object {

        @JvmStatic
        fun newInstance() : EventFragment {
            return EventFragment()
        }
    }

    private lateinit var eventVp: EventVpAdapter

    override fun onAttach(context: Context?) {
        super.onAttach(context)


        eventVp = EventVpAdapter(childFragmentManager)
        eventVp.addTitles(getString(R.string.upcoming))
        eventVp.addTitles(getString(R.string.last))
        eventVp.reload()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_event, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vp_event.adapter = eventVp
        tab_event.setupWithViewPager(vp_event)
    }
}
