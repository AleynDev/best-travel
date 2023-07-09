package com.aleyn.best_travel.domain.entities.documents;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "app_users")
public class AppUserDocument implements Serializable {

    @Id
    private String id;

    @Field(name = "username")
    private String userName;

    @Field(name = "dni")
    private String dni;

    @Field(name = "enabled")
    private boolean enabled;

    @Field(name = "password")
    private String password;

    private Role role;

}
