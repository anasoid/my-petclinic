package org.anasoid.petclinic.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class RoleCriteriaTest {

    @Test
    void newRoleCriteriaHasAllFiltersNullTest() {
        var roleCriteria = new RoleCriteria();
        assertThat(roleCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void roleCriteriaFluentMethodsCreatesFiltersTest() {
        var roleCriteria = new RoleCriteria();

        setAllFilters(roleCriteria);

        assertThat(roleCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void roleCriteriaCopyCreatesNullFilterTest() {
        var roleCriteria = new RoleCriteria();
        var copy = roleCriteria.copy();

        assertThat(roleCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(roleCriteria)
        );
    }

    @Test
    void roleCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var roleCriteria = new RoleCriteria();
        setAllFilters(roleCriteria);

        var copy = roleCriteria.copy();

        assertThat(roleCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(roleCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var roleCriteria = new RoleCriteria();

        assertThat(roleCriteria).hasToString("RoleCriteria{}");
    }

    private static void setAllFilters(RoleCriteria roleCriteria) {
        roleCriteria.id();
        roleCriteria.name();
        roleCriteria.distinct();
    }

    private static Condition<RoleCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria -> condition.apply(criteria.getId()) && condition.apply(criteria.getName()) && condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<RoleCriteria> copyFiltersAre(RoleCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
