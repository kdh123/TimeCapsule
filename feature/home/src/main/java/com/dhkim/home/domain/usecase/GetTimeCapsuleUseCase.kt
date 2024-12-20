package com.dhkim.home.domain.usecase

import com.dhkim.common.Dispatcher
import com.dhkim.common.TimeCapsuleDispatchers
import com.dhkim.home.domain.model.TimeCapsule
import com.dhkim.home.domain.repository.TimeCapsuleRepository
import com.dhkim.user.repository.UserRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetTimeCapsuleUseCase @Inject constructor(
    private val timeCapsuleRepository: TimeCapsuleRepository,
    private val userRepository: UserRepository,
    @Dispatcher(TimeCapsuleDispatchers.IO) private val ioDispatcher: CoroutineDispatcher
) {

    operator fun invoke(timeCapsuleId: String, isReceived: Boolean): Flow<TimeCapsule> {
        return flow {
            val myId = userRepository.getMyId()
            val myProfileImage = "${userRepository.getProfileImage()}"
            if (isReceived) {
                timeCapsuleRepository.getReceivedTimeCapsule(id = timeCapsuleId)?.let {
                    timeCapsuleRepository.updateReceivedTimeCapsule(it.copy(isOpened = true))
                    val nickname = userRepository.getFriend(it.sender)?.nickname ?: it.sender
                    emit(it.toTimeCapsule(nickname = nickname))
                }
            } else {
                timeCapsuleRepository.getMyTimeCapsule(id = timeCapsuleId)?.let {
                    timeCapsuleRepository.editMyTimeCapsule(it.copy(isOpened = true))
                    val sharedFriends = it.sharedFriends.map { userId ->
                        userRepository.getFriend(userId)?.nickname ?: userId
                    }
                    emit(it.toTimeCapsule(myId, myProfileImage, sharedFriends))
                }
            }
        }.flowOn(ioDispatcher)
    }
}