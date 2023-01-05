package com.gardenbk.mygarden.sec.web;

import com.gardenbk.mygarden.sec.entities.AppRole;
import com.gardenbk.mygarden.sec.entities.AppUser;
import com.gardenbk.mygarden.sec.service.AccountService;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AccountRestController {
    private AccountService accountService;

    public AccountRestController(AccountService accountService) {
        this.accountService = accountService;
    }
//Methode qui permet de retourner une liste de user
    @GetMapping(path = "/users")
    public List<AppUser> appUsers(){
        return accountService.listUsers();

    }
    //Methode qui permetd'ajouter un user
    @PostMapping(path = "/users")
    public AppUser saveUser(@RequestBody AppUser appUser){
        return  accountService.addNewUser(appUser);

    }
//Ajouter un nouveau role
    @PostMapping(path = "/roles")
    public AppRole saveRole(@RequestBody AppRole appRole){
        return  accountService.addNewRole(appRole);

    }
//Ajouter un role à un utilisateur
    @PostMapping(path = "/addRoleToUser")
    public void addRoleToUser(@RequestBody RoleUserForm roleUserForm){
         accountService.addRoleToUser(roleUserForm.getUsername(),roleUserForm.getRoleName());

    }
}
@Getter
@Setter
class RoleUserForm{
    private String username;
    private String roleName;
}
