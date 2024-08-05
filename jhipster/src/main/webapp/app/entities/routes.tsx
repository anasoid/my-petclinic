import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Person from './person';
import PetType from './pet-type';
import Visit from './visit';
import Specialty from './specialty';
import Vet from './vet';
import Pet from './pet';
import Owner from './owner';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="person/*" element={<Person />} />
        <Route path="pet-type/*" element={<PetType />} />
        <Route path="visit/*" element={<Visit />} />
        <Route path="specialty/*" element={<Specialty />} />
        <Route path="vet/*" element={<Vet />} />
        <Route path="pet/*" element={<Pet />} />
        <Route path="owner/*" element={<Owner />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
