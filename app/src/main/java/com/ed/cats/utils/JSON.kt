package com.ed.cats.utils


import com.ed.cats.data.Cat
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class JSON {
    companion object{
        private const val ID = "id"
        private const val TEMPERAMENT = "temperament"
        private const val ORIGIN = "origin"
        private const val ADAPTABILITY = "adaptability"
        private const val AFFECTION_LEVEL = "affection_level"
        private const val CHILD_FRIENDLY = "child_friendly"
        private const val GROOMING = "grooming"
        private const val HEALTH_ISSUES = "health_issues"
        private const val INTELLIGENCE = "intelligence"
        private const val SHEDDING_LEVEL = "shedding_level"
        private const val SOCIAL_NEEDS = "social_needs"
        private const val STRANGER_FRIENDLY = "stranger_friendly"
        private const val VOCALISATION = "vocalisation"
        private const val EXPERIMANTAL = "experimental"
        private const val HAIRLESS = "hairless"
        private const val NATURAL = "natural"
        private const val RARE = "rare"
        private const val REX = "rex"
        private const val SUPPRESSED_TAIL = "suppressed_tail"
        private const val SHORT_LEGS = "short_legs"
        private const val WIKI = "wikipedia_url"
        private const val HYPOALLERGENIC = "hypoallergenic"
        private const val NAME : String = "name"
        private const val LIFE_SPAN : String = "life_span"
        private const val DESCRIPTION : String = "description"
        private const val IMAGE : String = "image"
        private const val IMAGE_URL : String = "url"


        fun getCatsFromJSON (catsJSON: JSONArray?):ArrayList<Cat> {
            val catsArray = ArrayList<Cat>()
            if (catsJSON != null) {
                var id : String
                var name : String
                var description : String
                var life_span : String
                var image : String
                var temperament : String
                var origin : String
                var adaptability : Int
                var affection_level : Int
                var child_friendly : Int
                var grooming : Int
                var health_issues : Int
                var intelligence : Int
                var shedding_level : Int
                var social_needs : Int
                var stranger_friendly : Int
                var vocalisation : Int
                var experimental : Int
                var hairless : Int
                var natural : Int
                var rare : Int
                var rex : Int
                var suppressed_tail : Int
                var short_legs : Int
                var wikipedia_url : String
                var hypoallergenic : Int
                try{
                    for(x in 0 until catsJSON.length()){
                        val jString = JSONObject(catsJSON.getString(x))
                        if(jString.has(ID)) id = jString.get(ID) as String else continue
                        if(jString.has(NAME)) name = jString.get(NAME) as String else continue
                        if(jString.has(DESCRIPTION)) description = jString.get(DESCRIPTION) as String else continue
                        if(jString.has(LIFE_SPAN)) life_span = jString.get(LIFE_SPAN) as String else continue
                        if(jString.has(IMAGE) && jString.getJSONObject(IMAGE).has(IMAGE_URL)) image = jString.getJSONObject(IMAGE).getString(IMAGE_URL) else continue
                        if(jString.has(TEMPERAMENT)) temperament = jString.get(TEMPERAMENT) as String else continue
                        if(jString.has(ORIGIN)) origin = jString.get(ORIGIN) as String else continue
                        if(jString.has(WIKI)) wikipedia_url = jString.get(WIKI) as String else continue
                        if(jString.has(ADAPTABILITY)) adaptability = jString.get(ADAPTABILITY) as Int else continue
                        if(jString.has(AFFECTION_LEVEL)) affection_level = jString.get(AFFECTION_LEVEL) as Int else continue
                        if(jString.has(CHILD_FRIENDLY)) child_friendly = jString.get(CHILD_FRIENDLY) as Int else continue
                        if(jString.has(GROOMING)) grooming = jString.get(GROOMING) as Int else continue
                        if(jString.has(HEALTH_ISSUES)) health_issues = jString.get(HEALTH_ISSUES) as Int else continue
                        if(jString.has(INTELLIGENCE)) intelligence = jString.get(INTELLIGENCE) as Int else continue
                        if(jString.has(SHEDDING_LEVEL)) shedding_level = jString.get(SHEDDING_LEVEL) as Int else continue
                        if(jString.has(SOCIAL_NEEDS)) social_needs = jString.get(SOCIAL_NEEDS) as Int else continue
                        if(jString.has(STRANGER_FRIENDLY)) stranger_friendly = jString.get(STRANGER_FRIENDLY) as Int else continue
                        if(jString.has(VOCALISATION)) vocalisation = jString.get(VOCALISATION) as Int else continue
                        if(jString.has(EXPERIMANTAL)) experimental = jString.get(EXPERIMANTAL) as Int else continue
                        if(jString.has(HAIRLESS)) hairless = jString.get(HAIRLESS) as Int else continue
                        if(jString.has(NATURAL)) natural = jString.get(NATURAL) as Int else continue
                        if(jString.has(RARE)) rare = jString.get(RARE) as Int else continue
                        if(jString.has(REX)) rex = jString.get(REX) as Int else continue
                        if(jString.has(SUPPRESSED_TAIL)) suppressed_tail = jString.get(SUPPRESSED_TAIL) as Int else continue
                        if(jString.has(SHORT_LEGS)) short_legs = jString.get(SHORT_LEGS) as Int else continue
                        if(jString.has(HYPOALLERGENIC)) hypoallergenic = jString.get(HYPOALLERGENIC) as Int else continue
                            val cat = Cat(
                                id,
                                name,
                                description,
                                life_span,
                                image,
                                temperament,
                                origin,
                                wikipedia_url,
                                adaptability,
                                affection_level,
                                child_friendly,
                                grooming,
                                health_issues,
                                intelligence,
                                shedding_level,
                                social_needs,
                                stranger_friendly,
                                vocalisation,
                                experimental,
                                hairless,
                                natural,
                                rare,
                                rex,
                                suppressed_tail,
                                short_legs,
                                hypoallergenic
                            )
                            catsArray.add(cat)
                        }
                }
                catch(e: JSONException){
                    e.printStackTrace()
                }

            }
            return catsArray
        }
    }
}