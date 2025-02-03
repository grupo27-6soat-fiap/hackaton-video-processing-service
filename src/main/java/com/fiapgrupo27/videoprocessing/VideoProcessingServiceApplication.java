package com.fiapgrupo27.videoprocessing;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
@EnableRabbit
public class VideoProcessingServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(VideoProcessingServiceApplication.class, args);
    }
}
    