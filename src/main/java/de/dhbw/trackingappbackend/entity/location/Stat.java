package de.dhbw.trackingappbackend.entity.location;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "base_stats")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Stat {

    @Id
    private String id;

    private String laenderKuerzel;

    private String title;

    private float percentage;
}
