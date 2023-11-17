package com.ead.authuser.services.implement;

import com.ead.authuser.clients.CourseClient;
import com.ead.authuser.enums.ActionType;
import com.ead.authuser.publishers.UserEventPublisher;
import com.ead.authuser.services.UserService;
import com.ead.authuser.models.UserModel;
import com.ead.authuser.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImplement implements UserService {

    private final UserRepository userRepository;
    private final CourseClient courseClient;
    private final UserEventPublisher userEventPublisher;

    @Autowired
    public UserServiceImplement(UserRepository userRepository, CourseClient courseClient, UserEventPublisher userEventPublisher) {
        this.userRepository = userRepository;
        this.courseClient = courseClient;
        this.userEventPublisher = userEventPublisher;
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
        this.userRepository.delete(userModel);
    }

    @Override
    public UserModel save(UserModel userModel) {
        return this.userRepository.save(userModel);
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

    @Transactional
    @Override
    public UserModel saveUser(UserModel userModel) {
        userModel = save(userModel);
        this.userEventPublisher.publishUserEvent(userModel.convertToUserEventDto(), ActionType.CREATE);
        return userModel;
    }

}
