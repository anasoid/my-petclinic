package org.anasoid.petclinic.service.impl;

import java.util.Optional;
import org.anasoid.petclinic.domain.Owner;
import org.anasoid.petclinic.repository.OwnerRepository;
import org.anasoid.petclinic.service.OwnerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link org.anasoid.petclinic.domain.Owner}.
 */
@Service
@Transactional
public class OwnerServiceImpl implements OwnerService {

    private static final Logger log = LoggerFactory.getLogger(OwnerServiceImpl.class);

    private final OwnerRepository ownerRepository;

    public OwnerServiceImpl(OwnerRepository ownerRepository) {
        this.ownerRepository = ownerRepository;
    }

    @Override
    public Owner save(Owner owner) {
        log.debug("Request to save Owner : {}", owner);
        return ownerRepository.save(owner);
    }

    @Override
    public Owner update(Owner owner) {
        log.debug("Request to update Owner : {}", owner);
        return ownerRepository.save(owner);
    }

    @Override
    public Optional<Owner> partialUpdate(Owner owner) {
        log.debug("Request to partially update Owner : {}", owner);

        return ownerRepository
            .findById(owner.getId())
            .map(existingOwner -> {
                if (owner.getFirstName() != null) {
                    existingOwner.setFirstName(owner.getFirstName());
                }
                if (owner.getLastName() != null) {
                    existingOwner.setLastName(owner.getLastName());
                }
                if (owner.getAddress() != null) {
                    existingOwner.setAddress(owner.getAddress());
                }
                if (owner.getCity() != null) {
                    existingOwner.setCity(owner.getCity());
                }
                if (owner.getTelephone() != null) {
                    existingOwner.setTelephone(owner.getTelephone());
                }

                return existingOwner;
            })
            .map(ownerRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Owner> findOne(Long id) {
        log.debug("Request to get Owner : {}", id);
        return ownerRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Owner : {}", id);
        ownerRepository.deleteById(id);
    }
}
