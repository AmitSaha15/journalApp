package net.amit.journalApp.controller;

import net.amit.journalApp.entity.JournalEntry;
import net.amit.journalApp.service.JournalEntryService;
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

    @GetMapping
    public ResponseEntity<?> getAll(){
        List<JournalEntry> all = journalEntryService.getAll();
        if(all != null && !all.isEmpty()){
            return new ResponseEntity<>(all, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<?> createEntry(@RequestBody JournalEntry newEntry){
        try {
            newEntry.setDate(LocalDateTime.now());
            journalEntryService.saveEntry(newEntry);
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

    @DeleteMapping("id/{journalId}")
    public ResponseEntity<?> deleteJournalEntryById(@PathVariable ObjectId journalId){
        journalEntryService.deleteById(journalId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("id/{journalId}")
    public ResponseEntity<?> updateJournalEntryById(@PathVariable ObjectId journalId, @RequestBody JournalEntry updatedEntry){
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
