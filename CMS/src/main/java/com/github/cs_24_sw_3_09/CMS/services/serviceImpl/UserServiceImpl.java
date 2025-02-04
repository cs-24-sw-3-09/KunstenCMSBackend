package com.github.cs_24_sw_3_09.CMS.services.serviceImpl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.github.cs_24_sw_3_09.CMS.model.entities.UserEntityDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.github.cs_24_sw_3_09.CMS.model.entities.UserEntity;
import com.github.cs_24_sw_3_09.CMS.repositories.UserRepository;
import com.github.cs_24_sw_3_09.CMS.services.UserService;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<UserEntity> save(UserEntity user) {
        for (UserEntity userToCmp : findAll()) {
            if (user.getEmail().equals(userToCmp.getEmail())) return Optional.empty();
        }
        
        return Optional.of(userRepository.save(user));
    }

    @Override
    public UserEntity forceSave(UserEntity user) {
        return userRepository.save(user);
    }

    @Override
    public List<UserEntity> findAll() {
        // return itterable, so we convert it to list.
        return StreamSupport.stream(userRepository.findAll().spliterator(), false).collect(Collectors.toList());
    }

    @Override
    public Page<UserEntity> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    public Optional<UserEntity> findOne(Long id) {
        return userRepository.findById(Math.toIntExact(id));
    }

    @Override
    public boolean isExists(Long id) {
        return userRepository.existsById(Math.toIntExact(id));
    }

    @Override
    public UserEntity partialUpdate(Long id, UserEntity userEntity) {
        // Set the ID of the incoming userEntity object.
        userEntity.setId(Math.toIntExact(id));
        return userRepository.findById(Math.toIntExact(id)).map(existingUserEntity -> {
            // Check if each field is non-null and update it if present in the incoming
            // request object
            Optional.ofNullable(userEntity.getFirstName()).ifPresent(existingUserEntity::setFirstName);
            Optional.ofNullable(userEntity.getLastName()).ifPresent(existingUserEntity::setLastName);
            Optional.ofNullable(userEntity.getEmail()).ifPresent(existingUserEntity::setEmail);
            Optional.ofNullable(userEntity.getPassword()).ifPresent(password -> existingUserEntity.setPassword(encoder.encode(password)));
            Optional.ofNullable(userEntity.getPauseNotificationStart())
                    .ifPresent(existingUserEntity::setPauseNotificationStart);
            Optional.ofNullable(userEntity.getPauseNotificationEnd())
                    .ifPresent(existingUserEntity::setPauseNotificationEnd);
            Optional.ofNullable(userEntity.getNotificationState()).ifPresent(existingUserEntity::setNotificationState);
            Optional.ofNullable(userEntity.getMediaPlanner()).ifPresent(existingUserEntity::setMediaPlanner);
            Optional.ofNullable(userEntity.getAdmin()).ifPresent(existingUserEntity::setAdmin);
            return userRepository.save(existingUserEntity);
        }).orElseThrow(() -> new RuntimeException("User does not exist"));
    }

    @Override
    public void delete(Long id) {
        userRepository.deleteById(Math.toIntExact(id));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserEntity> user = userRepository.findByEmail(username);
        return user.map(UserEntityDetails::new).orElseThrow(() -> new UsernameNotFoundException("Email not found: " + username));
    }

    public Optional<UserEntity> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public boolean existsByAdmin() {
        return userRepository.existsByAdmin(true);
    }

    public Optional<UserEntity> getLoggedInUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String loggedInUserEmail = null;

        if (principal instanceof UserDetails) {
            loggedInUserEmail = ((UserDetails) principal).getUsername();
            return userRepository.findByEmail(loggedInUserEmail);

        } else {
            return null;
        }
    }
}
