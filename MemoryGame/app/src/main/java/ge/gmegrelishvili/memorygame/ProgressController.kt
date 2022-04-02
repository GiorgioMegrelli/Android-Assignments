package ge.gmegrelishvili.memorygame

interface ProgressController {
    fun success()
    fun fail()
    fun clearHighlight()
    fun clearScores()
}