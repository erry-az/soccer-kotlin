package id.eaz.soccerclub.repo

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.util.Log
import id.eaz.soccerclub.api.ECallBack
import id.eaz.soccerclub.api.NetworkState
import id.eaz.soccerclub.api.service.EventApi
import id.eaz.soccerclub.api.service.TeamApi
import id.eaz.soccerclub.db.dao.EventFavDAO
import id.eaz.soccerclub.vo.EventVo
import id.eaz.soccerclub.vo.TeamVo
import retrofit2.Response

class EventRepo(private val eventApi: EventApi, private val teamApi: TeamApi,
                private val eventFavDAO: EventFavDAO) {

    val events = MutableLiveData<List<EventVo>>()
    val eventPasts = MutableLiveData<List<EventVo>>()
    val eventDetail = MutableLiveData<List<EventVo>>()
    val eventFavs = MutableLiveData<List<EventVo>>()
    val eventFounds = MutableLiveData<List<EventVo>>()

    fun getUpcomingEvent(leagueId: String) : LiveData<NetworkState> {
        events.value = listOf()
        val cb = callback {
            events.value = it
        }
        eventApi.nextEvents(leagueId).enqueue(cb)
        return cb.networkStateMLD
    }

    fun getLastEvent(leagueId: String): LiveData<NetworkState> {
        eventPasts.value = listOf()
        val cb = callback {
            eventPasts.value = it
        }
        eventApi.pastEvents(leagueId).enqueue(cb)
        return cb.networkStateMLD
    }

    fun getEventDetail(eventId:String): LiveData<NetworkState>{
        eventDetail.value = listOf()
        val cbd= callback {
            eventDetail.value = it
        }
        eventApi.detailEvent(eventId).enqueue(cbd)
        return cbd.networkStateMLD
    }

    fun searchEvents(search: String): LiveData<NetworkState>{
        eventFounds.value = listOf()
        val cb = callback {
            eventFounds.value = it
        }
        eventApi.searchEvent(search).enqueue(cb)
        return cb.networkStateMLD
    }

    fun addToFav(eventVo: EventVo){
        eventFavDAO.add(eventVo)
        loadFavs()
    }

    fun removeFromFav(eventId: String){
        eventFavDAO.removeByIdEvent(eventId)
        loadFavs()
    }

    fun loadFavs() {
        eventFavs.value = eventFavDAO.getAll()
    }

    private fun callback(listener: (List<EventVo>?) -> Unit) = object :
    ECallBack<EventApi.EventRes>() {
        override fun success(response: Response<EventApi.EventRes>,
                             nsMld: MutableLiveData<NetworkState>) {

            val eventRes = response.body() ?: EventApi.EventRes()
            val eventResList = eventRes.events ?: eventRes.event
            if (eventResList != null) {
                val eventSoccers = eventResList.filter {
                    !it.strLeague.startsWith("_") && it.strSport.toLowerCase() == "soccer"
                }
                Log.d("eventRepo", "teamfound " + eventSoccers.size)
                listener(eventSoccers)
                if(eventSoccers.size == 1)
                eventSoccers.map { event ->
                    event.favorite = eventFavDAO.isFavorite(event.idEvent)
                    getTeamDetail(event.idHomeTeam) {
                        event.homeTeam = it
                        listener(eventSoccers)
                    }

                    getTeamDetail(event.idAwayTeam) {
                        event.awayTeam = it
                        listener(eventSoccers)
                    }

                }
            } else
                listener(null)
        }
    }


    fun getTeamDetail(teamId : String, listener: (TeamVo?) -> Unit){
        val callback = object  : ECallBack<TeamApi.TeamRes>(){
            override fun success(response: Response<TeamApi.TeamRes>,
                                 nsMld: MutableLiveData<NetworkState>) {
                val teamRes = response.body() ?: TeamApi.TeamRes()
                val teamResList = teamRes.teams ?: listOf()
                if (teamResList.isNotEmpty())
                    listener(teamResList[0])
                else
                    listener(null)
            }
        }
        teamApi.teamDetail(teamId).enqueue(callback)
    }

}