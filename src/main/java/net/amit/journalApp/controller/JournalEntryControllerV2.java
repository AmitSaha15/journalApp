package net.amit.journalApp.controller;

import net.amit.journalApp.entity.JournalEntry;
import net.amit.journalApp.entity.User;
import net.amit.journalApp.service.JournalEntryService;
import net.amit.journalApp.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/journal")
public class JournalEntryControllerV2 {

    @Autowired
    private JournalEntryService journalEntryService;
    @Autowired
    private UserService userService;

    @GetMapping("/{userName}")
    public ResponseEntity<?> getAllJournalEntriesOfUser(@PathVariable String userName){
        User user = userService.findByUserName(userName);
        List<JournalEntry> all = user.getJournalEntries();
        if(all != null && !all.isEmpty()){
            return new ResponseEntity<>(all, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/{userName}")
    public ResponseEntity<?> createEntry(@RequestBody JournalEntry newEntry, @PathVariable String userName){
        try {
            journalEntryService.saveEntry(newEntry, userName);
            return new ResponseEntity<>(newEntry, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("id/{journalId}")
    public ResponseEntity<JournalEntry> getJournalEntryById(@PathVariable ObjectId journalId){
        Optional<JournalEntry> journalEntry = journalEntryService.findById(journalId);
        if(journalEntry.isPresent()){
            return new ResponseEntity<>(journalEntry.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("id/{userName}/{journalId}")
    public ResponseEntity<?> deleteJournalEntryById(@PathVariable ObjectId journalId, @PathVariable String userName){
        journalEntryService.deleteById(journalId, userName);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("id/{userName}/{journalId}")
    public ResponseEntity<?> updateJournalEntryById(
            @PathVariable ObjectId journalId,
            @RequestBody JournalEntry updatedEntry,
            @PathVariable String userName
    ){
        JournalEntry old = journalEntryService.findById(journalId).orElse(null);

        if(old != null){
            old.setTitle(updatedEntry.getTitle() != null && !updatedEntry.getTitle().equals(" ") ? updatedEntry.getTitle() : old.getTitle());
            old.setContent(updatedEntry.getContent() != null && !updatedEntry.getContent().equals(" ") ? updatedEntry.getContent() : old.getContent());
            journalEntryService.saveEntry(old);
            return new ResponseEntity<>(old, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
