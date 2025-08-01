package net.amit.journalApp.entity;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    private ObjectId id;

    @Indexed(unique = true) //userName should be unique
    @NonNull
    private String userName;

    @NonNull
    private String password;

    // @DBRef is used for creating references between documents in your MongoDB database
    @DBRef
    List<JournalEntry> journalEntries = new ArrayList<>();

    private List<String> roles;
}
