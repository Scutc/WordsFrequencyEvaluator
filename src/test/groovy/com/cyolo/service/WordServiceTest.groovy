package com.cyolo.service

import com.cyolo.error.BaseException
import com.cyolo.model.WordFrequency
import com.cyolo.model.WordsStatistic
import spock.lang.Specification

import static com.cyolo.error.WordsFrequencyEvaluatorError.NO_WORDS_PROVIDED
import static com.cyolo.error.WordsFrequencyEvaluatorError.WORDS_NOT_FOUND

class WordServiceTest extends Specification {
    WordStorageService wordStorageService = Mock()

    WordService service = new WordServiceImpl(wordStorageService)

    def "addWords calls wordStorageService.addWords"() {
        given:
        String words = "word1"

        when:
        service.addWords(words)

        then:
        1 * wordStorageService.addWords(_)
    }

    def "getWordsStatistic calls wordStorageService.getWordFrequenciesSortedDesc and returns wordsStatistic"() {
        given:
        List<WordFrequency> wordFrequencies = List.of(new WordFrequency("test", 1))
        WordsStatistic wordsStatistic = new WordsStatistic(wordFrequencies, 1, 1)

        when:
        WordsStatistic result = service.getWordsStatistic()

        then:
        1 * wordStorageService.getWordFrequenciesSortedDesc() >> wordFrequencies
        result == wordsStatistic
    }

    def "addWords should throw BaseException if no words provided"() {
        when:
        service.addWords("")

        then:
        BaseException e = thrown()
        e.error == NO_WORDS_PROVIDED
    }

    def "getWordsStatistic should throw BaseException if no words in storage"() {
        when:
        service.getWordsStatistic()

        then:
        wordStorageService.getWordFrequenciesSortedDesc() >> []
        def exception = thrown(BaseException)
        exception.error == WORDS_NOT_FOUND
    }
}
