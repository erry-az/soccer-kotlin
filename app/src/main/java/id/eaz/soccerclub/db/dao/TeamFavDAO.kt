package id.eaz.soccerclub.db.dao

import id.eaz.soccerclub.db.DbConn
import id.eaz.soccerclub.vo.TeamVo
import org.jetbrains.anko.db.*

class TeamFavDAO(private val db: DbConn) {
    companion object {
        const val TBL_FAV_TEAM = "tbl_fav_team"
        const val FAV_TEAM_ID = "fav_team_id"
        const val FAV_TEAM_BADGE = "fav_team_badge"
        const val FAV_TEAM_NAME = "fav_team_name"
    }

    fun add(team: TeamVo){
        db.use {
            insert(TBL_FAV_TEAM,
                    FAV_TEAM_ID to team.idTeam,
                    FAV_TEAM_BADGE to team.strTeamBadge,
                    FAV_TEAM_NAME to team.strTeam)
        }
    }

    fun remove(idTeam: String){
        db.use {
            delete(TBL_FAV_TEAM, "$FAV_TEAM_ID = {idTeam}",
                    "idTeam" to idTeam)
        }
    }

    fun getAll() : List<TeamVo>{
        return db.use {
            return@use select(TBL_FAV_TEAM,
                    FAV_TEAM_ID, FAV_TEAM_NAME, FAV_TEAM_BADGE).orderBy(DbConn.ID,
                    SqlOrderDirection.DESC).parseList(TeamFavRP())
        }
    }

    fun isFavourite(idTeam: String) : Boolean {
        return db.use {
            return@use select(TBL_FAV_TEAM,
                    FAV_TEAM_ID, FAV_TEAM_NAME, FAV_TEAM_BADGE)
                    .whereArgs("$FAV_TEAM_ID = {idTeam}", "idTeam" to idTeam)
                    .parseList(TeamFavRP()).isNotEmpty()
        }
    }

    class TeamFavRP : RowParser<TeamVo>{
        override fun parseRow(columns: Array<Any?>): TeamVo {
            val team = TeamVo(idTeam = columns[0] as String)
            team.strTeam = columns[1] as String
            team.strTeamBadge = columns[2] as String

            return team
        }

    }
}