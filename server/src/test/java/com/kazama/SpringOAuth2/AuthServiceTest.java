package com.kazama.SpringOAuth2;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("dev")
public class AuthServiceTest {

    @Test
    public void test01() {
        int result = 4 + 3;
        assertEquals(7, result, "Addition result should be 7");
    }
}
