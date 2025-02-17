package ru.yandex.practicum.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "actions")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Action {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "sensor_id")
    private Sensor sensor;
    @Enumerated(EnumType.STRING)
    private ActionType type;
    Integer value;
    @ManyToMany(mappedBy = "actions")
    private List<Scenario> scenarios;
}