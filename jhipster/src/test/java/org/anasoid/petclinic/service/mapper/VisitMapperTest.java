package org.anasoid.petclinic.service.mapper;

import static org.anasoid.petclinic.domain.VisitAsserts.*;
import static org.anasoid.petclinic.domain.VisitTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class VisitMapperTest {

    private VisitMapper visitMapper;

    @BeforeEach
    void setUp() {
        visitMapper = new VisitMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getVisitSample1();
        var actual = visitMapper.toEntity(visitMapper.toDto(expected));
        assertVisitAllPropertiesEquals(expected, actual);
    }
}
