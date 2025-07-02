package net.amit.journalApp.controller;

import net.amit.journalApp.entity.JournalEntry;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/_journal")
public class JournalEntryController {

    Map<Long, JournalEntry> journalEntries = new HashMap<>();

    @GetMapping
    public List<JournalEntry> getAll(){
        return new ArrayList<>(journalEntries.values());
    }

    @PostMapping
    public boolean createEntry(@RequestBody JournalEntry newEntry){
        journalEntries.put(newEntry.getId(), newEntry);
        return true;
    }

    @GetMapping("id/{journalId}")
    public JournalEntry getJournalEntryById(@PathVariable Long journalId){
        return journalEntries.get(journalId);
    }

    @DeleteMapping("id/{journalId}")
    public boolean deleteJournalEntryById(@PathVariable Long journalId){
        journalEntries.remove(journalId);
        return true;
    }

    @PutMapping("id/{journalId}")
    public String updateJournalEntryById(@PathVariable Long journalId, @RequestBody JournalEntry updatedEntry){
        journalEntries.put(journalId, updatedEntry);
        return "Journal updated!";
    }
}
