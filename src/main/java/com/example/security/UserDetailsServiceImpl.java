package com.example.security;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.mapper.UsersMapper;
import com.example.model.entity.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UsersMapper usersMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users users = usersMapper.selectOne(new LambdaQueryWrapper<Users>().eq(Users::getUsername, username));
        if (users == null) {
            throw new UsernameNotFoundException("用户不存在: " + username);
        }

        return new AuthUserDetails(
                users.getId(),
                users.getUsername(),
                users.getPassword(),
                users.getStatus() == 1,
                users.getRole()
        );
    }

    // 自定义UserDetails实现
    public record AuthUserDetails(
            Long userId,
            String username,
            String password,
            boolean enabled,
            Integer role
    ) implements UserDetails {

        @Override
        public String getPassword() {
            return password;
        }

        @Override
        public String getUsername() {
            return username;
        }

        @Override
        public boolean isAccountNonExpired() {
            return true;
        }

        @Override
        public boolean isAccountNonLocked() {
            return enabled;
        }

        @Override
        public boolean isCredentialsNonExpired() {
            return true;
        }

        @Override
        public boolean isEnabled() {
            return enabled;
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return switch (role) {
                case 9 -> List.of(new SimpleGrantedAuthority("ROLE_ADMIN"));
                case 2 -> List.of(new SimpleGrantedAuthority("ROLE_STAFF"));
                default -> List.of(new SimpleGrantedAuthority("ROLE_USER"));
            };
        }
    }
} 