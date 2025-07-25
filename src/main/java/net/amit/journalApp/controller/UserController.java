package net.amit.journalApp.controller;

import net.amit.journalApp.api.response.QuoteResponse;
import net.amit.journalApp.api.response.WeatherResponse;
import net.amit.journalApp.entity.User;
import net.amit.journalApp.repository.UserRepo;
import net.amit.journalApp.service.QuoteService;
import net.amit.journalApp.service.UserService;
import net.amit.journalApp.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private WeatherService weatherService;
    @Autowired
    private QuoteService quoteService;

    @PutMapping
    public ResponseEntity<?> updateUser(@RequestBody User user){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();

        User userInDB = userService.findByUserName(userName);
        userInDB.setUserName(user.getUserName());
        userInDB.setPassword(user.getPassword());
        userService.saveNewUser(userInDB);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        userRepo.deleteByUserName(authentication.getName());

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping
    public ResponseEntity<?> greeting(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        WeatherResponse weatherResponse = weatherService.getWeather("Kolkata");
        QuoteResponse quoteResponse = quoteService.getQuote();
        String greeting = "";

        if(weatherResponse != null && quoteResponse != null){
            greeting = ", Weather feels like " + weatherResponse.getCurrent().getFeelsLike() + ". Your quote of the day: " + quoteResponse.getQuote();
        }
        return new ResponseEntity<>("Hi "+ authentication.getName() + greeting,HttpStatus.OK);
    }
}
