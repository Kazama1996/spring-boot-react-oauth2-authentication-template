package com.kazama.SpringOAuth2.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import javax.naming.spi.DirStateFactory.Result;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("dev")
@SpringBootTest
public class MyServiceTest {

    @Test
    public void test001() {
        int res = 6 + 6;
        assertEquals(12, res, "res must be 13 but" + res);
    }

}
