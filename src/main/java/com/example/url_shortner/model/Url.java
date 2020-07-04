package com.example.url_shortner.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@EqualsAndHashCode(exclude = {"user","dates"})
@ToString(exclude = {"user","dates"})
@NoArgsConstructor
public class Url {

    @Id
    @Column(name = "url_id")
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

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonIgnore
    @JoinTable(name = "users_urls",
            joinColumns = @JoinColumn(name = "url_id", referencedColumnName = "url_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "user_id"))
    private User user ;

    @JsonIgnore
    @OneToMany(mappedBy = "url", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<ClickDates> dates = new HashSet<>();

}
