package com.ssc.vocabularyquiz.data.model

data class Question(
    val question: String,
    val options: List<String>,
    val answerIndex: Int
)