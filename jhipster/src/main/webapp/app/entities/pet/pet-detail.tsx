import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './pet.reducer';

export const PetDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const petEntity = useAppSelector(state => state.pet.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="petDetailsHeading">Pet</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{petEntity.id}</dd>
          <dt>
            <span id="name">Name</span>
          </dt>
          <dd>{petEntity.name}</dd>
          <dt>
            <span id="birthDate">Birth Date</span>
          </dt>
          <dd>{petEntity.birthDate ? <TextFormat value={petEntity.birthDate} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}</dd>
          <dt>Type</dt>
          <dd>{petEntity.type ? petEntity.type.name : ''}</dd>
          <dt>Owner</dt>
          <dd>{petEntity.owner ? petEntity.owner.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/pet" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/pet/${petEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default PetDetail;
