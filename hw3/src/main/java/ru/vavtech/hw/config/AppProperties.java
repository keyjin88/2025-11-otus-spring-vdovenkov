package ru.vavtech.hw.config;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

import java.util.Locale;

@ConfigurationProperties(prefix = "application")
@Getter
public class AppProperties implements TestConfig, TestFileNameProvider {

    private final Integer rightAnswersCountToPass;

    private final String testFileName;

    private final Locale locale;

    @ConstructorBinding
    public AppProperties(Integer rightAnswersCountToPass, String testFileName, String locale) {
        this.rightAnswersCountToPass = rightAnswersCountToPass;
        this.testFileName = testFileName;
        this.locale = Locale.forLanguageTag(locale.replace("_", "-"));
    }

    @Override
    public int getRightAnswersCountToPass() {
        return rightAnswersCountToPass;
    }

    @Override
    public String getTestFileName() {
        return testFileName + "_" + locale + ".csv";
    }
}
