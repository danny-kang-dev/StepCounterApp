package com.example.myapplication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.repository.StepRepository
import com.example.myapplication.data.room.StepEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: StepRepository
) : ViewModel() {

    private val _currentStepFlow: MutableStateFlow<StepEntity?> = MutableStateFlow(null)
    val currentStepFlow: StateFlow<StepEntity?> = _currentStepFlow

    fun updateSteps(date: String, steps: Long) {
        viewModelScope.launch {
            repository.updateStep(
                StepEntity(
                    date = date,
                    steps = steps
                )
            )
        }
    }

    fun getStepByDate(date: String) {
        viewModelScope.launch {
            repository.getStep(date).collect { step ->
                _currentStepFlow.emit(step)
            }
        }
    }
}