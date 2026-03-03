package com.example.easygame.domain.util

import com.example.easygame.R
import com.example.easygame.domain.model.GameObject
import com.example.easygame.domain.model.GameObjectType

const val GAME_DATABASE = "game_database"
const val GAME_DATA_STORE = "game_data_store"
const val DEFAULT_BASKET_ID = "local_basket"
const val DEFAULT_BASKET_NAME = "Default Basket"
val DEFAULT_BASKET = GameObject(
    id = DEFAULT_BASKET_ID,
    name = DEFAULT_BASKET_NAME,
    source = R.drawable.icon_basket,
    isPurchased = true,
    type = GameObjectType.BASKET
)
