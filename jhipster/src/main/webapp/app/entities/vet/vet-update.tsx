import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ISpecialty } from 'app/shared/model/specialty.model';
import { getEntities as getSpecialties } from 'app/entities/specialty/specialty.reducer';
import { IVet } from 'app/shared/model/vet.model';
import { getEntity, updateEntity, createEntity, reset } from './vet.reducer';

export const VetUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const specialties = useAppSelector(state => state.specialty.entities);
  const vetEntity = useAppSelector(state => state.vet.entity);
  const loading = useAppSelector(state => state.vet.loading);
  const updating = useAppSelector(state => state.vet.updating);
  const updateSuccess = useAppSelector(state => state.vet.updateSuccess);

  const handleClose = () => {
    navigate('/vet' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getSpecialties({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  // eslint-disable-next-line complexity
  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }

    const entity = {
      ...vetEntity,
      ...values,
      specialties: mapIdList(values.specialties),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...vetEntity,
          specialties: vetEntity?.specialties?.map(e => e.id.toString()),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="petclinicApp.vet.home.createOrEditLabel" data-cy="VetCreateUpdateHeading">
            Create or edit a Vet
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="vet-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField
                label="First Name"
                id="vet-firstName"
                name="firstName"
                data-cy="firstName"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                }}
              />
              <ValidatedField label="Last Name" id="vet-lastName" name="lastName" data-cy="lastName" type="text" />
              <ValidatedField label="Specialties" id="vet-specialties" data-cy="specialties" type="select" multiple name="specialties">
                <option value="" key="0" />
                {specialties
                  ? specialties.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.name}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/vet" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">Back</span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp; Save
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default VetUpdate;
