package com.phinion.gcepluselearning.models

data class GroupChat(
    var senderId: String? = null,
    var senderName: String? = null,
    var message: String? = null,
    var timestamp: Long = 0,
    val isSent: Boolean? = null,
    val members: List<String> = emptyList()
) {

    constructor() : this("", "", "", 0, null, emptyList())

}

