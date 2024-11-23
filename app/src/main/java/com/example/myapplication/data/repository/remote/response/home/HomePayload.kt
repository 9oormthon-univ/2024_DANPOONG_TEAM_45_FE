package com.example.myapplication.data.repository.remote.response.home

import com.example.myapplication.data.repository.remote.request.character.CharacterDTO

data class HomePayload(
    val id : Int = 0,
    val character : DistinctCharacterResponse = DistinctCharacterResponse(),
    val userPicture : String = ""
)
