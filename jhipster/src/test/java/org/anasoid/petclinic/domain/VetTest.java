package org.anasoid.petclinic.domain;

import static org.anasoid.petclinic.domain.SpecialtyTestSamples.*;
import static org.anasoid.petclinic.domain.VetTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashSet;
import java.util.Set;
import org.anasoid.petclinic.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VetTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Vet.class);
        Vet vet1 = getVetSample1();
        Vet vet2 = new Vet();
        assertThat(vet1).isNotEqualTo(vet2);

        vet2.setId(vet1.getId());
        assertThat(vet1).isEqualTo(vet2);

        vet2 = getVetSample2();
        assertThat(vet1).isNotEqualTo(vet2);
    }

    @Test
    void specialtiesTest() {
        Vet vet = getVetRandomSampleGenerator();
        Specialty specialtyBack = getSpecialtyRandomSampleGenerator();

        vet.addSpecialties(specialtyBack);
        assertThat(vet.getSpecialties()).containsOnly(specialtyBack);

        vet.removeSpecialties(specialtyBack);
        assertThat(vet.getSpecialties()).doesNotContain(specialtyBack);

        vet.specialties(new HashSet<>(Set.of(specialtyBack)));
        assertThat(vet.getSpecialties()).containsOnly(specialtyBack);

        vet.setSpecialties(new HashSet<>());
        assertThat(vet.getSpecialties()).doesNotContain(specialtyBack);
    }
}
