package ru.yandex.practicum.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "conditions")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Condition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "sensor_id")
    private Sensor sensor;
    @Enumerated(EnumType.STRING)
    private ConditionType type;
    @Enumerated(EnumType.STRING)
    private ConditionOperation operation;
    Integer value;
    @ManyToMany(mappedBy = "conditions")
    private List<Scenario> scenarios;
}
