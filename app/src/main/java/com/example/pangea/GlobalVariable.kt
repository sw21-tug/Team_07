package com.example.pangea

import android.app.Application

class GlobalVariable : Application(){
    companion object{
        var matchedPosts : List<Post> = emptyList()
    }
}