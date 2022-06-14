package com.losacabaos.skillsharing.offer;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class OfferRowMapper implements RowMapper<Offer> {
    @Override
    public Offer mapRow(ResultSet rs, int rowNum) throws SQLException {
        Offer offer = new Offer();
        offer.setId(rs.getInt("id"));
        offer.setUsername(rs.getString("username"));
        offer.setName(rs.getString("name"));
        offer.setLevel(rs.getString("level"));
        offer.setDescription(rs.getString("description"));
        offer.setStartDate(rs.getObject("start_date", LocalDate.class));
        offer.setEndDate(rs.getObject("end_date", LocalDate.class));
        offer.setCanceled(rs.getBoolean("canceled"));

        return offer;
    }
}
