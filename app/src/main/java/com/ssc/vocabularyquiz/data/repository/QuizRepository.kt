package com.ssc.vocabularyquiz.data.repository

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ssc.vocabularyquiz.data.model.Question
import com.ssc.vocabularyquiz.data.model.QuizCategory
import com.tom_roush.pdfbox.android.PDFBoxResourceLoader
import com.tom_roush.pdfbox.pdmodel.PDDocument
import com.tom_roush.pdfbox.text.PDFTextStripper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException

class QuizRepository(private val context: Context) {
    
    private val gson = Gson()
    
    init {
        // Initialize PDFBox
        PDFBoxResourceLoader.init(context)
    }
    
    suspend fun getQuestions(category: QuizCategory): List<Question> = withContext(Dispatchers.IO) {
        return@withContext try {
            if (category.fileName.endsWith(".json")) {
                loadQuestionsFromJson(category.fileName)
            } else if (category.fileName.endsWith(".pdf")) {
                loadQuestionsFromPdf(category.fileName)
            } else {
                // Default to JSON if extension is unclear
                loadQuestionsFromJson(category.fileName)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
    
    private fun loadQuestionsFromJson(fileName: String): List<Question> {
        return try {
            val jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
            val type = object : TypeToken<List<Question>>() {}.type
            gson.fromJson(jsonString, type) ?: emptyList()
        } catch (e: IOException) {
            e.printStackTrace()
            emptyList()
        }
    }
    
    private fun loadQuestionsFromPdf(fileName: String): List<Question> {
        return try {
            val inputStream = context.assets.open(fileName)
            val document = PDDocument.load(inputStream)
            val pdfStripper = PDFTextStripper()
            val text = pdfStripper.getText(document)
            document.close()
            inputStream.close()
            
            // Parse PDF text to extract questions
            // This is a simplified parser - you may need to adjust based on your PDF format
            parsePdfTextToQuestions(text)
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
    
    private fun parsePdfTextToQuestions(text: String): List<Question> {
        val questions = mutableListOf<Question>()
        val lines = text.split("\n").filter { it.trim().isNotEmpty() }
        
        var i = 0
        while (i < lines.size) {
            try {
                // Look for question pattern (this is a basic implementation)
                val questionLine = lines[i].trim()
                if (questionLine.isNotEmpty() && i + 4 < lines.size) {
                    val options = mutableListOf<String>()
                    var answerIndex = 0
                    
                    // Get next 4 lines as options
                    for (j in 1..4) {
                        if (i + j < lines.size) {
                            val option = lines[i + j].trim()
                            options.add(option)
                            
                            // Simple way to identify correct answer (marked with *)
                            if (option.contains("*") || option.startsWith("✓")) {
                                answerIndex = j - 1
                            }
                        }
                    }
                    
                    if (options.size == 4) {
                        questions.add(Question(questionLine, options, answerIndex))
                        i += 5 // Move to next question
                    } else {
                        i++
                    }
                } else {
                    i++
                }
            } catch (e: Exception) {
                i++
            }
        }
        
        return questions
    }
}