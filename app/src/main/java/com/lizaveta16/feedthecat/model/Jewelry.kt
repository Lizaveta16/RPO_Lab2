package com.lizaveta16.feedthecat.model

class Jewelry() {

    var pid: String = ""
    var name: String = ""
    var image: String = ""
    var price: Double = 0.0
    var size: Double = 0.0
    var category: String = ""
    var material: String = ""
    var forWhom: String = ""
    var insert: String = ""
    var sample: String = ""
    var brand: String = ""
    var video: String = ""

    constructor(
        pid: String,
        name: String,
        image: String,
        price: Double,
        size: Double,
        category: String,
        material: String,
        forWhom : String,
        insert: String,
        sample: String,
        brand: String,
        video: String
    ) : this() {
        this.pid = pid
        this.name = name
        this.image = image
        this.price = price
        this.size = size
        this.category = category
        this.material = material
        this.forWhom = forWhom
        this.insert = insert
        this.sample = sample
        this.brand = brand
        this.video = video
    }

    fun toMap(): Map<String, Any> {
        return mapOf(
            "pid" to pid,
            "name" to name,
            "image" to image,
            "price" to price,
            "size" to size,
            "category" to category,
            "material" to material,
            "forWhom" to forWhom,
            "insert" to insert,
            "sample" to sample,
            "brand" to brand,
            "video" to video,
        )
    }
}