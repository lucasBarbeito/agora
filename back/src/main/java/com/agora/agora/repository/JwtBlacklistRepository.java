package com.agora.agora.repository;

import com.agora.agora.model.JwtBlacklist;
import org.springframework.data.repository.CrudRepository;

public interface JwtBlacklistRepository extends CrudRepository<JwtBlacklist,Integer> {
    JwtBlacklist findByToken(String token);
}
