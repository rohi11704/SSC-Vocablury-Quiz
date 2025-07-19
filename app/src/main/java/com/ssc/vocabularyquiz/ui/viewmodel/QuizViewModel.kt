package com.ssc.vocabularyquiz.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssc.vocabularyquiz.data.model.Question
import com.ssc.vocabularyquiz.data.model.QuizCategory
import com.ssc.vocabularyquiz.data.repository.QuizRepository
import kotlinx.coroutines.launch

class QuizViewModel(private val repository: QuizRepository) : ViewModel() {
    
    private val _questions = MutableLiveData<List<Question>>()
    val questions: LiveData<List<Question>> = _questions
    
    private val _currentQuestionIndex = MutableLiveData<Int>()
    val currentQuestionIndex: LiveData<Int> = _currentQuestionIndex
    
    private val _correctAnswers = MutableLiveData<Int>()
    val correctAnswers: LiveData<Int> = _correctAnswers
    
    private val _incorrectAnswers = MutableLiveData<Int>()
    val incorrectAnswers: LiveData<Int> = _incorrectAnswers
    
    private val _isQuizCompleted = MutableLiveData<Boolean>()
    val isQuizCompleted: LiveData<Boolean> = _isQuizCompleted
    
    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading
    
    private val _selectedAnswerIndex = MutableLiveData<Int?>()
    val selectedAnswerIndex: LiveData<Int?> = _selectedAnswerIndex
    
    private val _showResult = MutableLiveData<Boolean>()
    val showResult: LiveData<Boolean> = _showResult
    
    init {
        _currentQuestionIndex.value = 0
        _correctAnswers.value = 0
        _incorrectAnswers.value = 0
        _isQuizCompleted.value = false
        _selectedAnswerIndex.value = null
        _showResult.value = false
    }
    
    fun loadQuestions(category: QuizCategory) {
        _loading.value = true
        viewModelScope.launch {
            try {
                val questionsList = repository.getQuestions(category)
                _questions.value = questionsList
                _loading.value = false
            } catch (e: Exception) {
                _loading.value = false
                _questions.value = emptyList()
            }
        }
    }
    
    fun selectAnswer(answerIndex: Int) {
        _selectedAnswerIndex.value = answerIndex
    }
    
    fun submitAnswer() {
        val selectedIndex = _selectedAnswerIndex.value ?: return
        val currentQuestion = getCurrentQuestion() ?: return
        
        if (selectedIndex == currentQuestion.answerIndex) {
            _correctAnswers.value = (_correctAnswers.value ?: 0) + 1
        } else {
            _incorrectAnswers.value = (_incorrectAnswers.value ?: 0) + 1
        }
        
        _showResult.value = true
    }
    
    fun nextQuestion() {
        val currentIndex = _currentQuestionIndex.value ?: 0
        val totalQuestions = _questions.value?.size ?: 0
        
        if (currentIndex + 1 < totalQuestions) {
            _currentQuestionIndex.value = currentIndex + 1
            _selectedAnswerIndex.value = null
            _showResult.value = false
        } else {
            _isQuizCompleted.value = true
        }
    }
    
    fun getCurrentQuestion(): Question? {
        val questions = _questions.value ?: return null
        val index = _currentQuestionIndex.value ?: return null
        return if (index < questions.size) questions[index] else null
    }
    
    fun getTotalQuestions(): Int {
        return _questions.value?.size ?: 0
    }
    
    fun resetQuiz() {
        _currentQuestionIndex.value = 0
        _correctAnswers.value = 0
        _incorrectAnswers.value = 0
        _isQuizCompleted.value = false
        _selectedAnswerIndex.value = null
        _showResult.value = false
    }
}