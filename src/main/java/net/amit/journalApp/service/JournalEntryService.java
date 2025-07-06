package net.amit.journalApp.service;

import net.amit.journalApp.entity.JournalEntry;
import net.amit.journalApp.entity.User;
import net.amit.journalApp.repository.JournalEntryRepo;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class JournalEntryService {

    @Autowired
    private JournalEntryRepo journalEntryRepo;
    @Autowired
    private UserService userService;

    @Transactional
    public void saveEntry(JournalEntry entry, String userName){
        try {
            User user = userService.findByUserName(userName);
            entry.setDate(LocalDateTime.now());
            JournalEntry saved = journalEntryRepo.save(entry);
            user.getJournalEntries().add(saved);
//            user.setUserName(null);
            userService.saveEntry(user);
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while saving the entry.",e);
        }
    }

    public void saveEntry(JournalEntry entry){
        entry.setDate(LocalDateTime.now());
        journalEntryRepo.save(entry);
    }

    public List<JournalEntry> getAll(){
        return journalEntryRepo.findAll();
    }

    public Optional<JournalEntry> findById(ObjectId id){
        return journalEntryRepo.findById(id);
    }

    public void deleteById(ObjectId id, String userName){
        User user = userService.findByUserName(userName);
        user.getJournalEntries().removeIf(x -> x.getId().equals(id));
        userService.saveEntry(user);
        journalEntryRepo.deleteById(id);
    }
}
