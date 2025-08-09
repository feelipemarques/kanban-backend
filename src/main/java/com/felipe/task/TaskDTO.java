package com.felipe.task;

import com.felipe.enums.StatusEnum;

public class TaskDTO {

    public Long id;
    public String taskName;
    public StatusEnum status;
    public String userEmail;

    public static TaskDTO toDTO(TaskEntity task){
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.id = task.id;
        taskDTO.taskName = task.taskName;
        taskDTO.status = task.status;
        taskDTO.userEmail = task.userOwner.email;
        return taskDTO;
    }

}
