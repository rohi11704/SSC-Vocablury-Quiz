package com.ssc.vocabularyquiz.data.model

enum class QuizCategory(val displayName: String, val fileName: String) {
    ONE_WORD_SUBSTITUTION("One-Word Substitution", "one_word_substitution.json"),
    IDIOMS_PHRASES("Idioms & Phrases", "idioms_phrases.json"),
    SYNONYMS("Synonyms", "synonyms.json"),
    ANTONYMS("Antonyms", "antonyms.json")
}