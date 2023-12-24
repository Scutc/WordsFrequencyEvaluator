package com.cyolo.controller;

import com.cyolo.controller.mapper.WordMapper;
import com.cyolo.model.WordsStatistic;
import com.cyolo.service.WordService;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Status;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class WordController implements WordApi {
    private final WordService wordService;
    private final WordMapper wordMapper;

    @Override
    @Status(HttpStatus.CREATED)
    public void addWords(String body) {
        wordService.addWords(body);
    }

    @Override
    public String getWordsStatistic() {
        WordsStatistic wordsStatistic = wordService.getWordsStatistic();
        return wordMapper.fromWordsStatistic(wordsStatistic);
    }
}
