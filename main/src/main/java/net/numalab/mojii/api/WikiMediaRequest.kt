package net.numalab.mojii.api

import net.numalab.mojii.lang.Lang

class WikiMediaRequest(val keyWord: String, val lang: Lang) {
    companion object {
        val url =
            { word: String, lang: Lang -> "https://${lang.langSet.langCode()}.wikipedia.org/w/api.php?action=query&prop=extracts&exintro&explaintext&redirects=1&titles=${word}&format=json" }
    }

    fun url() = url(keyWord, lang)
}

