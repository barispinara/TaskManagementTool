package com.TaskManagementTool.Models;

import java.util.Date;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String taskName;
    private TaskStatus taskStatus;
    private Date createdDate;
    private Date updatedDate;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "project_id")
    private Project project;

    public Task(String taskName, Project project){
        this.taskName = taskName;
        this.project = project;
    }

}
