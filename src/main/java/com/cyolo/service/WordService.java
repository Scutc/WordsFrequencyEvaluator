package com.cyolo.service;

import com.cyolo.model.WordsStatistic;

public interface WordService {
    void addWords(String words);

    WordsStatistic getWordsStatistic();
}
