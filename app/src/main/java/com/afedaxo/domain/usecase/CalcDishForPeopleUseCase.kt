package com.afedaxo.domain.usecase

import android.graphics.Bitmap
import com.afedaxo.domain.Either
import com.afedaxo.domain.Failure
import com.afedaxo.domain.Success
import com.afedaxo.domain.model.CalculationParams
import com.afedaxo.model.repository.FilesRepository
import com.afedaxo.model.repository.SessionsRepository
import com.afedaxo.processor.CompositeWeightProcessor

class CalcDishForPeopleUseCase(val filesRepository: FilesRepository,
                               val sessionsRepository: SessionsRepository): UseCase<List<Pair<Int, Bitmap>>, CalculationParams>() {

    override suspend fun run(param: CalculationParams): Either<Exception, List<Pair<Int, Bitmap>>> {
        return try {
            val dishes = sessionsRepository.getAllDishesForSessionId(param.sessionId)

            val preproc = CompositeWeightProcessor.preprocessValues(param.numPeople, dishes)

            val compositeWeightProcessor = CompositeWeightProcessor(preproc)

            if (param.priceMode == 0) {
                val resultingDish = compositeWeightProcessor.getWeightedRandom()
                if (resultingDish != null) {
                    Success(resultingDish.map {
                        Pair(it.first, filesRepository.getBitmapOfFile(it.second.croppedFilename))
                    })
                }
            } else if (param.priceMode == 1) {
                val fullyRandom = preproc.random()
                val weightedRandom = compositeWeightProcessor.getWeightedRandom()
                val resultingDish = listOf(fullyRandom, weightedRandom).random()
                if (resultingDish != null) {
                    Success(resultingDish.map {
                        Pair(it.first, filesRepository.getBitmapOfFile(it.second.croppedFilename))
                    })
                }
            } else {
                val fullyRandom = preproc.random()
                Success(fullyRandom.map {
                    Pair(it.first, filesRepository.getBitmapOfFile(it.second.croppedFilename))
                })
            }

            Failure(java.lang.Exception("CalcDishForPeopleUseCase error!"))
        }
        catch (e: java.lang.Exception) {
            Failure(e)
        }
    }
}