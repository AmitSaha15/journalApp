package net.amit.journalApp.controller;

import net.amit.journalApp.entity.JournalEntry;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/journal")
public class JournalEntryControllerV2 {

    @GetMapping
    public List<JournalEntry> getAll(){
        return null;
    }

    @PostMapping
    public boolean createEntry(@RequestBody JournalEntry newEntry){
        return true;
    }

    @GetMapping("id/{journalId}")
    public JournalEntry getJournalEntryById(@PathVariable Long journalId){
        return null;
    }

    @DeleteMapping("id/{journalId}")
    public boolean deleteJournalEntryById(@PathVariable Long journalId){
        return true;
    }

    @PutMapping("id/{journalId}")
    public String updateJournalEntryById(@PathVariable Long journalId, @RequestBody JournalEntry updatedEntry){
        return "Journal updated!";
    }
}
