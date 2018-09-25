package id.eaz.soccerclub.ui.main.event

import android.arch.lifecycle.Observer
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ethanhua.skeleton.RecyclerViewSkeletonScreen
import com.ethanhua.skeleton.Skeleton
import id.eaz.soccerclub.AppPref
import id.eaz.soccerclub.R
import id.eaz.soccerclub.api.NetworkState
import id.eaz.soccerclub.api.NetworkState.Companion.LOADING
import id.eaz.soccerclub.api.Status.FAILED
import id.eaz.soccerclub.ui.detail.event.DetailActivity
import id.eaz.soccerclub.ui.detail.event.DetailActivity.EXTRA
import id.eaz.soccerclub.vo.EventVo
import kotlinx.android.synthetic.main.fragment_item.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.support.v4.onRefresh
import org.jetbrains.anko.support.v4.toast
import org.koin.android.architecture.ext.viewModel
import org.koin.android.ext.android.inject

class EventDetailFragment : Fragment(){

    companion object {
        private const val ARG_LEAGUE_TYPE = "arg_league_type"
        private const val ARG_FAV = "arg_fav"
        private const val ARG_QUERY = "arg_query"

        fun newInstance(type: EventVM.TYPE = EventVM.TYPE.UPCOMING,
                        isFav: Boolean = false, query: String? = null)
                : EventDetailFragment {
            val args = Bundle().apply {
                putSerializable(ARG_LEAGUE_TYPE, type)
                putBoolean(ARG_FAV, isFav)
                putString(ARG_QUERY, query)
            }

            val eventDetailFragment = EventDetailFragment()
            eventDetailFragment.arguments = args

            return eventDetailFragment
        }
    }

    private val appPref: AppPref by inject()

    private lateinit var leagueType: EventVM.TYPE
    private var isFav = false
    private var query: String? = null

    private val eventVm by viewModel<EventVM>()

    private lateinit var skeletonList : RecyclerViewSkeletonScreen

    private val matchItemAdapter = EventItemAdapter {
        activity?.startActivity<DetailActivity>(EXTRA.ID_EVENT.name to it.idEvent)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        arguments?.let {
            leagueType = it.getSerializable(ARG_LEAGUE_TYPE) as EventVM.TYPE
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
        rv_event.adapter = matchItemAdapter

        skeletonList = Skeleton.bind(rv_event).shimmer(false).adapter(matchItemAdapter)
                .load(R.layout.item_event_skeleton).show()

        srl_event.onRefresh {
            load()
        }

        subsVM()
    }

    private fun load(){
        when {
            isFav -> eventVm.loadFavs()
            !isFav && query != null -> eventVm.searchEvents(query ?: "fiorentina")
            else -> {
                eventVm.reqLeagueData.postValue(appPref.getString(AppPref.Key.STR_LEAGUE_ID))
                eventVm.reqEventList.postValue(leagueType)
            }
        }
    }

    private fun subsVM(){
        if(!isFav) {
            if(query != null) {
                eventVm.nsSearchEvents.observe(this, nsObserver)
                eventVm.eventFounds.observe(this, Observer {
                    if (it != null) {
                        Log.d("edf", "search found " + it.size)
                        bindEvents(it)
                    }
                    else showEmpty()
                })
            } else {
                eventVm.nsEventList.observe(this, nsObserver)

                if (leagueType == EventVM.TYPE.UPCOMING)
                    eventVm.eventList.observe(this, Observer {
                        if (it != null) bindEvents(it)
                        else showEmpty()
                    })

                if (leagueType == EventVM.TYPE.PAST)
                    eventVm.eventPastList.observe(this, Observer {
                        if (it != null) bindEvents(it)
                        else showEmpty()
                    })
            }
        } else
            eventVm.eventFavs.observe(this, Observer {
                if (it != null) bindEvents(it)
                else showEmpty()
            })
    }

    private val nsObserver: Observer<NetworkState> = Observer {
        if(it!=null)
            if (it != LOADING){
                skeletonList.hide()
                if(it.status == FAILED)
                    toast(it.msg ?: "Unknown Error")
            } else skeletonList.show()
    }

    private fun bindEvents(events: List<EventVo>){
        if(activity != null) {
            skeletonList.hide()
            matchItemAdapter.setList(events)

            srl_event.post {
                if(srl_event != null) {
                    srl_event.isRefreshing = false
                }
            }

            if (!events.isEmpty()) {
                rv_event.visibility = View.VISIBLE
                ll_event_empty.visibility = View.GONE
            }
        }
    }

    private fun showEmpty(){
        skeletonList.hide()
        rv_event.visibility = View.GONE
        ll_event_empty.visibility = View.VISIBLE
    }
}