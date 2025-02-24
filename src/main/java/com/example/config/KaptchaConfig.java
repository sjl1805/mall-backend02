package com.example.config;

import com.google.code.kaptcha.Producer;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import java.util.Properties;

@Configuration
@EnableConfigurationProperties(KaptchaProperties.class)
public class KaptchaConfig {

    private final KaptchaProperties properties;

    public KaptchaConfig(KaptchaProperties properties) {
        this.properties = properties;
    }

    @Bean
    public Producer captchaProducer() {
        Properties props = new Properties();
        props.setProperty("kaptcha.border", properties.getBorder());
        props.setProperty("kaptcha.borderColor", properties.getBorderColor());
        props.setProperty("kaptcha.textproducer.font.size", String.valueOf(properties.getTextproducer().getFont().getSize()));
        props.setProperty("kaptcha.textproducer.char.length", String.valueOf(properties.getTextproducer().getCharLength()));
        props.setProperty("kaptcha.textproducer.font.color", properties.getTextproducer().getFontColor());
        props.setProperty("kaptcha.image.width", String.valueOf(properties.getImage().getWidth()));
        props.setProperty("kaptcha.image.height", String.valueOf(properties.getImage().getHeight()));
        Config config = new Config(props);
        return config.getProducerImpl();
    }
} 