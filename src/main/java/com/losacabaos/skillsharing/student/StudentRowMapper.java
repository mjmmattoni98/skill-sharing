package com.losacabaos.skillsharing.student;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class StudentRowMapper implements RowMapper<Student> {
    @Override
    public Student mapRow(ResultSet rs, int rowNum) throws SQLException {
        Student student = new Student();
        student.setName(rs.getString("name"));
        student.setSurname(rs.getString("surname"));
        student.setEmail(rs.getString("email"));
        student.setPassword(rs.getString("password"));
        student.setUsername(rs.getString("username"));
        student.setBalanceHours(rs.getInt("balance_hours"));
        student.setBlocked(rs.getBoolean("blocked"));
        student.setSkp(rs.getBoolean("skp"));
        student.setStreet(rs.getString("street"));
        student.setLocality(rs.getString("locality"));
        student.setDegree(rs.getString("degree"));
        student.setNumber(rs.getInt("number"));
        student.setPc(rs.getInt("pc"));
        student.setConfirmPassword(student.getPassword());
        student.setOldPassword(student.getPassword());

        return student;
    }
}
