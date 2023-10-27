package com.ead.authuser.services.implement;

import com.ead.authuser.clients.CourseClient;
import com.ead.authuser.models.UserCourseModel;
import com.ead.authuser.repositories.UserCourseRepository;
import com.ead.authuser.services.UserService;
import com.ead.authuser.models.UserModel;
import com.ead.authuser.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImplement implements UserService {

    private final UserRepository userRepository;
    private final UserCourseRepository userCourseRepository;
    private final CourseClient courseClient;

    @Autowired
    public UserServiceImplement(UserRepository userRepository, UserCourseRepository userCourseRepository, CourseClient courseClient) {
        this.userRepository = userRepository;
        this.userCourseRepository = userCourseRepository;
        this.courseClient = courseClient;
    }

    @Override
    public List<UserModel> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Optional<UserModel> findById(UUID userId) {
        return this.userRepository.findById(userId);
    }

    @Override
    public void delete(UserModel userModel) {
        boolean deleteUserCourseInCourse = false;
        List<UserCourseModel> userCourseModelList = this.userCourseRepository.findAllUserCourseIntoUser(userModel.getUserId());
        if(!userCourseModelList.isEmpty()) {
            this.userCourseRepository.deleteAll(userCourseModelList);
            deleteUserCourseInCourse = true;
        }
        this.userRepository.delete(userModel);
        if(deleteUserCourseInCourse) {
            this.courseClient.deleteUserInCourse(userModel.getUserId());
        }
    }

    @Override
    public void save(UserModel userModel) {
        this.userRepository.save(userModel);
    }

    @Override
    public boolean existsByUserName(String userName) {
        return this.userRepository.existsByUserName(userName);
    }

    @Override
    public boolean existsByEmail(String email) {
        return this.userRepository.existsByEmail(email);
    }

    @Override
    public Page<UserModel> findAll(Specification<UserModel> spec, Pageable pageable) {
        return this.userRepository.findAll(spec, pageable);
    }

}
