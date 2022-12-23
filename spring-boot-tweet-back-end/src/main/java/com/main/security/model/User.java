package com.main.security.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.main.model.Auditable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "lw_user")
public class User extends Auditable {

    @Id
    @Column(length = 100, name = "user_name")
    private String username;

    @Column(length = 128)
    private String email;

    @Column(nullable = false, length = 75, name = "pass_word") // //bycrypt password length 50-72
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Transient
    private String updatedPassword;

    @Column(length = 20, name = "phone_number")
    private String phoneNumber;

    @Column(nullable = false, length = 20)
    private String roles;

    @Column(length = 128, name = "system_user")
    private String systemUser;

    @Transient
    private String emailOption;

    @JsonIgnore
    public List<String> getRoleList() {
        if(this.roles.length() > 0) {
            return Arrays.asList(this.roles.split(","));
        }
        return new ArrayList<>();
    }
}
