package ru.vavtech.hw.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Locale;

@Setter
@ConfigurationProperties(prefix = "application")
public class AppProperties implements TestConfig, TestFileNameProvider {

    private Integer rightAnswersCountToPass;

    private String testFileName;

    @Getter
    private Locale locale;

    public void setLocale(String localeString) {
        this.locale = Locale.forLanguageTag(localeString.replace("_", "-"));
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
