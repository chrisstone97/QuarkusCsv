package com.rc;

import io.quarkus.elytron.security.common.BcryptUtil;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.security.jpa.Password;
import io.quarkus.security.jpa.Roles;
import io.quarkus.security.jpa.UserDefinition;
import io.quarkus.security.jpa.Username;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "user_access")
@UserDefinition
public class UserAccess extends PanacheEntity {
    @Username
    public String username;
    @Password
    public String password;
    @Roles
    public String role;

    public static void add(String username, String password, String role) {
        UserAccess user = new UserAccess();
        user.username = username;
        user.password = BcryptUtil.bcryptHash(password);
        user.role = role;
        user.persist();
    }
}
