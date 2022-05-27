package com.tweetapp.security.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tweetapp.constants.ConstantRegex;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "user")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Integer userId;

    @Column(nullable = false, unique = true, length = 100)
    private String username;

    @Column(nullable = false, length = 20)
    private String roles;

    @Column(nullable = false, length = 75) // //bycrypt password length 50-72
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String passWord;

    @Transient
    @JsonIgnore
    private String confirmPassword;

    @Column(length = 128, nullable = false)
    @Pattern(regexp= ConstantRegex.EMAIL_VALIDATION_REGEX,
            message=ConstantRegex.EMAIL_VALIDATION_MESSAGE)
    private String email;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createDateTime;

    @JsonIgnore
    public List<String> getRoleList(){
        if(this.roles.length() > 0){
            return Arrays.asList(this.roles.split(","));
        }
        return new ArrayList<>();
    }
}
