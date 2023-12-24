package com.cyolo.controller

import com.cyolo.controller.mapper.WordMapper
import com.cyolo.model.WordFrequency
import com.cyolo.model.WordsStatistic
import com.cyolo.service.WordService
import spock.lang.Specification

class WordControllerTest extends Specification {
    WordService wordService = Mock()
    WordMapper wordMapper = Mock()

    WordController controller = new WordController(wordService, wordMapper)

    def "addWords calls wordService.addWord"() {
        given:
        String body = "test"

        when:
        controller.addWords(body)

        then:
        1 * wordService.addWords(body)
    }

    def "getWordsStatistic calls wordService.getWordsStatistic and returns mapped result"() {
        given:
        List<WordFrequency> wordFrequencies = List.of(new WordFrequency("test", 1))
        WordsStatistic wordsStatistic = new WordsStatistic(wordFrequencies, 1, 1)
        String expectedResult = "result"

        when:
        String result = controller.getWordsStatistic()

        then:
        1 * wordService.getWordsStatistic() >> wordsStatistic
        1 * wordMapper.fromWordsStatistic(wordsStatistic) >> expectedResult
        result == expectedResult
    }
}
