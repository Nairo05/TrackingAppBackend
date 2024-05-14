package de.dhbw.trackingappbackend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "base_achievements")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Achievement {

    @Id
    public String id;

    private String title;

    private String description;

    // TODO add field for kuerzel or specific tile
}
