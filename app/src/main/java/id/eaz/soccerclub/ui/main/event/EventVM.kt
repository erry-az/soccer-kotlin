package id.eaz.soccerclub.ui.main.event

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import id.eaz.soccerclub.api.NetworkState
import id.eaz.soccerclub.repo.EventRepo
import id.eaz.soccerclub.vo.EventVo

class EventVM(private val eventRepo: EventRepo) : ViewModel() {
    enum class TYPE {
        UPCOMING,
        PAST
    }

    var eventList: LiveData<List<EventVo>> = eventRepo.events
    var eventPastList: LiveData<List<EventVo>> = eventRepo.eventPasts
    val eventFavs: LiveData<List<EventVo>> = eventRepo.eventFavs
    val eventFounds: LiveData<List<EventVo>> = eventRepo.eventFounds

    var reqEventList: MutableLiveData<TYPE> = MutableLiveData()
    val reqLeagueData: MutableLiveData<String> = MutableLiveData()
    var reqSearchEvent: MutableLiveData<String> = MutableLiveData()

    val nsEventList: LiveData<NetworkState>
    val nsEventLeague: LiveData<NetworkState>
    val nsSearchEvents: LiveData<NetworkState>

    init {
        nsEventList = Transformations.switchMap(reqEventList) {
            byType(it, reqLeagueData.value)
        }

        nsEventLeague = Transformations.switchMap(reqLeagueData) {
            byType(reqEventList.value, it)
        }

        nsSearchEvents = Transformations.switchMap(reqSearchEvent){
            eventRepo.searchEvents(it)
        }
    }

    private fun byType(type: TYPE? = null, leagueId: String? = null) : LiveData<NetworkState>? {
        eventList = eventRepo.events
        return when (type) {
            TYPE.PAST -> eventRepo.getLastEvent(leagueId ?: "0")
            else -> eventRepo.getUpcomingEvent(leagueId ?: "0")
        }
    }

    fun loadFavs(){
        eventRepo.loadFavs()
    }

    fun searchEvents(query: String){
        reqSearchEvent.value = query
    }
}