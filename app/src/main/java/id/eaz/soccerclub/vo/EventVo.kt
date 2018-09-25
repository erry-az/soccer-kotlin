package id.eaz.soccerclub.vo

import com.google.gson.annotations.SerializedName

data class EventVo(@SerializedName("idEvent")
                   val idEvent: String = "")  {
    @SerializedName("intHomeShots")
    var intHomeShots: Int = 0
    @SerializedName("strSport")
    var strSport: String = ""
    @SerializedName("strHomeLineupDefense")
    var strHomeLineupDefense: String? = null
    @SerializedName("strAwayLineupSubstitutes")
    var strAwayLineupSubstitutes: String? = null
    @SerializedName("idLeague")
    var idLeague: String = ""
    @SerializedName("idSoccerXML")
    var idSoccerXML: String = ""
    @SerializedName("strHomeLineupForward")
    var strHomeLineupForward: String? = null
    @SerializedName("strTVStation")
    var strTVStation: String = ""
    @SerializedName("strHomeGoalDetails")
    var strHomeGoalDetails: String? = null
    @SerializedName("strAwayLineupGoalkeeper")
    var strAwayLineupGoalkeeper: String? = null
    @SerializedName("strAwayLineupMidfield")
    var strAwayLineupMidfield: String? = null
    @SerializedName("intRound")
    var intRound: String = ""
    @SerializedName("strHomeYellowCards")
    var strHomeYellowCards: String? = null
    @SerializedName("idHomeTeam")
    var idHomeTeam: String = ""
    @SerializedName("intHomeScore")
    var intHomeScore: String? = null
    @SerializedName("dateEvent")
    var dateEvent: String = ""
    @SerializedName("strCountry")
    var strCountry: String = ""
    @SerializedName("strAwayTeam")
    var strAwayTeam: String = ""
    @SerializedName("strHomeLineupMidfield")
    var strHomeLineupMidfield: String? = null
    @SerializedName("strDate")
    var strDate: String = ""
    @SerializedName("strHomeFormation")
    var strHomeFormation: String = ""
    @SerializedName("strMap")
    var strMap: String = ""
    @SerializedName("idAwayTeam")
    var idAwayTeam: String = ""
    @SerializedName("strAwayRedCards")
    var strAwayRedCards: String? = null
    @SerializedName("strBanner")
    var strBanner: String = ""
    @SerializedName("strFanart")
    var strFanart: String = ""
    @SerializedName("strDescriptionEN")
    var strDescriptionEN: String = ""
    @SerializedName("strResult")
    var strResult: String = ""
    @SerializedName("strCircuit")
    var strCircuit: String = ""
    @SerializedName("intAwayShots")
    var intAwayShots: String = ""
    @SerializedName("strFilename")
    var strFilename: String = ""
    @SerializedName("strTime")
    var strTime: String = ""
    @SerializedName("strAwayGoalDetails")
    var strAwayGoalDetails: String? = null
    @SerializedName("strAwayLineupForward")
    var strAwayLineupForward: String? = null
    @SerializedName("strLocked")
    var strLocked: String = ""
    @SerializedName("strSeason")
    var strSeason: String = ""
    @SerializedName("intSpectators")
    var intSpectators: String = ""
    @SerializedName("strHomeRedCards")
    var strHomeRedCards: String? = null
    @SerializedName("strHomeLineupGoalkeeper")
    var strHomeLineupGoalkeeper: String? = null
    @SerializedName("strHomeLineupSubstitutes")
    var strHomeLineupSubstitutes: String? = null
    @SerializedName("strAwayFormation")
    var strAwayFormation: String = ""
    @SerializedName("strEvent")
    var strEvent: String = ""
    @SerializedName("strAwayYellowCards")
    var strAwayYellowCards: String? = null
    @SerializedName("strAwayLineupDefense")
    var strAwayLineupDefense: String? = null
    @SerializedName("strHomeTeam")
    var strHomeTeam: String = ""
    @SerializedName("strThumb")
    var strThumb: String = ""
    @SerializedName("strLeague")
    var strLeague: String = ""
    @SerializedName("intAwayScore")
    var intAwayScore: String? = null
    @SerializedName("strCity")
    var strCity: String = ""
    @SerializedName("strPoster")
    var strPoster: String = ""
    var homeTeam: TeamVo? = null
    var awayTeam: TeamVo? = null
    var favorite: Boolean = false
}