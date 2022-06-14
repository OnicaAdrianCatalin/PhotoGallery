package com.example.photogallery.utils

object Constants {
    const val BASE_URL = "https://api.flickr.com/"
    const val API_URL =
        "/services/rest/?method=flickr." +
            "interestingness." +
            "getList&api_key=c920fffa449a70927da9fadccd1d27e0&format=json&nojsoncallback=1&extras=url_s"
}
