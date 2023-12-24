package com.cyolo.words.frequency.evaluator.controller;

import com.cyolo.words.frequency.evaluator.controller.mapper.WordMapper;
import com.cyolo.words.frequency.evaluator.model.WordsStatistic;
import com.cyolo.words.frequency.evaluator.service.WordService;
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
