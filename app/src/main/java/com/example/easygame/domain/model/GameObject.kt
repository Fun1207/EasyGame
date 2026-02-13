package com.example.easygame.domain.model

data class GameObject(
    val x: Float,
    var y: Float,
    var gameObjectType: GameObjectType = GameObjectType.APPLE
)

enum class GameObjectType { APPLE, BOMB, COIN }
