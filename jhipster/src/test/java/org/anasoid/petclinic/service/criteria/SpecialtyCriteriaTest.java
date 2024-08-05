package org.anasoid.petclinic.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class SpecialtyCriteriaTest {

    @Test
    void newSpecialtyCriteriaHasAllFiltersNullTest() {
        var specialtyCriteria = new SpecialtyCriteria();
        assertThat(specialtyCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void specialtyCriteriaFluentMethodsCreatesFiltersTest() {
        var specialtyCriteria = new SpecialtyCriteria();

        setAllFilters(specialtyCriteria);

        assertThat(specialtyCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void specialtyCriteriaCopyCreatesNullFilterTest() {
        var specialtyCriteria = new SpecialtyCriteria();
        var copy = specialtyCriteria.copy();

        assertThat(specialtyCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(specialtyCriteria)
        );
    }

    @Test
    void specialtyCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var specialtyCriteria = new SpecialtyCriteria();
        setAllFilters(specialtyCriteria);

        var copy = specialtyCriteria.copy();

        assertThat(specialtyCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(specialtyCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var specialtyCriteria = new SpecialtyCriteria();

        assertThat(specialtyCriteria).hasToString("SpecialtyCriteria{}");
    }

    private static void setAllFilters(SpecialtyCriteria specialtyCriteria) {
        specialtyCriteria.id();
        specialtyCriteria.name();
        specialtyCriteria.vetId();
        specialtyCriteria.distinct();
    }

    private static Condition<SpecialtyCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getVetId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<SpecialtyCriteria> copyFiltersAre(SpecialtyCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getVetId(), copy.getVetId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
