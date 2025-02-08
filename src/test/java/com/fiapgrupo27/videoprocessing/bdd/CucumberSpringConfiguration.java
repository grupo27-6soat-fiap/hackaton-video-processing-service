package com.fiapgrupo27.videoprocessing.bdd;

import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@CucumberContextConfiguration
@SpringBootTest(classes = CucumberSpringConfiguration.class)
@TestPropertySource(locations = "classpath:application-test.yml")
public class CucumberSpringConfiguration {
}
