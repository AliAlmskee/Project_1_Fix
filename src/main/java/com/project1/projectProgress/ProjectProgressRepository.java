package com.project1.projectProgress;

import com.project1.project.data.Project;
import com.project1.project.data.ProjectStatus;
import com.project1.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectProgressRepository extends JpaRepository<Project, Long> {
    List<Project> findAllByClientOrWorker(User user, User user1);

    boolean deleteByIdAndStatusAndClient(Long projectId, ProjectStatus projectStatus, User client);
}