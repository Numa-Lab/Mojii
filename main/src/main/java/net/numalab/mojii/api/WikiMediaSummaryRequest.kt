package net.numalab.mojii.api

import net.numalab.mojii.lang.Lang

// FIXME ひらがなで検索をかけると、カタカナには変換されることはない(ひらがなでも表記する単語はちゃんと返ってくる)
class WikiMediaSummaryRequest(val keyWord: String, val lang: Lang, val exchars: Int) {
    companion object {
        val url =
            { word: String, lang: Lang, exChars: Int -> "https://${lang.langSet.langCode()}.wikipedia.org/w/api.php?format=json&action=query&prop=extracts&exintro&explaintext&redirects=1&titles=${word}&exchars=${exChars}" }
    }

    fun url() = url(keyWord, lang, exchars)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other is WikiMediaSummaryRequest) {
            return keyWord == other.keyWord && lang == other.lang && exchars == other.exchars
        }

        return false
    }

    override fun hashCode(): Int {
        var result = keyWord.hashCode()
        result = 31 * result + lang.hashCode()
        result = 31 * result + exchars
        return result
    }
}