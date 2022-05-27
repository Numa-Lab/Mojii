package net.numalab.mojii.api

import net.numalab.mojii.lang.Lang

class WikiMediaSearchRequest(val keyWord: String, val lang: Lang) {
    companion object {
        val url =
            { word: String, lang: Lang -> "https://${lang.langSet.langCode()}.wikipedia.org/w/api.php?action=query&prop=extracts&exintro&explaintext&redirects=1&titles=${word}&format=json" }
    }

    fun url() = url(keyWord, lang)
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as WikiMediaSearchRequest

        if (keyWord != other.keyWord) return false
        if (lang != other.lang) return false

        return true
    }

    override fun hashCode(): Int {
        var result = keyWord.hashCode()
        result = 31 * result + lang.hashCode()
        return result
    }
}

