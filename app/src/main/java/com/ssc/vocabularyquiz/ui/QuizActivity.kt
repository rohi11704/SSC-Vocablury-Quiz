package com.ssc.vocabularyquiz.ui

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.ssc.vocabularyquiz.R
import com.ssc.vocabularyquiz.data.model.QuizCategory
import com.ssc.vocabularyquiz.data.repository.QuizRepository
import com.ssc.vocabularyquiz.databinding.ActivityQuizBinding
import com.ssc.vocabularyquiz.ui.viewmodel.QuizViewModel
import com.ssc.vocabularyquiz.ui.viewmodel.QuizViewModelFactory

class QuizActivity : AppCompatActivity() {
    
    companion object {
        const val EXTRA_CATEGORY = "extra_category"
    }
    
    private lateinit var binding: ActivityQuizBinding
    private lateinit var category: QuizCategory
    
    private val viewModel: QuizViewModel by viewModels {
        QuizViewModelFactory(QuizRepository(this))
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setupCategory()
        setupObservers()
        setupClickListeners()
        
        viewModel.loadQuestions(category)
    }
    
    private fun setupCategory() {
        val categoryName = intent.getStringExtra(EXTRA_CATEGORY) ?: return
        category = QuizCategory.valueOf(categoryName)
        
        supportActionBar?.title = category.displayName
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
    
    private fun setupObservers() {
        viewModel.questions.observe(this) { questions ->
            if (questions.isNotEmpty()) {
                updateUI()
            }
        }
        
        viewModel.currentQuestionIndex.observe(this) {
            updateUI()
        }
        
        viewModel.correctAnswers.observe(this) { correct ->
            binding.tvCorrectCount.text = getString(R.string.correct_answers, correct)
        }
        
        viewModel.incorrectAnswers.observe(this) { incorrect ->
            binding.tvIncorrectCount.text = getString(R.string.incorrect_answers, incorrect)
        }
        
        viewModel.selectedAnswerIndex.observe(this) { selectedIndex ->
            updateOptionButtons(selectedIndex)
        }
        
        viewModel.showResult.observe(this) { showResult ->
            if (showResult) {
                showAnswerResult()
                binding.btnNext.visibility = View.VISIBLE
            } else {
                hideAnswerResult()
                binding.btnNext.visibility = View.GONE
            }
        }
        
        viewModel.isQuizCompleted.observe(this) { completed ->
            if (completed) {
                showQuizCompleted()
            }
        }
        
        viewModel.loading.observe(this) { loading ->
            binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
            binding.layoutQuiz.visibility = if (loading) View.GONE else View.VISIBLE
        }
    }
    
    private fun setupClickListeners() {
        binding.btnOption1.setOnClickListener { viewModel.selectAnswer(0) }
        binding.btnOption2.setOnClickListener { viewModel.selectAnswer(1) }
        binding.btnOption3.setOnClickListener { viewModel.selectAnswer(2) }
        binding.btnOption4.setOnClickListener { viewModel.selectAnswer(3) }
        
        binding.btnSubmit.setOnClickListener {
            viewModel.submitAnswer()
        }
        
        binding.btnNext.setOnClickListener {
            viewModel.nextQuestion()
        }
        
        binding.btnTryAgain.setOnClickListener {
            viewModel.resetQuiz()
            binding.layoutQuizCompleted.visibility = View.GONE
            binding.layoutQuiz.visibility = View.VISIBLE
        }
        
        binding.btnBackToHome.setOnClickListener {
            finish()
        }
    }
    
    private fun updateUI() {
        val question = viewModel.getCurrentQuestion() ?: return
        val currentIndex = viewModel.currentQuestionIndex.value ?: 0
        val totalQuestions = viewModel.getTotalQuestions()
        
        binding.tvQuestionNumber.text = "Question ${currentIndex + 1} of $totalQuestions"
        binding.tvQuestion.text = question.question
        
        val options = question.options
        binding.btnOption1.text = options.getOrNull(0) ?: ""
        binding.btnOption2.text = options.getOrNull(1) ?: ""
        binding.btnOption3.text = options.getOrNull(2) ?: ""
        binding.btnOption4.text = options.getOrNull(3) ?: ""
        
        // Reset button states
        resetOptionButtons()
    }
    
    private fun updateOptionButtons(selectedIndex: Int?) {
        resetOptionButtons()
        
        selectedIndex?.let { index ->
            val selectedButton = when (index) {
                0 -> binding.btnOption1
                1 -> binding.btnOption2
                2 -> binding.btnOption3
                3 -> binding.btnOption4
                else -> null
            }
            selectedButton?.setBackgroundColor(ContextCompat.getColor(this, R.color.primary_blue))
        }
    }
    
    private fun resetOptionButtons() {
        val buttons = listOf(
            binding.btnOption1,
            binding.btnOption2,
            binding.btnOption3,
            binding.btnOption4
        )
        
        buttons.forEach { button ->
            button.setBackgroundColor(ContextCompat.getColor(this, R.color.light_grey))
            button.isEnabled = true
        }
    }
    
    private fun showAnswerResult() {
        val question = viewModel.getCurrentQuestion() ?: return
        val selectedIndex = viewModel.selectedAnswerIndex.value ?: return
        val correctIndex = question.answerIndex
        
        val buttons = listOf(
            binding.btnOption1,
            binding.btnOption2,
            binding.btnOption3,
            binding.btnOption4
        )
        
        // Disable all buttons
        buttons.forEach { it.isEnabled = false }
        
        // Show correct answer in green
        buttons[correctIndex].setBackgroundColor(ContextCompat.getColor(this, R.color.correct_green))
        
        // Show incorrect answer in red if different from correct
        if (selectedIndex != correctIndex) {
            buttons[selectedIndex].setBackgroundColor(ContextCompat.getColor(this, R.color.incorrect_red))
        }
    }
    
    private fun hideAnswerResult() {
        resetOptionButtons()
    }
    
    private fun showQuizCompleted() {
        val correct = viewModel.correctAnswers.value ?: 0
        val total = viewModel.getTotalQuestions()
        
        binding.layoutQuiz.visibility = View.GONE
        binding.layoutQuizCompleted.visibility = View.VISIBLE
        binding.tvFinalScore.text = getString(R.string.your_score, correct, total)
    }
    
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}