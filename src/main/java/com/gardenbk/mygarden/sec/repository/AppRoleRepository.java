package com.gardenbk.mygarden.sec.repository;

import com.gardenbk.mygarden.sec.entities.AppRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppRoleRepository extends JpaRepository<AppRole,Long> {
    AppRole findByRoleName(String role);
}
