package com.example.url_shortner.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Url {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Instant creationDate;

    @NotNull
    @Size(min = 1, message = "This field can't be empty.")
    private String shortUrl;

    @NotNull
    @Size(min = 1, message = "This field can't be empty.")
    @Column(columnDefinition = "varchar(1000)", unique = true)
    private String fullUrl;

    @NotNull
    private Boolean isActive;

    private Long visitCount;
}
