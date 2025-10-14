package com.tanaka.template.dto;

import lombok.Data;

@Data
public class AccountCreationNotification {
    Role role;
    String email;
    String name;
}
