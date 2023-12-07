package ru.wv3rine.abspringwebapp.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// В задании написано про
// name notnull, хотя логичнее это сделать с логином (с учетом
// акцента на аутентификации
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {
        @Id
        @GeneratedValue
        private Integer id;
        private String name;
        @NotNull
        @NotBlank
        private String login;
        @NotNull
        @NotBlank
        private String password;
        private String url;
}
