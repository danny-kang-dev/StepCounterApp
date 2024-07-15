package com.example.myapplication.data.repository

import com.example.myapplication.data.room.StepDao
import com.example.myapplication.data.room.StepEntity
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class StepRepository @Inject constructor(
    private val dao: StepDao
){
    suspend fun insertStep(stepEntity: StepEntity) {
        withContext(IO) {
            dao.insert(stepEntity)
        }
    }

    suspend fun updateStep(stepEntity: StepEntity) {
        withContext(IO) {
            dao.update(stepEntity)
        }
    }

    suspend fun deleteStep(stepEntity: StepEntity) {
        withContext(IO) {
            dao.delete(stepEntity)
        }
    }

    suspend fun getStep(date: String): Flow<StepEntity> {
        return withContext(IO) {
            dao.getStepByDate(date)
        }
    }
}