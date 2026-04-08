package com.techtricks.Exam_Seating.model;


import com.techtricks.coe_auth.models.Staff;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "invigilator")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Invigilator {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long invigilatorId;
    @ManyToOne
    @JoinColumn(name = "staff_id")
    private Staff staff;
    private String name;
    private String phone;
    private String email;

    @ManyToOne
    @JoinColumn(name = "room_id_room_id")
    private Room roomID;

    private List<Long> sessionId;
}