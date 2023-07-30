package com.pankaj.askgpt_ai

class Message {
    var SENT_BY_ME = "me"
    var SENT_BY_BOT = "bot"
    var message: String? = null
    var sentBy: String? = null
    var image: Boolean? = null
    fun getmessage(): String? {
        return message
    }

    fun setimage(image: Boolean?){
        this.image = image
    }

    fun getimage(): Boolean? {
        return image
    }
    fun setmessage(message: String?) {
        this.message = message
    }

    fun getsentby(): String? {
        return sentBy
    }

    fun setsentby(sentBy: String?) {
        this.sentBy = sentBy
    }
    constructor(message: String?, sentBy: String?, image: Boolean?) {
        this.message = message
        this.sentBy = sentBy
        this.image = image
    }
}