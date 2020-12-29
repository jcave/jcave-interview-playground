package com.onxmaps.playground.calculator.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.onxmaps.playground.calculator.models.Calculation

@Dao
interface CalculationDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addCalculation(calculation: Calculation)

    @Query("SELECT * FROM calculation")
    fun getCalculations(): LiveData<List<Calculation>>

    @Delete
    suspend fun deleteCalculation(calculation: Calculation)

}