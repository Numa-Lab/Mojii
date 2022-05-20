package net.numalab.mojii.lang


interface LangSet {
    /**
     * 言語名
     */
    fun langName(): String

    /**
     * 言語コード
     */
    fun langCode():String
    fun chars(): CharSet
}