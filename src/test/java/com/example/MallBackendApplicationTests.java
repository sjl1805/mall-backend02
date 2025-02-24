package com.example;

import com.example.model.entity.Users;
import com.example.service.impl.UsersServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MallBackendApplicationTests {
    UsersServiceImpl  usersService;
    @Test
    void contextLoads() {
    }

    @Test
    void test() {
            usersService.list();
 }
}
