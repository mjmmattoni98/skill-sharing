package com.losacabaos.skillsharing.offer;

import com.losacabaos.skillsharing.skill.SkillLevel;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class Offer implements Comparable<Offer> {
    private int id;
    private String username;
    private String name;
    private SkillLevel level;
    private String description;
    @DateTimeFormat(iso=DateTimeFormat.ISO.DATE)
    private LocalDate startDate;
    @DateTimeFormat(iso=DateTimeFormat.ISO.DATE)
    private LocalDate endDate = null;
    private boolean canceled = false;
    private boolean fromSkill;

    public void setLevel(String level) {
        this.level = SkillLevel.fromId(level);
    }

    public String getLevel() {
        if (this.level == null)
            return null;
        return this.level.getId();
    }

    @Override
    public int compareTo(Offer o) {
        return this.getName().toLowerCase().compareTo(o.getName().toLowerCase());
    }
}
