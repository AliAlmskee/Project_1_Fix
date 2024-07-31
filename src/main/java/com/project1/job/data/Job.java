package com.project1.job.data;

import com.project1.fileSystem.Doc;
import com.project1.fileSystem.Photo;
import com.project1.fileSystem.Video;
import com.project1.profile.WorkerProfile;
import com.project1.skill.Skill;
import com.project1.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "workerProfileId")
    private WorkerProfile workerProfile;


    private String name;
    private String description;
    private Date date;

    @Column(columnDefinition = "integer default 0")
    private int viewsNo;

    @Column(columnDefinition = "integer default 0")
    private int likeNo;


    @ManyToMany
    @JoinTable(
            name = "job_view",
            joinColumns = @JoinColumn(name = "job_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "job_id"})
    )
    private List<User> viewedBy;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "job_like",
            joinColumns = @JoinColumn(name = "job_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "job_id"})
    )
    private List<User> likedBy;
    @ManyToMany
    @JoinTable(
            name = "job_photo",
            joinColumns = @JoinColumn(name = "job_id"),
            inverseJoinColumns = @JoinColumn(name = "photo_id")
    )
    private List<Photo> photos;
    @ManyToMany
    @JoinTable(
            name = "job_video",
            joinColumns = @JoinColumn(name = "job_id"),
            inverseJoinColumns = @JoinColumn(name = "video_id")
    )
    private List<Video> videos;
    @ManyToMany
    @JoinTable(
            name = "job_doc",
            joinColumns = @JoinColumn(name = "job_id"),
            inverseJoinColumns = @JoinColumn(name = "doc_id")
    )
    private List<Doc> docs;

    @ManyToMany
    @JoinTable(
            name = "job_skill",
            joinColumns = @JoinColumn(name = "job_id"),
            inverseJoinColumns = @JoinColumn(name = "skill_id")
    )
    private List<Skill> skills;



}
