package fpt.project.bsmart.service.Impl;


import fpt.project.bsmart.entity.Role;
import fpt.project.bsmart.entity.User;
import fpt.project.bsmart.entity.common.ApiException;
import fpt.project.bsmart.entity.request.CreateAccountRequest;
import fpt.project.bsmart.repository.RoleRepository;
import fpt.project.bsmart.repository.UserRepository;
import fpt.project.bsmart.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserServiceImpl  {





//    @Override
//    public Integer saveUser(CreateAccountRequest createAccountRequest) {
//        User user = new User();
//        user.setUsername(createAccountRequest.getUsername());
//        user.setPassword(bCryptEncoder.encode(createAccountRequest.getPassword()));
//        user.setEmail(createAccountRequest.getEmail());
//        user.setAddress(createAccountRequest.getAddress());
//        user.setBirthday(createAccountRequest.getBirthday());
//        user.setPhone(createAccountRequest.getPhone());
//        user.setFullName(createAccountRequest.getFullName());
//        List<Role> roleList = new ArrayList<>();
//        Role role = roleRepo.findRoleByCode(createAccountRequest.getRole())
//                .orElseThrow(() -> ApiException.create(HttpStatus.NOT_FOUND).withMessage("Khong tim thay role"));
//        roleList.add(role);
//        user.setRoles(roleList);
//
//        return userRepo.save(user).getId();
//    }

//    @Override
//    public Optional<User> findByUsername(String username) {
//        return userRepo.findByUsername(username);
//    }

//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        Optional<User> opt = userRepo.findByUsername(username);
//        org.springframework.security.core.userdetails.User springUser = null;
//        if (!opt.isPresent()) {
//            throw new UsernameNotFoundException("User with username: " + username + " not found");
//        } else {
//            User user = opt.get();    //retrieving user from DB
//            List<Role> roles = user.getRoles();
//            Set<GrantedAuthority> ga = new HashSet<>();
//            for (Role role : roles) {
//                ga.add(new SimpleGrantedAuthority(role.getCode().toString()));
//            }
//
//            springUser = new org.springframework.security.core.userdetails.User(
//                    username,
//                    user.getPassword(),
//                    ga);
//        }
//
//        return springUser;
//    }
}
