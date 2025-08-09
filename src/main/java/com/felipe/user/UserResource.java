package com.felipe.user;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import org.mindrot.jbcrypt.BCrypt;

@Path("/auth")
public class UserResource {

    public String response = "Usuário ou senha inválido!";

    @POST
    @Path("/register")
    @Transactional
    public Response registerUser(@Valid UserEntity user) {
        if(!(UserEntity.findById(user.email) == null)) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity("Usuário já cadastrado!")
                    .build();
        }

        user.password = BCrypt.hashpw(user.password, BCrypt.gensalt());
        user.persist();

        return Response
                .status(Response.Status.CREATED)
                .entity("Usuário: " + user.email + " cadastrado!")
                .build();
    }

    @POST
    @Path("/login")
    public Response logIn(@Valid UserEntity user){
        UserEntity existingUser = UserEntity.findById(user.email);

        if(existingUser == null){
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(response)
                    .build();
        }
        if(!BCrypt.checkpw(user.password, existingUser.password)) {
            return Response
                    .status(Response.Status.UNAUTHORIZED)
                    .entity(response)
                    .build();
        }else{
            return Response
                    .ok("Olá, " + user.email + "! Seja bem vindo!")
                    .build();
        }
    }

    @GET
    @Path("/users")
    public Response getUsers(){
        return Response.ok(UserEntity.listAll()).build();
    }
}
