package com.cyolo.words.frequency.evaluator.service;

import com.cyolo.words.frequency.evaluator.model.WordsStatistic;

public interface WordService {
    void addWords(String words);

    WordsStatistic getWordsStatistic();
}
