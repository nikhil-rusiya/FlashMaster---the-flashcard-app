package com.example.flashmaster

class User {
    var question: String? = null
    var description: String? = null
    var videoLink: String? = null
    var answer: String? = null
    var backdescription: String? = null
    var id: String? = null

    constructor() {}
    constructor(
        question: String?,
        description: String?,
        videoLink: String?,
        answer: String?,
        backdescription: String?,
        id: String?
    ) {
        this.question = question
        this.description = description
        this.videoLink = videoLink
        this.answer = answer
        this.backdescription = backdescription
        this.id = id
    }
}