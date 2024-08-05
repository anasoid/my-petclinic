import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './specialty.reducer';

export const SpecialtyDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const specialtyEntity = useAppSelector(state => state.specialty.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="specialtyDetailsHeading">Specialty</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{specialtyEntity.id}</dd>
          <dt>
            <span id="name">Name</span>
          </dt>
          <dd>{specialtyEntity.name}</dd>
          <dt>Vet</dt>
          <dd>
            {specialtyEntity.vets
              ? specialtyEntity.vets.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {specialtyEntity.vets && i === specialtyEntity.vets.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/specialty" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/specialty/${specialtyEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default SpecialtyDetail;
