package com.romeo.eatmeapp.data.model

import com.romeo.eatmeapp.data.repository.MainMenuRepository

data class MainMenuModel(
    val id: Int = 0,
    val name: String = "",
    val imageUri: String = ""
): MainMenuRepository {
    override suspend fun getMainMenu(): List<MainMenuModel> {

        return List(3) { index ->
            MainMenuModel(
                id  = index,
                name = "Button#${index + 1}",
                imageUri = ""
            )
        }
    }
}
