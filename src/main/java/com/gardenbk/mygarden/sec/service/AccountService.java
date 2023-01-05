package com.gardenbk.mygarden.sec.service;

import com.gardenbk.mygarden.sec.entities.AppRole;
import com.gardenbk.mygarden.sec.entities.AppUser;

import java.util.List;

public interface AccountService {
    AppUser addNewUser(AppUser appUser);
    AppRole addNewRole(AppRole appRole);
    void addRoleToUser(String username,String roleName);
    AppUser loadUserByUsername(String username);
    List<AppUser> listUsers();
}
