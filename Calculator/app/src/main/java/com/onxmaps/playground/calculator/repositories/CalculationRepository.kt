package com.onxmaps.playground.calculator.repositories

import androidx.lifecycle.LiveData
import com.onxmaps.playground.calculator.dao.CalculationDao
import com.onxmaps.playground.calculator.models.Calculation

class CalculationRepository(private val calculationDao: CalculationDao) {

    val getCalculations: LiveData<List<Calculation>> = calculationDao.getCalculations()

    suspend fun addCalculation(calculation: Calculation) {
        calculationDao.addCalculation(calculation)
    }

    suspend fun deleteCalculation(calculation: Calculation) {
        calculationDao.deleteCalculation(calculation)
    }

}