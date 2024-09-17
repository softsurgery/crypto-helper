package com.zc.cryptohelper.crypto_helper.bootstrap;

import com.zc.cryptohelper.crypto_helper.models.Role;
import com.zc.cryptohelper.crypto_helper.models.User;
import com.zc.cryptohelper.crypto_helper.repository.RoleRepository;
import com.zc.cryptohelper.crypto_helper.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DevBootstrap implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        initRoles();
        initUsers();
    }

    private void initRoles(){
        if (!roleRepository.findByName("Administrator").isPresent()){
            Role role = new Role();
            role.setName("Administrator");
            role.setDescription("Most privileged User");
            roleRepository.save(role);
        }
        if (!roleRepository.findByName("Standard").isPresent()){
            Role role = new Role();
            role.setName("Standard");
            role.setDescription("Standard User");
            roleRepository.save(role);
        }
    }

    private void initUsers(){
        if (!userRepository.findByUsername("admin").isPresent()){
            User user = new User();
            user.setEmail("admin@gmail.com");
            user.setUsername("admin");
            user.setPassword(passwordEncoder.encode("123456"));
            user.setRole(roleRepository.findByName("Administrator").get());
            userRepository.save(user);
        }
    }
}
