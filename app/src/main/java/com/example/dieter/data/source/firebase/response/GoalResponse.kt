package com.example.dieter.data.source.firebase.response

import com.example.dieter.data.source.domain.GoalModel
import com.example.dieter.data.source.domain.GoalType
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class GoalResponse(
    var target: GoalType? = null,
    var age: Int? = null,
    var isMale: Boolean? = null,
    var height: Int? = null,
    var weight: Int? = null,
    var targetWeight: Int? = null,
    var addedAt: Long? = null
)

// TODO: Remove all bang operators
fun GoalResponse.asDomainModel() = GoalModel(target!!, age!!, isMale!!, height!!, weight!!, targetWeight!!, addedAt!!)
