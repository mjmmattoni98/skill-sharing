package com.losacabaos.skillsharing.login;

import java.util.Collection;

public interface UserDao {
    InternalUser loadUserByUsername(String user);//, String password);
    Collection<InternalUser> listAllUsers();
}
