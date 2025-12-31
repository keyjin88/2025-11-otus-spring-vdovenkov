package ru.vavtech.hw4.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Locale;

@Setter
@ConfigurationProperties(prefix = "application")
public class AppProperties implements TestConfig, TestFileNameProvider {

    @Value("${test.rightAnswersCountToPass}")
    private Integer rightAnswersCountToPass;

    @Value("${test.fileName}")
    private String testFileName;

    @Getter
    @Value("${application.locale}")
    private Locale locale;

    @Override
    public int getRightAnswersCountToPass() {
        return rightAnswersCountToPass;
    }

    @Override
    public String getTestFileName() {
        return testFileName + "_" + locale + ".csv";
    }
}
