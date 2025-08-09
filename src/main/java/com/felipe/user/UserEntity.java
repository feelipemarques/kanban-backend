package com.felipe.user;

import com.felipe.task.TaskEntity;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

import java.util.List;

@Entity
public class UserEntity extends PanacheEntityBase {

    @Id
    @Email(message = "Deve ser um e-mail válido!")
    public String email;

    @Size(min = 6, message = "Senha precisa ter mais de 6 caracteres!")
    public String password;

    @OneToMany(mappedBy = "userOwner", cascade = CascadeType.ALL)
    public List<TaskEntity> tasks;

}
