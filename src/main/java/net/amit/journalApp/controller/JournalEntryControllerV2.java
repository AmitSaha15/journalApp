package net.amit.journalApp.controller;

import net.amit.journalApp.entity.JournalEntry;
import net.amit.journalApp.service.JournalEntryService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/journal")
public class JournalEntryControllerV2 {

    @Autowired
    private JournalEntryService journalEntryService;

    @GetMapping
    public List<JournalEntry> getAll(){
        return journalEntryService.getAll();
    }

    @PostMapping
    public JournalEntry createEntry(@RequestBody JournalEntry newEntry){
        newEntry.setDate(LocalDateTime.now());
        journalEntryService.saveEntry(newEntry);
        return newEntry;
    }

    @GetMapping("id/{journalId}")
    public JournalEntry getJournalEntryById(@PathVariable ObjectId journalId){
        return journalEntryService.findById(journalId).orElse(null);
    }

    @DeleteMapping("id/{journalId}")
    public boolean deleteJournalEntryById(@PathVariable ObjectId journalId){
        journalEntryService.deleteById(journalId);
        return true;
    }

    @PutMapping("id/{journalId}")
    public JournalEntry updateJournalEntryById(@PathVariable ObjectId journalId, @RequestBody JournalEntry updatedEntry){
        JournalEntry old = journalEntryService.findById(journalId).orElse(null);

        if(old != null){
            old.setTitle(updatedEntry.getTitle() != null && !updatedEntry.getTitle().equals(" ") ? updatedEntry.getTitle() : old.getTitle());
            old.setContent(updatedEntry.getContent() != null && !updatedEntry.getContent().equals(" ") ? updatedEntry.getContent() : old.getContent());
        }
        journalEntryService.saveEntry(old);
        return old;
    }
}
