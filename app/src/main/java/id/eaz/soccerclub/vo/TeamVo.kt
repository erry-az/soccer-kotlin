package id.eaz.soccerclub.vo

import com.google.gson.annotations.SerializedName

data class TeamVo(@SerializedName("idTeam")
                  val idTeam: String = "") {
    @SerializedName("intStadiumCapacity")
    var intStadiumCapacity: String = ""
    @SerializedName("strTeamShort")
    var strTeamShort: String = ""
    @SerializedName("intFormedYear")
    var intFormedYear: String = ""
    @SerializedName("strSport")
    var strSport: String = ""
    @SerializedName("strInstagram")
    var strInstagram: String = ""
    @SerializedName("strTeamJersey")
    var strTeamJersey: String = ""
    @SerializedName("strDescriptionEN")
    var strDescriptionEN: String = ""
    @SerializedName("strTeamFanart2")
    var strTeamFanart2: String = ""
    @SerializedName("strTeamFanart3")
    var strTeamFanart3: String = ""
    @SerializedName("strTeamFanart4")
    var strTeamFanart4: String = ""
    @SerializedName("strWebsite")
    var strWebsite: String = ""
    @SerializedName("strYoutube")
    var strYoutube: String = ""
    @SerializedName("strStadiumDescription")
    var strStadiumDescription: String = ""
    @SerializedName("strTeamFanart1")
    var strTeamFanart1: String = ""
    @SerializedName("strLocked")
    var strLocked: String = ""
    @SerializedName("intLoved")
    var intLoved: String = ""
    @SerializedName("idLeague")
    var idLeague: String = ""
    @SerializedName("idSoccerXML")
    var idSoccerXML: String = ""
    @SerializedName("strAlternate")
    var strAlternate: String = ""
    @SerializedName("strTeam")
    var strTeam: String = ""
    @SerializedName("strTwitter")
    var strTwitter: String = ""
    @SerializedName("strGender")
    var strGender: String = ""
    @SerializedName("strTeamLogo")
    var strTeamLogo: String = ""
    @SerializedName("strStadiumLocation")
    var strStadiumLocation: String = ""
    @SerializedName("strStadium")
    var strStadium: String = ""
    @SerializedName("strFacebook")
    var strFacebook: String = ""
    @SerializedName("strTeamBadge")
    var strTeamBadge: String? = ""
    @SerializedName("strCountry")
    var strCountry: String = ""
    @SerializedName("strRSS")
    var strRSS: String = ""
    @SerializedName("strTeamBanner")
    var strTeamBanner: String = ""
    @SerializedName("strLeague")
    var strLeague: String = ""
    @SerializedName("strManager")
    var strManager: String = ""
    @SerializedName("strStadiumThumb")
    var strStadiumThumb: String = ""
    @SerializedName("strKeywords")
    var strKeywords: String = ""
    var players: List<PlayerVo>? = listOf()
    var favourites: Boolean = false
}