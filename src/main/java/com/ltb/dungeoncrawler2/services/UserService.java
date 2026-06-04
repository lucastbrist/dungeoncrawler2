package com.ltb.dungeoncrawler2.services;

import com.ltb.dungeoncrawler2.models.User;
import com.ltb.dungeoncrawler2.models.dto.UserResponse;
import com.ltb.dungeoncrawler2.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepo;

    public UserResponse createUser() {
        User user = userRepo.save(new User());
        return new UserResponse(user.getId());
    }
}