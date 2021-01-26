package com.example.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "statictics")
@Data
@NoArgsConstructor
public class Statistics {
    @Id
    @GeneratedValue
    @Type(type = "pg-uuid")
    @Column(name = "id")
    private UUID id;


    @ManyToOne(fetch = FetchType.LAZY)
    private Player player;


    @ManyToOne(fetch = FetchType.LAZY)
    private Answer answer;
}
