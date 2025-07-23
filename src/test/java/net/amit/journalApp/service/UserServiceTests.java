package net.amit.journalApp.service;

import net.amit.journalApp.entity.User;
import net.amit.journalApp.repository.UserRepo;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class UserServiceTests {

    @Autowired
    private UserRepo userRepo;

    @ParameterizedTest
//    @CsvSource({
//            "amit",
//            "shantanu",
//            "shivam"
//    })
    @ValueSource(strings = {
            "amit",
            "shantanu"
    })
    public void testFindByUsername(String userName){
        assertNotNull(userRepo.findByUserName(userName));
    }

    @Disabled
    @ParameterizedTest
    @CsvSource({
            "1,1,2",
            "5,23,28"
    })
    public void test(int a, int b, int expected){
        assertEquals(expected, a+b);
    }
}
