package com.example.dieter.data.source.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "nutrient_entity")
class NutrientEntity(
    @PrimaryKey
    val id: Int
)
