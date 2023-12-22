package com.cyolo.service;

import com.cyolo.model.WordsStatistic;

public interface WordService {
    void addWord(String words);

    WordsStatistic getWordStatistics();
}
