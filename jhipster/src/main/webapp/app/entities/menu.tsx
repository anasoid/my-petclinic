import React from 'react';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/person">
        Person
      </MenuItem>
      <MenuItem icon="asterisk" to="/pet-type">
        Pet Type
      </MenuItem>
      <MenuItem icon="asterisk" to="/visit">
        Visit
      </MenuItem>
      <MenuItem icon="asterisk" to="/specialty">
        Specialty
      </MenuItem>
      <MenuItem icon="asterisk" to="/vet">
        Vet
      </MenuItem>
      <MenuItem icon="asterisk" to="/pet">
        Pet
      </MenuItem>
      <MenuItem icon="asterisk" to="/owner">
        Owner
      </MenuItem>
      <MenuItem icon="asterisk" to="/role">
        Role
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
