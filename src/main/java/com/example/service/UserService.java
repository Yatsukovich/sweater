package com.example.service;

import com.example.domain.Role;
import com.example.domain.User;
import com.example.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private MailSender mailSender;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepo.findByUsername(username);
    }
    public boolean addUser(User user){
        User userFromDB = userRepo.findByUsername(user.getUsername());

        if (userFromDB!=null){
            return false;
        }
        user.setActive(false);
        user.setRoles(Collections.singleton(Role.USER));

        user.setActivationCode(UUID.randomUUID().toString());
        userRepo.save(user);


        sendMessage(user);
        return true;
    }

    private void sendMessage(User user) {
        if (!StringUtils.isEmpty(user.getEmail())) {
            String message = String.format(
                    "Hello, %s! \n" +
                            "Welcome to Sweater. Please, visit next link: http://localhost:8080/activate/%s",
                    user.getUsername(),
                    user.getActivationCode()
            );

          mailSender.send(user.getEmail(), "Activation code", message);
        }
    }

    public boolean activateUser(String code) {
        User user = userRepo.findByActivationCode(code);
        if (user==null){
            return false;
        }
        user.setActivationCode(null);
        userRepo.save(user);
        user.setActive(true);
        return true;
    }

    public List<User> findAll() {
        return userRepo.findAll();
    }

    public void saveUser(User user, String username, Map<String, String> form) {
        user.setUsername(username);

        Set<String> roles = Arrays.stream(Role.values())
                .map(Role::name)
                .collect(Collectors.toSet());

        user.getRoles().clear();

        for (String key : form.keySet()) {
            if (roles.contains(key)) {
                user.getRoles().add(Role.valueOf(key));
            }
        }

        userRepo.save(user);
    }

    public void updateProfile(User user, String password, String mail) {
        String userEmail= user.getEmail();
        boolean isEmailChanged = (mail !=null && !mail.equals(userEmail)
                ||(userEmail!=null && !userEmail.equals(mail)));
        if (isEmailChanged){
            user.setEmail(mail);

            if (!StringUtils.isEmpty(mail)){
                user.setActivationCode(UUID.randomUUID().toString());
            }
            if (!StringUtils.isEmpty(password)){
                user.setPassword(password);
            }
        }
        userRepo.save(user);
        if (isEmailChanged)
        sendMessage(user);{
        }
    }
}
