package com.cyolo.controller;

import com.cyolo.service.WordService;
import io.micronaut.http.annotation.Controller;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class WordController implements WordApi {
    private final WordService wordService;

    @Override
    public void addWords(String body) {
        wordService.addWord(body);
    }

    @Override
    public String getWords() {
        return wordService.getWordStatistics();
    }
}
