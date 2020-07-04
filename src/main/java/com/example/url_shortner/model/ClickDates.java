package com.example.url_shortner.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Data
public class ClickDates {

    @Id
    @Column(name = "date_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private Instant clickDate;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonIgnore
    @JoinTable(name = "url_dates",
            joinColumns = @JoinColumn(name = "date_id", referencedColumnName = "date_id"),
            inverseJoinColumns = @JoinColumn(name = "url_id", referencedColumnName = "url_id"))
    private Url url ;
}
