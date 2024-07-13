package com.project1.profile;
import com.project1.category.Category;
import com.project1.jobTitle.JobTitle;
import com.project1.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class WorkerProfile {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user ;

    String bio ;

    double rate ;

    boolean is_verified;

    @ManyToOne
    @JoinColumn(name = "category_id")
    Category category ;

    @ManyToOne
    @JoinColumn(name = "jobTitle_id")
    JobTitle jobTitle ;

}
