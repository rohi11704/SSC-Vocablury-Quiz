# SSC Vocabulary Quiz App

A comprehensive Android quiz application designed for SSC (Staff Selection Commission) vocabulary preparation, featuring four main categories: One-Word Substitution, Idioms & Phrases, Synonyms, and Antonyms.

## Features

- **Four Quiz Categories**: One-Word Substitution, Idioms & Phrases, Synonyms, Antonyms
- **Clean UI**: White background with light grey option cards and black text
- **Real-time Scoring**: Running counts of correct and incorrect answers
- **MVVM Architecture**: Proper separation of concerns with ViewModel, Repository, and LiveData
- **Flexible Data Sources**: Supports both JSON and PDF file formats
- **Interactive Quiz Experience**: One question at a time with immediate feedback

## Architecture

The app follows MVVM (Model-View-ViewModel) architecture pattern:

- **Model**: `Question` and `QuizCategory` data classes
- **View**: Activities (`MainActivity`, `QuizActivity`) and layouts
- **ViewModel**: `QuizViewModel` manages UI-related data and business logic
- **Repository**: `QuizRepository` handles data operations from JSON/PDF files

## Project Structure

```
app/
├── src/main/
│   ├── java/com/ssc/vocabularyquiz/
│   │   ├── data/
│   │   │   ├── model/
│   │   │   │   ├── Question.kt
│   │   │   │   └── QuizCategory.kt
│   │   │   └── repository/
│   │   │       └── QuizRepository.kt
│   │   └── ui/
│   │       ├── adapter/
│   │       │   └── CategoryAdapter.kt
│   │       ├── viewmodel/
│   │       │   ├── QuizViewModel.kt
│   │       │   └── QuizViewModelFactory.kt
│   │       ├── MainActivity.kt
│   │       └── QuizActivity.kt
│   ├── res/
│   │   ├── layout/
│   │   │   ├── activity_main.xml
│   │   │   ├── activity_quiz.xml
│   │   │   └── item_category.xml
│   │   ├── values/
│   │   │   ├── colors.xml
│   │   │   ├── strings.xml
│   │   │   └── themes.xml
│   └── assets/
│       ├── one_word_substitution.json
│       ├── idioms_phrases.json
│       ├── synonyms.json
│       └── antonyms.json
```

## Data Format

### JSON Structure
```json
[
  {
    "question": "What is the one-word substitution for 'a person who loves books'?",
    "options": ["Bibliophile", "Bibliographer", "Librarian", "Scholar"],
    "answerIndex": 0
  }
]
```

### PDF Support
The app includes PdfBox-Android library for PDF parsing. PDF files should contain questions in a structured format that can be parsed into the same JSON structure.

## Key Components

### QuizRepository
- Handles data loading from JSON and PDF files
- Supports both asset files and external files
- Includes PDF text parsing logic

### QuizViewModel
- Manages quiz state and progression
- Tracks correct/incorrect answers
- Handles question navigation and completion

### UI Components
- **MainActivity**: Displays category selection
- **QuizActivity**: Handles quiz flow and user interactions
- **CategoryAdapter**: RecyclerView adapter for category list

## Dependencies

- **Architecture Components**: ViewModel, LiveData
- **Material Design**: Modern UI components
- **Gson**: JSON parsing
- **PdfBox-Android**: PDF text extraction
- **Navigation Components**: Fragment navigation

## Setup Instructions

1. Clone the repository
2. Open in Android Studio
3. Sync Gradle files
4. Add your question files to `app/src/main/assets/`
5. Run the application

## Customization

### Adding New Categories
1. Add new enum value to `QuizCategory`
2. Create corresponding JSON/PDF file in assets
3. Update UI strings if needed

### Modifying Questions
- Edit JSON files in the assets folder
- Follow the specified JSON structure
- Ensure `answerIndex` corresponds to correct option position

## Sample Data

The app includes sample questions for all four categories:
- **One-Word Substitution**: 10 questions covering common vocabulary
- **Idioms & Phrases**: 10 popular English idioms
- **Synonyms**: 10 word pairs with similar meanings
- **Antonyms**: 10 word pairs with opposite meanings

## Future Enhancements

- Timer functionality for each question
- Difficulty levels
- Progress tracking across sessions
- Detailed performance analytics
- Online question database integration

## License

This project is open source and available under the MIT License.