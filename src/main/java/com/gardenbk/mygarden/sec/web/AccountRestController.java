package com.gardenbk.mygarden.sec.web;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gardenbk.mygarden.sec.entities.AppRole;
import com.gardenbk.mygarden.sec.entities.AppUser;
import com.gardenbk.mygarden.sec.service.AccountService;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class AccountRestController {
    private AccountService accountService;

    public AccountRestController(AccountService accountService) {
        this.accountService = accountService;
    }
//Methode qui permet de retourner une liste de user
    @GetMapping(path = "/users")
    //@PostAuthorize("hasAuthority('USER')")
    public List<AppUser> appUsers(){
        return accountService.listUsers();

    }
    //Methode qui permet d'ajouter un user
    @PostMapping(path = "/users")
    //@PostAuthorize("hasAuthority('ADMIN')")
    public AppUser saveUser(@RequestBody AppUser appUser){
        return  accountService.addNewUser(appUser);

    }
//Ajouter un nouveau role
    @PostMapping(path = "/roles")
    public AppRole saveRole(@RequestBody AppRole appRole){
        return  accountService.addNewRole(appRole);

    }
//Ajouter un role ?? un utilisateur
    @PostMapping(path = "/addRoleToUser")
    public void addRoleToUser(@RequestBody RoleUserForm roleUserForm){
         accountService.addRoleToUser(roleUserForm.getUsername(),roleUserForm.getRoleName());

    }
    @GetMapping(path = "/refreshToken")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String authToken= request.getHeader(JwtUtil.Auth_Header);
        if (authToken!=null && authToken.startsWith(JwtUtil.PREFIX)){
            try {
                //le 7 c'est pour indiquer d'ignorer les 7 premier caractere de la chaine
                String jwt=authToken.substring(JwtUtil.PREFIX.length());
                Algorithm algorithm=Algorithm.HMAC256(JwtUtil.SECRET);
                JWTVerifier jwtVerifier= JWT.require(algorithm).build();
                DecodedJWT decodedJWT = jwtVerifier.verify(jwt);
                String username=decodedJWT.getSubject();
                AppUser appUser=accountService.loadUserByUsername(username);
                String jwtAccessToken= JWT.create()
                        .withSubject(appUser.getUsername())
                        .withExpiresAt(new Date(System.currentTimeMillis()+JwtUtil.EXPIRE_ACCESS_TOKEN))
                        .withIssuer(request.getRequestURL().toString())
                        .withClaim("roles",appUser.getAppRoles().stream().map(r->r.getRoleName()).collect(Collectors.toList()))
                        .sign(algorithm) ;
                Map<String,String> idToken=new HashMap<>();
                idToken.put("access-token",jwtAccessToken);
                idToken.put("refresh-token",jwt);
                response.setContentType("application/json");
                new ObjectMapper().writeValue(response.getOutputStream(),idToken);
            }
            catch (Exception e){
                throw e;
            }
        }
        else {
            throw new RuntimeException("Refresh token required");
        }
        }
    //si vous voulez consulter l'utilisateur authentifi?? :sn profil
    @GetMapping(path = "/profile")
    /*public AppUser profile(Principal principal){

        return accountService.loadUserByUsername(principal.getName());
    }*/
    public AppUser profile(Principal principal){
        return accountService.loadUserByUsername(principal.getName());
    }

    }


@Data
class RoleUserForm{
    private String username;
    private String roleName;
}
