package com.china.hcg.eas.business.base.security;

import com.china.hcg.eas.business.model.User;
import com.china.hcg.eas.business.service.UserMng;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @autor hecaigui
 * @date 2019-11-30
 * @description
 */
@Component("userDetailsService")
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    UserMng userRepository;

    //load到了User对象后,框架会帮你匹配密码是否正确
    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        // 1. 查询用户
        User user = new User();
        user.setName(login);
        List<User> loginResults = userRepository.getUserByModel(user);

        User userFromDatabase = loginResults.get(0);
        if (userFromDatabase == null) {
            //log.warn("User: {} not found", login);
            throw new UsernameNotFoundException("User " + login + " was not found in db");
            //这里找不到必须抛异常
        }
        // 2. 设置角色
        Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(userFromDatabase.getDuty());
        grantedAuthorities.add(grantedAuthority);

        // 返回User对象
        return new org.springframework.security.core.userdetails.User(login,
                userFromDatabase.getPassword(), grantedAuthorities);

        //这个方法做了2件事情，查询用户以及设置角色，
        // 通常一个用户会有多个角色，即上面的userFromDatabase.getRole()通常是一个list，所以设置角色的时候，就是for循环new 多个SimpleGrantedAuthority并设置。
        // （本例为了简单没有设置角色表以及用户角色关联表，只在用户中增加了一个角色字段，所以grantedAuthorities只有一个）
    }
}