package net.numalab.mojii.api

import net.numalab.mojii.lang.Lang

class WikiMediaSummaryRequest(val keyWord: String, val lang: Lang, val exchars: Int) {
    companion object {
        val url =
            { word: String, lang: Lang, exChars: Int -> "https://${lang.langSet.langCode()}.wikipedia.org/w/api.php?format=json&action=query&prop=extracts&exintro&explaintext&redirects=1&titles=${word}&exchars=${exChars}" }
    }

    fun url() = url(keyWord, lang, exchars)
}