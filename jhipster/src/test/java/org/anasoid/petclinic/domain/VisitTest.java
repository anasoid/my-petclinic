package org.anasoid.petclinic.domain;

import static org.anasoid.petclinic.domain.PetTestSamples.*;
import static org.anasoid.petclinic.domain.VisitTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import org.anasoid.petclinic.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VisitTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Visit.class);
        Visit visit1 = getVisitSample1();
        Visit visit2 = new Visit();
        assertThat(visit1).isNotEqualTo(visit2);

        visit2.setId(visit1.getId());
        assertThat(visit1).isEqualTo(visit2);

        visit2 = getVisitSample2();
        assertThat(visit1).isNotEqualTo(visit2);
    }

    @Test
    void petsTest() {
        Visit visit = getVisitRandomSampleGenerator();
        Pet petBack = getPetRandomSampleGenerator();

        visit.setPets(petBack);
        assertThat(visit.getPets()).isEqualTo(petBack);

        visit.pets(null);
        assertThat(visit.getPets()).isNull();
    }
}
