package org.anasoid.petclinic.cucumber;

import io.cucumber.spring.CucumberContextConfiguration;
import org.anasoid.petclinic.IntegrationTest;
import org.springframework.test.context.web.WebAppConfiguration;

@CucumberContextConfiguration
@IntegrationTest
@WebAppConfiguration
public class CucumberTestContextConfiguration {}
