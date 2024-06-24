package com.example.fetch.domain

import com.example.fetch.data.UsersRepository
import com.example.fetch.data.model.User

class GetUsersNearMe {

    private var repository = UsersRepository()

    suspend operator fun invoke(latitude: Double, longitude: Double):List<User>{
        repository = UsersRepository()
        return repository.getUsersNearMe(latitude, longitude)
    }
}