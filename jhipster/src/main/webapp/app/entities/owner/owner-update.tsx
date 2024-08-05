import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IOwner } from 'app/shared/model/owner.model';
import { getEntity, updateEntity, createEntity, reset } from './owner.reducer';

export const OwnerUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const ownerEntity = useAppSelector(state => state.owner.entity);
  const loading = useAppSelector(state => state.owner.loading);
  const updating = useAppSelector(state => state.owner.updating);
  const updateSuccess = useAppSelector(state => state.owner.updateSuccess);

  const handleClose = () => {
    navigate('/owner' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }
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
    if (values.telephone !== undefined && typeof values.telephone !== 'number') {
      values.telephone = Number(values.telephone);
    }

    const entity = {
      ...ownerEntity,
      ...values,
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
          ...ownerEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="petclinicApp.owner.home.createOrEditLabel" data-cy="OwnerCreateUpdateHeading">
            Create or edit a Owner
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="owner-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField
                label="First Name"
                id="owner-firstName"
                name="firstName"
                data-cy="firstName"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                }}
              />
              <ValidatedField label="Last Name" id="owner-lastName" name="lastName" data-cy="lastName" type="text" />
              <ValidatedField
                label="Address"
                id="owner-address"
                name="address"
                data-cy="address"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                  minLength: { value: 1, message: 'This field is required to be at least 1 characters.' },
                }}
              />
              <ValidatedField
                label="City"
                id="owner-city"
                name="city"
                data-cy="city"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                  minLength: { value: 1, message: 'This field is required to be at least 1 characters.' },
                }}
              />
              <ValidatedField
                label="Telephone"
                id="owner-telephone"
                name="telephone"
                data-cy="telephone"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                  validate: v => isNumber(v) || 'This field should be a number.',
                }}
              />
              <ValidatedField label="Ffff" id="owner-ffff" name="ffff" data-cy="ffff" type="text" />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/owner" replace color="info">
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

export default OwnerUpdate;
