package ge.gmegrelishvili.weatherapp

class Country {
    companion object {
        const val CapitalGe = "Tbilisi"
        const val CapitalUk = "London"
        const val CapitalJa = "Kingston"
        const val CapitalDefault = CapitalGe

        fun getCapital(countryCode: String): String? {
            return when (countryCode.lowercase()) {
                "ge" -> CapitalGe
                "uk" -> CapitalUk
                "ja" -> CapitalJa
                else -> null
            }
        }
    }
}