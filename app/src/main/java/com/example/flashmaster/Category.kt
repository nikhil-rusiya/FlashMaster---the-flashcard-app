package com.example.flashmaster

class Category {
    var email: String? = null
    var category: String? = null
    var id: String? = null

    constructor() {}
    constructor(email: String?, category: String?, id: String?) {
        this.email = email
        this.category = category
        this.id = id
    }
}