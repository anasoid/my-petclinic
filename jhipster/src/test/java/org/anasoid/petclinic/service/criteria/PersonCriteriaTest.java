package org.anasoid.petclinic.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class PersonCriteriaTest {

    @Test
    void newPersonCriteriaHasAllFiltersNullTest() {
        var personCriteria = new PersonCriteria();
        assertThat(personCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void personCriteriaFluentMethodsCreatesFiltersTest() {
        var personCriteria = new PersonCriteria();

        setAllFilters(personCriteria);

        assertThat(personCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void personCriteriaCopyCreatesNullFilterTest() {
        var personCriteria = new PersonCriteria();
        var copy = personCriteria.copy();

        assertThat(personCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(personCriteria)
        );
    }

    @Test
    void personCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var personCriteria = new PersonCriteria();
        setAllFilters(personCriteria);

        var copy = personCriteria.copy();

        assertThat(personCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(personCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var personCriteria = new PersonCriteria();

        assertThat(personCriteria).hasToString("PersonCriteria{}");
    }

    private static void setAllFilters(PersonCriteria personCriteria) {
        personCriteria.id();
        personCriteria.firstName();
        personCriteria.lastName();
        personCriteria.distinct();
    }

    private static Condition<PersonCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getFirstName()) &&
                condition.apply(criteria.getLastName()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<PersonCriteria> copyFiltersAre(PersonCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getFirstName(), copy.getFirstName()) &&
                condition.apply(criteria.getLastName(), copy.getLastName()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
