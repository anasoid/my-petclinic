import person from 'app/entities/person/person.reducer';
import petType from 'app/entities/pet-type/pet-type.reducer';
import visit from 'app/entities/visit/visit.reducer';
import specialty from 'app/entities/specialty/specialty.reducer';
import vet from 'app/entities/vet/vet.reducer';
import pet from 'app/entities/pet/pet.reducer';
import owner from 'app/entities/owner/owner.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  person,
  petType,
  visit,
  specialty,
  vet,
  pet,
  owner,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
