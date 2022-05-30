package net.numalab.mojii.api

import com.github.bun133.tinked.RunnableTask
import com.github.bun133.tinked.TickedTask

class WikiMediaCache {
    companion object {
        val instance = WikiMediaCache()
    }

    private val summaryCache = mutableMapOf<WikiMediaSummaryRequest, WikiMediaSummaryResponse>()
    private val searchCache = mutableMapOf<WikiMediaSearchRequest, WikiMediaSearchResponse>()

    fun summary(req: WikiMediaSummaryRequest): TickedTask<Unit, WikiMediaSummaryResponse> {
        val e = summaryCache[req]
        if (e != null) {
            return instantTickedTask(e)
        } else {
            return WikiMediaSummaryRequestTask(req).apply(RunnableTask { summaryCache[req] = it;it })
        }
    }

    fun search(req: WikiMediaSearchRequest): TickedTask<Unit, WikiMediaSearchResponse> {
        val e = searchCache[req]
        if (e != null) {
            return instantTickedTask(e)
        } else {
            return WikiMediaSearchRequestTask(req).apply(RunnableTask { searchCache[req] = it;it })
        }
    }
}

private fun <T> instantTickedTask(t: T): TickedTask<Unit, T> = InstantTickedTask(t)

private class InstantTickedTask<T>(val t: T) : TickedTask<Unit, T>() {
    override fun runnable(input: Unit): T {
        throw UnsupportedOperationException()
    }

    override fun run(input: Unit) {
        nextNode?.run(t)
    }

}