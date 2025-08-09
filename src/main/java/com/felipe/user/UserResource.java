package com.felipe.user;

import io.smallrye.jwt.build.Jwt;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Map;

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
            String token = Jwt
                    .issuer("kanban")
                    .upn(user.email)
                    .sign();
            Map<String, String> response = Map.of("token", token);
            return Response.ok(response).build();
        }
    }

    @GET
    @Path("/users")
    public Response getUsers(){
        return Response.ok(UserEntity.listAll()).build();
    }
}
