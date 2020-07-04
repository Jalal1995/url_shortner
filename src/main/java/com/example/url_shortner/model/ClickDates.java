package com.example.url_shortner.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Data
@EqualsAndHashCode(exclude = "url")
@ToString(exclude = "url")
public class ClickDates {

    @Id
    @Column(name = "click_date_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Instant clickDate;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonIgnore
    @JoinTable(name = "url_dates",
            joinColumns = @JoinColumn(name = "click_date_id", referencedColumnName = "click_date_id"),
            inverseJoinColumns = @JoinColumn(name = "url_id", referencedColumnName = "url_id"))
    private Url url ;
}
