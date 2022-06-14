package com.losacabaos.skillsharing.skill;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SkillRowMapper implements RowMapper<Skill> {
    @Override
    public Skill mapRow(ResultSet rs, int rowNum) throws SQLException {
        Skill skill = new Skill();
        skill.setName(rs.getString("name"));
        skill.setDescription(rs.getString("description"));
        skill.setLevel(rs.getString("level"));
        skill.setCanceled(rs.getBoolean("canceled"));

        return skill;
    }
}
