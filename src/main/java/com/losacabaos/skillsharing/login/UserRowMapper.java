package com.losacabaos.skillsharing.login;


import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRowMapper implements RowMapper<InternalUser> {
    public InternalUser mapRow(ResultSet rs, int i) throws SQLException {
        InternalUser internalUser = new InternalUser();
        internalUser.setUsername(rs.getString("username"));
        internalUser.setPassword(rs.getString("password"));
        internalUser.setSkp(rs.getBoolean("skp"));
        internalUser.setBalanceHours(rs.getInt("balance_hours"));
        internalUser.setEmail(rs.getString("email"));


        return internalUser;
    }
}
