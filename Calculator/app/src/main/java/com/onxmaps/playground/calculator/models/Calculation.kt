package com.onxmaps.playground.calculator.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "calculation")
data class Calculation(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var firstNumber: Float = 0.0F,
    var lastNumber: Float = 0.0F,
    var operationType: Int = 0,
    var answer: Float = 0.0F
)