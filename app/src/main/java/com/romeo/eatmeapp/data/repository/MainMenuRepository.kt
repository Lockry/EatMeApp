package com.romeo.eatmeapp.data.repository

import com.romeo.eatmeapp.data.model.MainMenuModel

interface MainMenuRepository {
    suspend fun getMainMenu(): List<MainMenuModel>

}

class MainMenuRepositoryImpl : MainMenuRepository {
    override suspend fun getMainMenu(): List<MainMenuModel> {
        return List(3) { index ->
            MainMenuModel(
                id = index,
                name = "Button#${index + 1}",
                imageUri = "" // пока без картинок
            )
        }
    }
}