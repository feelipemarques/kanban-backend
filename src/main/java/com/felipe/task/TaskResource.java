package com.felipe.task;

import com.felipe.enums.StatusEnum;
import com.felipe.user.UserEntity;
import io.quarkus.panache.common.Parameters;
import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

@Path("/task")
public class TaskResource {
    @Inject
    SecurityContext securityContext;

    @POST
    @Transactional
    public Response createTask(@Valid TaskDTO taskDTO){
        TaskEntity task = new TaskEntity();
        task.taskName = taskDTO.taskName;


        if(taskDTO.status != null){
            task.status = taskDTO.status;
        }
        task.userOwner = UserEntity.findById(securityContext.getUserPrincipal().getName());
        task.persist();
        TaskDTO response = TaskDTO.toDTO(task);
        return Response.ok(response).build();
    }

    @GET
    public Response getAllTasks(){
        return Response.ok(TaskEntity
                .listAll()
                        .stream()
                        .map((e -> TaskDTO.toDTO((TaskEntity) e)))
                        .toList())
                .build();
    }

    @GET
    @Path("{status}")
    @Authenticated
    public Response getTasksByStatus(@PathParam("status") String status){
        try{
        StatusEnum statusEnum = StatusEnum.fromString(status);
        return Response.ok(TaskEntity.find("status = :status and userOwner.email = :email", Parameters.with("status", statusEnum).and("email", securityContext.getUserPrincipal().getName()))
                .stream()
                .map(e -> TaskDTO.toDTO((TaskEntity) e))
                .toList()).build();
        } catch (IllegalArgumentException e){
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid status").build();
        }
    }

    @PUT
    @Transactional
    @Path("{id}/{status}")
    public Response updateTaskById(@PathParam("id") Long id, @PathParam("status") String status){
        TaskEntity task = TaskEntity.findById(id);
        if(task == null){
            return Response.status(Response.Status.NOT_FOUND).entity("TaskID not found!").build();
        }
        try{
            task.status = StatusEnum.fromString(status);
            return Response.ok("Task status " + id + " updated to " + status).build();
        }catch (IllegalArgumentException e){
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid status").build();
        }
    }

    @DELETE
    @Transactional
    @Path("{id}")
    public Response deleteTaskById(@PathParam("id") Long id){
        TaskEntity.deleteById(id);
        return Response.ok().build();
    }
}
