package com.lty.model.dto.user;

import lombok.Data;

import java.io.Serializable;

/**
 * @author lty
 */
@Data
public class UserRegisterRequest implements Serializable {

    private String userAccount;

    private String userPassword;
}
