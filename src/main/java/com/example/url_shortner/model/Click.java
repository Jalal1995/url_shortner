package com.example.url_shortner.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Data
@EqualsAndHashCode(exclude = "url")
@ToString(exclude = "url")
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Click {

    @Id
    @Column(name = "click_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Instant date;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonIgnore
    @JoinTable(name = "url_clicks",
            joinColumns = @JoinColumn(name = "click_id", referencedColumnName = "click_id"),
            inverseJoinColumns = @JoinColumn(name = "url_id", referencedColumnName = "url_id"))
    private Url url;
}
