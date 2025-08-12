package net.amit.journalApp.scheduler;

import net.amit.journalApp.cache.AppCache;
import net.amit.journalApp.entity.JournalEntry;
import net.amit.journalApp.entity.User;
import net.amit.journalApp.repository.UserRepoImpl;
import net.amit.journalApp.service.EmailService;
import net.amit.journalApp.service.SentimentAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserScheduler {

    @Autowired
    private EmailService emailService;
    @Autowired
    private UserRepoImpl userRepo;
    @Autowired
    private SentimentAnalysisService sentimentAnalysisService;
    @Autowired
    private AppCache appCache;

    @Scheduled(cron = "0 0 9 * * SUN") //cron expression means -> everyweek sunday 9 am
//    @Scheduled(cron = "0 * * ? * *")
    public void fetchUserAndSendSentimentAnalysisMail(){
        List<User> users = userRepo.getUserForSentimentAnalysis();
        for(User user : users){
            List<JournalEntry> entries = user.getJournalEntries();
            List<String> filteredEntries = entries.stream().filter(x -> x.getDate().isAfter(LocalDateTime.now().minus(7, ChronoUnit.DAYS))).map(x -> x.getContent()).collect(Collectors.toList());

            String joined = String.join(" ", filteredEntries);

            String sentiment = sentimentAnalysisService.getSentiment(joined);

            emailService.sendMail(user.getEmail(), "Sentiment analysis for last 7 days", sentiment);
        }
    }

    @Scheduled(cron = "0 0/10 * ? * *") //every 10 mins we are clearing in app cache
    public void clearAppCache(){
        appCache.init();
    }
}
