package net.amit.journalApp.scheduler;

import net.amit.journalApp.cache.AppCache;
import net.amit.journalApp.entity.JournalEntry;
import net.amit.journalApp.entity.User;
import net.amit.journalApp.enums.Sentiments;
import net.amit.journalApp.repository.UserRepoImpl;
import net.amit.journalApp.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class UserScheduler {

    @Autowired
    private EmailService emailService;
    @Autowired
    private UserRepoImpl userRepo;
    @Autowired
    private AppCache appCache;

    @Scheduled(cron = "0 0 9 * * SUN") //cron expression means -> everyweek sunday 9 am
//    @Scheduled(cron = "0 * * ? * *")
    public void fetchUsersAndSendSaMail() {
        List<User> users = userRepo.getUserForSentimentAnalysis();
        for (User user : users) {
            List<JournalEntry> journalEntries = user.getJournalEntries();
            List<Sentiments> sentiments = journalEntries.stream().filter(x -> x.getDate().isAfter(LocalDateTime.now().minus(7, ChronoUnit.DAYS))).map(x -> x.getSentiments()).collect(Collectors.toList());
            Map<Sentiments, Integer> sentimentCounts = new HashMap<>();
            for (Sentiments sentiment : sentiments) {
                if (sentiment != null)
                    sentimentCounts.put(sentiment, sentimentCounts.getOrDefault(sentiment, 0) + 1);
            }
            Sentiments mostFrequentSentiment = null;
            int maxCount = 0;
            for (Map.Entry<Sentiments, Integer> entry : sentimentCounts.entrySet()) {
                if (entry.getValue() > maxCount) {
                    maxCount = entry.getValue();
                    mostFrequentSentiment = entry.getKey();
                }
            }
            if (mostFrequentSentiment != null) {
                emailService.sendMail(user.getEmail(), "Your Mood for Last 7 Days", mostFrequentSentiment.toString());
            }
        }
    }

    @Scheduled(cron = "0 0/10 * ? * *") //every 10 mins we are clearing in app cache
    public void clearAppCache(){
        appCache.init();
    }
}
