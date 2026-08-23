package com.autodm.server.repository;

import com.autodm.server.model.CharacterResource;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CharacterResourceRepository extends CrudRepository<CharacterResource, Long> {
    List<CharacterResource> findByPlayerCharacterId(Long playerCharacterId);
}
