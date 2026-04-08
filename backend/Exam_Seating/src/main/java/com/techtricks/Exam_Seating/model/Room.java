package com.techtricks.Exam_Seating.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Entity
@Table(name = "room")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomId;

    @Column(name = "room_number", nullable = false, unique = true)
    private Long roomNumber;

    @Column(name = "name")
    private String name;

    @Column(name = "benches_total")
    private Integer benchesTotal;

    @Column(name = "seats_per_bench")
    private Integer seatsPerBench;

    @Column(name = "total_capacity")
    private Integer totalCapacity;

    @Column(name = "location")
    private String location;
}