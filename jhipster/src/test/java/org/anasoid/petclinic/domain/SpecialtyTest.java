package org.anasoid.petclinic.domain;

import static org.anasoid.petclinic.domain.SpecialtyTestSamples.*;
import static org.anasoid.petclinic.domain.VetTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashSet;
import java.util.Set;
import org.anasoid.petclinic.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SpecialtyTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Specialty.class);
        Specialty specialty1 = getSpecialtySample1();
        Specialty specialty2 = new Specialty();
        assertThat(specialty1).isNotEqualTo(specialty2);

        specialty2.setId(specialty1.getId());
        assertThat(specialty1).isEqualTo(specialty2);

        specialty2 = getSpecialtySample2();
        assertThat(specialty1).isNotEqualTo(specialty2);
    }

    @Test
    void vetTest() {
        Specialty specialty = getSpecialtyRandomSampleGenerator();
        Vet vetBack = getVetRandomSampleGenerator();

        specialty.addVet(vetBack);
        assertThat(specialty.getVets()).containsOnly(vetBack);
        assertThat(vetBack.getSpecialties()).containsOnly(specialty);

        specialty.removeVet(vetBack);
        assertThat(specialty.getVets()).doesNotContain(vetBack);
        assertThat(vetBack.getSpecialties()).doesNotContain(specialty);

        specialty.vets(new HashSet<>(Set.of(vetBack)));
        assertThat(specialty.getVets()).containsOnly(vetBack);
        assertThat(vetBack.getSpecialties()).containsOnly(specialty);

        specialty.setVets(new HashSet<>());
        assertThat(specialty.getVets()).doesNotContain(vetBack);
        assertThat(vetBack.getSpecialties()).doesNotContain(specialty);
    }
}
