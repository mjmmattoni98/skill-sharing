package com.losacabaos.skillsharing.request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Repository
public class RequestDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource ds) {
        jdbcTemplate = new JdbcTemplate(ds);
    }

    public void addRequest(Request request) throws DuplicateKeyException {
        jdbcTemplate.update("INSERT INTO request(name, level, username, start_date, end_date, description, canceled) " +
                        "VALUES (?,?::skill_level,?,?,?,?)",
                request.getName(),
                request.getLevel(),
                request.getUsername(),
                request.getStartDate(),
                request.getEndDate(),
                request.getDescription(),
                request.isCanceled()
        );
    }

    public void deleteRequest(Request request) {
        jdbcTemplate.update("DELETE FROM request WHERE id = ?",
                request.getId()
        );
    }

    public void deleteRequest(int id) {
        jdbcTemplate.update("DELETE FROM request WHERE id = ?",
                id
        );
    }

    public void updateRequest(Request request) {
        jdbcTemplate.update("UPDATE request SET start_date = ?, end_date = ?, description = ?, canceled = ? WHERE id = ?",
                request.getStartDate(),
                request.getEndDate(),
                request.getDescription(),
                request.isCanceled(),
                request.getId()
        );
    }

    public Request getRequest(int id) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM request WHERE id = ?",
                    new RequestRowMapper(),
                    id
            );
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public List<Request> getRequests() {
        try {
            return jdbcTemplate.query("SELECT * FROM request WHERE canceled = false " +
                            "AND (end_date IS NULL OR end_date >= CURRENT_DATE)",
                    new RequestRowMapper()
            );
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<>();
        }
    }

    public List<Request> getRequestsByUsername(String username) {
        try {
            return jdbcTemplate.query("SELECT * FROM request WHERE canceled = false AND LOWER(username) LIKE ? " +
                            "AND (end_date IS NULL OR end_date >= CURRENT_DATE)",
                    new RequestRowMapper(),
                    "%" + username.toLowerCase() + "%"
            );
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<>();
        }
    }

    public List<Request> getRequestsStudent(String username) {
        try {
            return jdbcTemplate.query("SELECT * FROM request WHERE username = ? AND canceled = false AND " +
                            "(end_date IS NULL OR end_date >= CURRENT_DATE)",
                    new RequestRowMapper(),
                    username
            );
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<>();
        }
    }

    public List<Request> getRequestsStudentBySkill(String username, String skill) {
        try {
            return jdbcTemplate.query("SELECT * FROM request WHERE LOWER(name) LIKE ? AND username = ? " +
                            "AND canceled = false AND (end_date IS NULL OR end_date >= CURRENT_DATE)",
                    new RequestRowMapper(),
                    "%" + skill.toLowerCase() + "%",
                    username
            );
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<>();
        }
    }

    public List<Request> getRequestsSkill(String name, String level) {
        try {
            return jdbcTemplate.query("SELECT * FROM request WHERE name = ? AND level = ?::skill_level AND canceled = false AND " +
                            "(end_date IS NULL OR end_date >= CURRENT_DATE)",
                    new RequestRowMapper(),
                    name,
                    level
            );
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<>();
        }
    }

    public List<Request> getRequestsSkillByUsername(String name, String level, String username) {
        try {
            return jdbcTemplate.query("SELECT * FROM request WHERE name = ? AND level = ?::skill_level AND canceled = false AND " +
                            "(end_date IS NULL OR end_date >= CURRENT_DATE) AND LOWER(username) LIKE ?",
                    new RequestRowMapper(),
                    name,
                    level,
                    "%" + username.toLowerCase() + "%"
            );
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<>();
        }
    }

    public List<Request> getRequestsSkillNotCollaborating(String name) {
        try {
            return jdbcTemplate.query("SELECT * FROM request WHERE name = ? AND canceled = false AND " +
                            "id NOT IN (SELECT id_request FROM collaboration) " +
                            "AND (end_date IS NULL OR end_date >= CURRENT_DATE)",
                    new RequestRowMapper(),
                    name
            );
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<>();
        }
    }

    public List<Request> fetchLastThreeRequests(String name) {
        try {
            return jdbcTemplate.query("SELECT * FROM request WHERE username = ? AND canceled = false " +
                            "AND (end_date IS NULL OR end_date >= CURRENT_DATE) " +
                            "ORDER BY id DESC LIMIT 3",
                    new RequestRowMapper(),
                    name
            );
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<>();
        }
    }
}
