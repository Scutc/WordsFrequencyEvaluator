package com.cyolo.controller;

import com.cyolo.mapper.WordMapper;
import com.cyolo.model.WordsStatistic;
import com.cyolo.service.WordService;
import io.micronaut.http.annotation.Controller;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class WordController implements WordApi {
    private final WordService wordService;
    private final WordMapper wordMapper;

    @Override
    public void addWords(String body) {
        wordService.addWord(body);
    }

    @Override
    public String getWords() {
        WordsStatistic wordsStatistic = wordService.getWordStatistics();
        return wordMapper.fromWordsStatistic(wordsStatistic);
    }
}
