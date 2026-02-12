package com.techtricks.coe_auth.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "black_rooms")
public class BlackRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long blackRoomId;

    @Column(unique = true)
    private String blackRoomName;

    @OneToMany(mappedBy = "blackRoom")
    private List<BlackRoomAccess> blackRoomAccess = new ArrayList<>();
}
