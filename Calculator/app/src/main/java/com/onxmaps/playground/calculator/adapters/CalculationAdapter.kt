package com.onxmaps.playground.calculator.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.onxmaps.playground.calculator.R
import com.onxmaps.playground.calculator.models.Calculation
import com.onxmaps.playground.calculator.viewmodels.MainActivityViewModel
import com.onxmaps.playground.calculator.viewmodels.MainActivityViewModel.Companion.OPERATION_TYPE_ADDITION
import com.onxmaps.playground.calculator.viewmodels.MainActivityViewModel.Companion.OPERATION_TYPE_DIVISION
import com.onxmaps.playground.calculator.viewmodels.MainActivityViewModel.Companion.OPERATION_TYPE_MODULUS
import com.onxmaps.playground.calculator.viewmodels.MainActivityViewModel.Companion.OPERATION_TYPE_MULTIPLICATION
import com.onxmaps.playground.calculator.viewmodels.MainActivityViewModel.Companion.OPERATION_TYPE_SUBTRACTION

class CalculationAdapter(
    private var calculations: List<Calculation>,
    private val context: Context
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface CalculateEventListener {
        fun onDeleteCalculation(calculation: Calculation)
    }

    private var listener: CalculateEventListener? = null

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {

        super.onAttachedToRecyclerView(recyclerView)

        try {
            listener = context as CalculateEventListener
        } catch (e: ClassCastException) {
            throw ClassCastException("Must implement OnEventListener")
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.calculated_row, parent, false)

        return CalculationHolder(view)

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        return bindCalculationHolder(holder as CalculationHolder, position)
    }

    override fun getItemCount(): Int {
        return calculations.size
    }

    fun update(calculations: List<Calculation>) {
        this.calculations = calculations
        notifyDataSetChanged()
    }

    private fun bindCalculationHolder(holder: CalculationHolder, position: Int) {
        val calculation = calculations[position]

        val symbol = when (calculation.operationType){
            OPERATION_TYPE_ADDITION -> "+"
            OPERATION_TYPE_SUBTRACTION -> "-"
            OPERATION_TYPE_MULTIPLICATION -> "x"
            OPERATION_TYPE_DIVISION -> "÷"
            else -> "%" // mod
        }

        "${calculation.firstNumber} $symbol ${calculation.lastNumber} = ${calculation.answer}".also { holder.calculationText.text = it }

        holder.imgDelete.setOnClickListener {
            listener?.onDeleteCalculation(calculation)
        }
    }

    private class CalculationHolder(view: View) : RecyclerView.ViewHolder(view) {

        val calculationText: TextView = view.findViewById(R.id.textCalculation)
        val imgDelete: ImageView = view.findViewById(R.id.imgDelete)

    }
}