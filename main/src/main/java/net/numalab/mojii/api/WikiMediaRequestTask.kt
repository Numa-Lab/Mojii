package net.numalab.mojii.api

import com.github.bun133.tinked.TickedTask
import com.github.kittinunf.fuel.gson.responseObject
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result

class WikiMediaRequestTask(val req:WikiMediaRequest) : TickedTask<Unit, WikiMediaResponse>() {
    override fun runnable(input: Unit): WikiMediaResponse {
        throw UnsupportedOperationException("Not supported yet.")
    }

    override fun run(input: Unit) {
        val r = req
            .url()
            .httpGet()
            .responseObject<WikiMediaResponse> { _, _, result ->
                when (result) {
                    is Result.Success -> {
                        nextNode?.run(result.value)
                    }
                    is Result.Failure -> {
                        throw result.getException()
                    }
                }
            }
    }
}