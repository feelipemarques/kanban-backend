package com.felipe.task;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.felipe.enums.StatusEnum;
import com.felipe.user.UserEntity;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
public class TaskEntity extends PanacheEntity {

    @NotBlank(message = "Nome não pode ser vazio!")
    public String taskName;

    @Enumerated(EnumType.STRING)
    public StatusEnum status = StatusEnum.TO_DO;

    @ManyToOne
    @JoinColumn(name = "user_id")
    public UserEntity userOwner;

}
