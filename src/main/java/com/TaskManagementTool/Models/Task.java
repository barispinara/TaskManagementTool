package com.TaskManagementTool.Models;

import java.util.Date;

import org.springframework.data.annotation.Id;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String taskName;
    private TaskStatus taskStatus;
    private Date createdDate;
    private Date updatedDate;
    //private Project project;

    public Task(String taskName){
        this.taskName = taskName;
    }

}
