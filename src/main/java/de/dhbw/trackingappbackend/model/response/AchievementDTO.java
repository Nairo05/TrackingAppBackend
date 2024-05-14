package de.dhbw.trackingappbackend.model.response;

import de.dhbw.trackingappbackend.entity.Achievement;
import lombok.Data;

@Data
public class AchievementDTO {

    public AchievementDTO(Achievement achievement) {
        this.title = achievement.getTitle();
        this.description = achievement.getDescription();
        this.achieved = false;
    }

    private String title;

    private String description;

    private boolean achieved;

    public void setAchieved() {
        this.achieved = true;
    }
}
