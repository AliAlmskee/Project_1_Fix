package com.project1.user;

import com.project1.config.JwtService;
import com.project1.project.ProjectRepository;
import com.project1.project.data.Project;
import com.project1.wallet.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;


    @Autowired
    private JwtService jwtService;


    public void changePassword(ChangePasswordRequest request, Principal connectedUser) {

        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        // check if the current password is correct
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new IllegalStateException("Wrong password");
        }
        // check if the two new passwords are the same
        if (!request.getNewPassword().equals(request.getConfirmationPassword())) {
            throw new IllegalStateException("Password are not the same");
        }

        // update the password
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));

        // save the new password
        userRepository.save(user);
    }

    public void addUserProject(int userId, Long projectId) {
        User user = userRepository.findById(userId).orElseThrow();
        Project project = projectRepository.findById(projectId).orElseThrow();
        if (!user.getProjects().contains(project)) {
            user.getProjects().add(project);
            project.getUsers().add(user);
            userRepository.save(user);
        }
    }

    public void addFavoriteUser(int userId, int favoriteUserId) {
        User user = userRepository.findById(userId).orElseThrow();
        User favoriteUser = userRepository.findById(favoriteUserId).orElseThrow();
        if (!user.getFavorites().contains(favoriteUser)) {
            user.getFavorites().add(favoriteUser);
            favoriteUser.getFavorites().add(user);
            userRepository.save(user);
        }
    }

    public void removeUserProject(int userId, Long projectId) {
        User user = userRepository.findById(userId).orElseThrow();
        Project project = projectRepository.findById(projectId).orElseThrow();
        user.getProjects().remove(project);
        project.getUsers().remove(user);
        userRepository.save(user);
    }

    public void removeFavoriteUser(int userId, int favoriteUserId) {
        User user = userRepository.findById(userId).orElseThrow();
        User favoriteUser = userRepository.findById(favoriteUserId).orElseThrow();
        user.getFavorites().remove(favoriteUser);
        favoriteUser.getFavorites().remove(user);
        userRepository.save(user);
    }


    public List<User> getFavoriteUser(int userId) {
        User user = userRepository.findById(userId).orElseThrow();
        return user.getFavorites();
    }

    public List<Project> getFavoriteProject(int userId) {
        User user = userRepository.findById(userId).orElseThrow();
        return user.getProjects();
    }







}
