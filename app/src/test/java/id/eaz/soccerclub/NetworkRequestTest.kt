package id.eaz.soccerclub

import id.eaz.soccerclub.api.ApiClient
import id.eaz.soccerclub.api.service.EventApi
import id.eaz.soccerclub.api.service.LeagueApi
import org.junit.Assert
import org.junit.Before
import org.junit.Test


class NetworkRequestTest {

    private lateinit var leagueApi: LeagueApi
    private lateinit var eventApi: EventApi

    @Before
    fun initFun(){
        leagueApi = ApiClient().retrofit.create(LeagueApi::class.java)
        eventApi = ApiClient().retrofit.create(EventApi::class.java)
    }

    @Test
    fun getAllLeague_test(){
        try {
            val getAllLeague = leagueApi.allLeagues().execute()
            Assert.assertEquals(getAllLeague.isSuccessful, true)
        } catch (e: Exception){
            e.printStackTrace()
        }
    }

    @Test
    fun getEventPast_test(){
        try {
            val getEventPast = eventApi.pastEvents("4332")
                    .execute()
            Assert.assertEquals(getEventPast.isSuccessful, true)
        } catch (e: Exception){
            e.printStackTrace()
        }
    }

    @Test
    fun getEventNext_test(){
        try {
            val getEventNext = eventApi.nextEvents("4332")
                    .execute()
            Assert.assertEquals(getEventNext.isSuccessful, true)
        } catch (e: Exception){
            e.printStackTrace()
        }
    }
}