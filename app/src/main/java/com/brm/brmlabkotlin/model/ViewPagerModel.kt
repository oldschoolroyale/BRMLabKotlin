package com.brm.brmlabkotlin.model

class ViewPagerModel {
    var image: String? = null
    var title: String? = null
    var id: Int? = null

    constructor()
    constructor(image: String?, title: String?, id: Int?) {
        this.image = image
        this.title = title
        this.id = id
    }


}
