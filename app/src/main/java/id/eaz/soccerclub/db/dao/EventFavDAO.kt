package id.eaz.soccerclub.db.dao

import id.eaz.soccerclub.db.DbConn
import id.eaz.soccerclub.vo.EventVo
import id.eaz.soccerclub.vo.TeamVo
import org.jetbrains.anko.db.*

class EventFavDAO(private val db: DbConn) {

    companion object {
        const val TBL_FAV_EVENT = "tbl_fav"
        const val FAV_EV_ID = "fav_ev_id"
        const val FAV_EV_TIME = "fav_ev_time"
        const val FAV_EV_DATE = "fav_ev_date"
        const val FAV_EV_HOME_TEAM = "fav_ev_home_team"
        const val FAV_EV_AWAY_TEAM = "fav_ev_away_team"
        const val FAV_EV_HOME_BADGE = "fav_ev_home_badge"
        const val FAV_EV_AWAY_BADGE = "fav_ev_away_badge"
        const val FAV_EV_HOME_SCORE = "fav_ev_home_score"
        const val FAV_EV_AWAY_SCORE = "fav_ev_away_score"
    }

    fun add(event: EventVo){
        db.use {
            insert(TBL_FAV_EVENT,
                    FAV_EV_ID to event.idEvent,
                    FAV_EV_DATE to event.dateEvent,
                    FAV_EV_TIME to event.strTime,
                    FAV_EV_HOME_TEAM to event.strHomeTeam,
                    FAV_EV_AWAY_TEAM to event.strAwayTeam,
                    FAV_EV_HOME_BADGE to event.homeTeam?.strTeamBadge,
                    FAV_EV_AWAY_BADGE to event.awayTeam?.strTeamBadge,
                    FAV_EV_HOME_SCORE to event.intHomeScore,
                    FAV_EV_AWAY_SCORE to event.intAwayScore)

        }
    }

    fun removeByIdEvent(idEvent: String){
        db.use {
            delete(TBL_FAV_EVENT, "$FAV_EV_ID = {idEvent}",
                    "idEvent" to idEvent)
        }
    }

    fun getAll() : List<EventVo> {
        return db.use {
            return@use select(TBL_FAV_EVENT,
                    FAV_EV_ID, FAV_EV_DATE, FAV_EV_TIME, FAV_EV_HOME_TEAM, FAV_EV_AWAY_TEAM,
                    FAV_EV_HOME_SCORE, FAV_EV_AWAY_SCORE, FAV_EV_HOME_BADGE, FAV_EV_AWAY_BADGE)
                    .orderBy(DbConn.ID, SqlOrderDirection.DESC).parseList(EventFavRP())
        }
    }

    fun isFavorite(idEvent: String) : Boolean {
        return db.use {
            return@use select(TBL_FAV_EVENT,
                    FAV_EV_ID, FAV_EV_DATE, FAV_EV_TIME, FAV_EV_HOME_TEAM, FAV_EV_AWAY_TEAM,
                    FAV_EV_HOME_SCORE, FAV_EV_AWAY_SCORE, FAV_EV_HOME_BADGE, FAV_EV_AWAY_BADGE)
                    .whereArgs("$FAV_EV_ID = {idEvent}", "idEvent" to idEvent)
                    .parseList(EventFavRP()).isNotEmpty()
        }
    }

    class EventFavRP : RowParser<EventVo>{
        override fun parseRow(columns: Array<Any?>): EventVo {
            val event = EventVo(idEvent = columns[0] as String)
            event.dateEvent = columns[1] as String
            event.strTime = columns[2] as String
            event.strHomeTeam = columns[3] as String
            event.strAwayTeam = columns[4] as String
            event.intHomeScore = columns[5] as String? ?: "-"
            event.intAwayScore = columns[6] as String? ?: "-"
            event.homeTeam = TeamVo()
            event.homeTeam?.strTeamBadge = columns[7] as String? ?: "-"
            event.awayTeam = TeamVo()
            event.awayTeam?.strTeamBadge = columns[8] as String? ?: "-"

            return event
        }

    }
}