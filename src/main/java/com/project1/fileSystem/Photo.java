package com.project1.fileSystem;

import com.project1.profile.ClientProfile;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Photo {
    @Id
    @GeneratedValue
    private Long id;
    String photo ;

    @ManyToMany(mappedBy = "photos")
    private List<ClientProfile> clientProfiles;

}
