package net.numalab.mojii.api

import com.google.gson.Gson
import com.google.gson.JsonElement

data class WikiMediaSearchResponse(
    val query: Query,
    val batchcomplete: String
)

data class Query(
    val normalized: Normalized,
    val redirects: List<Redirect>,
    private val pages: JsonElement
) {
    fun getPages(gson: Gson): List<Page> {
        return pages.asJsonObject.entrySet().map {
            val el = it.value
            gson.fromJson(el, Page::class.java)
        }.filter {
            it.pageid != 0  // 無効記事は除外
        }
    }
}

data class Redirect(
    val from: String,
    val to: String
)

data class Normalized(
    val from: String,
    val to: String
)

data class Page(
    val pageid: Int,
    val ns: Int,
    val title: String,
    val extract: String?
)