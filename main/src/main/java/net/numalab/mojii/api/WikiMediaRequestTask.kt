package net.numalab.mojii.api

import com.github.bun133.tinked.TickedTask
import com.github.kittinunf.fuel.gson.responseObject
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result

class WikiMediaRequestTask : TickedTask<WikiMediaRequest, WikiMediaResponse>() {
    override fun runnable(input: WikiMediaRequest): WikiMediaResponse {
        throw UnsupportedOperationException("Not supported yet.")
    }

    override fun run(input: WikiMediaRequest) {
        val r = input
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