package com.example.url_shortner.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
public class PasswordResetToken {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "token_id")
    private long tokenId;

    @Column(name = "confirmation_token")
    private String token;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @OneToOne(targetEntity = UserInfo.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name ="user_id")
    private UserInfo user;

    public PasswordResetToken(UserInfo user){
        this.user = user;
        createdDate = new Date();
        token = UUID.randomUUID().toString();
    }
}
