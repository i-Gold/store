package com.company.storehouse.service.impl;

import com.company.storehouse.model.User;
import com.company.storehouse.repository.UserRepository;
import com.company.storehouse.service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RegistrationServiceImpl implements RegistrationService {

    private UserRepository userRepository;
    private JavaMailSender javaMailSender;

    @Override
    public void createUser(User user) {
        user.setEnabled(true);
        userRepository.save(user);
        sendMessageTo(user);
    }

    private void sendMessageTo(User user) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject("Registration at Grocery Storehouse");
        message.setText("Congratulations, " + user.getUsername() + "!\n\nYou are successfully registered");
        javaMailSender.send(message);
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setJavaMailSender(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }
}
