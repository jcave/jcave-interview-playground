package com.onxmaps.playground.calculator

import android.os.Bundle
import android.util.ArrayMap
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.onxmaps.playground.calculator.adapters.CalculationAdapter
import com.onxmaps.playground.calculator.databinding.ActivityMainBinding
import com.onxmaps.playground.calculator.models.Calculation
import com.onxmaps.playground.calculator.viewmodels.MainActivityViewModel
import com.onxmaps.playground.calculator.viewmodels.MainActivityViewModel.Companion.OPERATION_TYPE_ADDITION
import com.onxmaps.playground.calculator.viewmodels.MainActivityViewModel.Companion.OPERATION_TYPE_DIVISION
import com.onxmaps.playground.calculator.viewmodels.MainActivityViewModel.Companion.OPERATION_TYPE_MODULUS
import com.onxmaps.playground.calculator.viewmodels.MainActivityViewModel.Companion.OPERATION_TYPE_MULTIPLICATION
import com.onxmaps.playground.calculator.viewmodels.MainActivityViewModel.Companion.OPERATION_TYPE_SUBTRACTION
import timber.log.Timber

class MainActivity : AppCompatActivity(), CalculationAdapter.CalculateEventListener {

    /**
     * Instructions: Create a calculator that can perform 5 operations:
     * addition, subtraction, multiplication, division, and find the
     * remainder (modulus). The calculator should only be able to
     * work with 2 real numbers. You will need to display the operation
     * that is currently selected, as well as display a solution and
     * find a way to clear your result and start over. If you have
     * extra time, keep and display a history of all operations done.
     */

    private lateinit var binding: ActivityMainBinding
    private lateinit var recyclerView: RecyclerView
    private val operationDrawableMap: ArrayMap<Int, Int> = ArrayMap()
    private val operationIdMap: ArrayMap<Int, Int> = ArrayMap()
    private lateinit var activityViewModel: MainActivityViewModel
    private lateinit var calculatorAdapter: CalculationAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.plant(Timber.DebugTree())

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        activityViewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)
        activityViewModel.calculationsLiveData.observe(this, {
            if (it != null) {
                updateView(it)
            }
        })

        setupOperationsMaps()

        val layoutManager = LinearLayoutManager(this)
        calculatorAdapter = CalculationAdapter(emptyList(), this)

        recyclerView = binding.includeContentMain.recyclerView
        recyclerView.adapter = calculatorAdapter
        recyclerView.layoutManager = layoutManager

        binding.fab.setOnClickListener { _ ->
            saveCalculation()
        }
    }

    private fun updateView(calculations: List<Calculation>): List<Calculation> {
        calculatorAdapter.update(calculations)
        return calculations
    }

    private fun saveCalculation() {
        val firstNum = binding.includeContentMain.txtFirstNumber.text.toString()
        val secondNum = binding.includeContentMain.txtSecondNumber.text.toString()

        val newCalculation = Calculation().apply {
            firstNumber = firstNum.toFloat()
            lastNumber = secondNum.toFloat()
            operationType = activityViewModel.selectedOperation
            Timber.i("Calculation type: ${activityViewModel.selectedOperation}")
        }

        activityViewModel.addCalculation(newCalculation)
    }


    private fun setupOperationsMaps() {
        operationDrawableMap[R.id.itemAddition] = R.drawable.ic_add
        operationDrawableMap[R.id.itemDivision] = R.drawable.ic_divide
        operationDrawableMap[R.id.itemMultiplication] = R.drawable.ic_multiply
        operationDrawableMap[R.id.itemSubtraction] = R.drawable.ic_subtract
        operationDrawableMap[R.id.itemModulus] = R.drawable.ic_mod

        operationIdMap[R.id.itemAddition] = OPERATION_TYPE_ADDITION
        operationIdMap[R.id.itemDivision] = OPERATION_TYPE_DIVISION
        operationIdMap[R.id.itemMultiplication] = OPERATION_TYPE_MULTIPLICATION
        operationIdMap[R.id.itemSubtraction] = OPERATION_TYPE_SUBTRACTION
        operationIdMap[R.id.itemModulus] = OPERATION_TYPE_MODULUS

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.

        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    private fun updateOperationType(selectedId: Int) {

        operationDrawableMap[selectedId]?.let { imgId ->
            binding.includeContentMain.imgOperation.setImageResource(imgId)
        }

        activityViewModel.selectedOperation = operationIdMap[selectedId] ?: OPERATION_TYPE_ADDITION
        Timber.i("selected operation: ${activityViewModel.selectedOperation}")

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        updateOperationType(item.itemId)
        return true
    }

    override fun onDeleteCalculation(calculation: Calculation) {
        activityViewModel.deleteCalculation(calculation)
    }

}
