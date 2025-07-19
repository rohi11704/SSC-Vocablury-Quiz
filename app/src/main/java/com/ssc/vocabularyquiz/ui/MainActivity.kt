package com.ssc.vocabularyquiz.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.ssc.vocabularyquiz.data.model.QuizCategory
import com.ssc.vocabularyquiz.databinding.ActivityMainBinding
import com.ssc.vocabularyquiz.ui.adapter.CategoryAdapter

class MainActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityMainBinding
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setupRecyclerView()
    }
    
    private fun setupRecyclerView() {
        val categories = QuizCategory.values().toList()
        val adapter = CategoryAdapter(categories) { category ->
            startQuizActivity(category)
        }
        
        binding.rvCategories.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            this.adapter = adapter
        }
    }
    
    private fun startQuizActivity(category: QuizCategory) {
        val intent = Intent(this, QuizActivity::class.java).apply {
            putExtra(QuizActivity.EXTRA_CATEGORY, category.name)
        }
        startActivity(intent)
    }
}