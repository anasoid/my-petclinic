package org.anasoid.petclinic.domain;

import static org.anasoid.petclinic.domain.PersonTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import org.anasoid.petclinic.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PersonTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Person.class);
        Person person1 = getPersonSample1();
        Person person2 = new Person();
        assertThat(person1).isNotEqualTo(person2);

        person2.setId(person1.getId());
        assertThat(person1).isEqualTo(person2);

        person2 = getPersonSample2();
        assertThat(person1).isNotEqualTo(person2);
    }
}
