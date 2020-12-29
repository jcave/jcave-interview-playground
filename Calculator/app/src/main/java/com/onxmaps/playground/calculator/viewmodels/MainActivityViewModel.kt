package com.onxmaps.playground.calculator.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.onxmaps.playground.calculator.data.PlaygroundDb
import com.onxmaps.playground.calculator.models.Calculation
import com.onxmaps.playground.calculator.repositories.CalculationRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivityViewModel(application: Application) : AndroidViewModel(application) {

    var selectedOperation: Int = 0
    val calculationsLiveData: LiveData<List<Calculation>>
    private val repository: CalculationRepository

    init {

        val calculationsDao = PlaygroundDb.getDatabase(application).calculationDao()
        repository = CalculationRepository(calculationsDao)
        calculationsLiveData = repository.getCalculations

    }

    fun addCalculation(calculation: Calculation) {
        viewModelScope.launch(Dispatchers.IO) {
            calculation.answer = calculateAnswer(calculation)
            repository.addCalculation(calculation)
        }
    }

    fun deleteCalculation(calculation: Calculation) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteCalculation(calculation)
        }
    }

    private fun calculateAnswer(calculation: Calculation): Float {
        return when (calculation.operationType) {
            OPERATION_TYPE_ADDITION -> calculation.firstNumber + calculation.lastNumber
            OPERATION_TYPE_SUBTRACTION -> calculation.firstNumber - calculation.lastNumber
            OPERATION_TYPE_MULTIPLICATION -> calculation.firstNumber * calculation.lastNumber
            OPERATION_TYPE_DIVISION -> calculation.firstNumber / calculation.lastNumber
            OPERATION_TYPE_MODULUS -> calculation.firstNumber % calculation.lastNumber
            else -> 0.0F
        }
    }

    companion object {
        const val OPERATION_TYPE_ADDITION = 0
        const val OPERATION_TYPE_SUBTRACTION = 1
        const val OPERATION_TYPE_MULTIPLICATION = 2
        const val OPERATION_TYPE_DIVISION = 3
        const val OPERATION_TYPE_MODULUS = 4
    }
}