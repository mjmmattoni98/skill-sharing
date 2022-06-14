package com.losacabaos.skillsharing.skill;

import lombok.Data;

@Data
public class Skill implements Comparable<Skill> {
    private String name;
    private String description;
    private SkillLevel level;
    private boolean canceled = false;

    public void setLevel(String level) {
        this.level = SkillLevel.fromId(level);
    }

    public String getLevel() {
        if (this.level == null)
            return null;
        return this.level.getId();
    }

    public String getName() {
        if (this.name == null)
            return null;
        return this.name.toUpperCase();
    }

    public void setName(String name) {
        this.name = name.toUpperCase();
    }

    @Override
    public int compareTo(Skill o) {
        return this.getName().toLowerCase().compareTo(o.getName().toLowerCase());
    }

}
