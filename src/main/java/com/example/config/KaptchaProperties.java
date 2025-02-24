package com.example.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import lombok.Getter;
import lombok.Setter;

@ConfigurationProperties(prefix = "kaptcha")
@Getter
@Setter
public class KaptchaProperties {
    private String border;
    private String borderColor;
    private Textproducer textproducer;
    private Image image;
    private Session session;

    @Getter @Setter
    public static class Textproducer {
        private Font font;
        private int charLength;
        private String fontColor;
    }

    @Getter @Setter
    public static class Font {
        private int size;
    }

    @Getter @Setter
    public static class Image {
        private int width;
        private int height;
    }

    @Getter @Setter
    public static class Session {
        private String key;
    }
} 