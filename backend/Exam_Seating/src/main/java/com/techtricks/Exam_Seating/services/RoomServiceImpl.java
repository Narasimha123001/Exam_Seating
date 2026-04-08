package com.techtricks.Exam_Seating.services;

import com.techtricks.Exam_Seating.dto.RoomRequest;
import com.techtricks.Exam_Seating.exception.InsufficientRoomCapacityException;
import com.techtricks.Exam_Seating.exception.RoomAlreadyPresentException;
import com.techtricks.Exam_Seating.exception.RoomDeletionNotAllowedException;
import com.techtricks.Exam_Seating.model.Room;
import com.techtricks.Exam_Seating.model.Seat;
import com.techtricks.Exam_Seating.repository.RoomRepository;
import com.techtricks.Exam_Seating.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Service
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final SeatRepository seatRepository;




    @Override
    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }


    @Override
    public void deleteRoom(Long roomNumber) {

        log.info("Attempting to delete room with id {}", roomNumber);

        Room room = roomRepository.findByRoomNumber(roomNumber)
                .orElseThrow(() ->
                        new RuntimeException("Room not found with id " + roomNumber)
                );
        try {
            roomRepository.delete(room);
            log.info("Room {} deleted successfully", roomNumber);

        } catch (DataIntegrityViolationException ex) {

            log.error("Room {} cannot be deleted. It is linked with seats or exams.", roomNumber);

            throw new RoomDeletionNotAllowedException(
                    "Room is assigned with exams/seats and cannot be deleted"
            );
        }
    }

    @Override
    public Page<Room> list(int page, int size, String search, String sort) {

        page = Math.max(page, 0);
        size = Math.min(Math.max(size, 1), 100);

        Sort sorting;
        if (sort != null && !sort.isBlank()) {
            String[] parts = sort.split(",");
            sorting = parts.length == 2 && parts[1].equalsIgnoreCase("desc")
                    ? Sort.by(parts[0]).descending()
                    : Sort.by(parts[0]).ascending();
        } else {
            sorting = Sort.by("roomNumber").descending();
        }

        Pageable pageable = PageRequest.of(page, size, sorting);
        return roomRepository.searchRoom(search, pageable);
    }


    @Override
    public Room updateRoom(Long roomNumber , RoomRequest roomRequest) {

        log.info("Updating room with id {}", roomRequest.getName());

        Room room = roomRepository.findByRoomNumber(roomNumber)
                .orElseThrow(() ->
                        new RuntimeException("Room not found with number " + roomRequest.getRoomNumber())
                );

        if (!room.getName().equals(roomRequest.getName())) {
            roomRepository.findByName(roomRequest.getName())
                    .ifPresent(existing -> {
                        throw new RuntimeException(
                                "Room already exists with name " + roomRequest.getName()
                        );
                    });
        }
        room.setRoomNumber(roomRequest.getRoomNumber());
        room.setName(roomRequest.getName());
        room.setBenchesTotal(roomRequest.getBenchesTotal());
        room.setSeatsPerBench(roomRequest.getSeatsPerBench());
        room.setLocation(roomRequest.getLocation());
        int totalCapacity = roomRequest.getBenchesTotal() * roomRequest.getSeatsPerBench();
        room.setTotalCapacity(totalCapacity);
        Room updatedRoom = roomRepository.save(room);
        log.info("Room {} updated successfully", roomRequest.getName());
        return updatedRoom;
    }

    @Override
    public Room addRoom(RoomRequest roomRequest) throws RoomAlreadyPresentException {
        Optional<Room> optionalRoom=roomRepository.findByRoomNumber(roomRequest.getRoomNumber());
        if(optionalRoom.isPresent()){
            throw  new RoomAlreadyPresentException("Room already exists with number " + roomRequest.getRoomNumber());
        }
        Room room = new Room();
        room.setName(roomRequest.getName());
        room.setRoomNumber(roomRequest.getRoomNumber());
        room.setBenchesTotal(roomRequest.getBenchesTotal());
        room.setSeatsPerBench(roomRequest.getSeatsPerBench());
        room.setTotalCapacity(roomRequest.getTotalCapacity());
        room.setLocation(roomRequest.getLocation());

        return  roomRepository.save(room);

    }
    /**
     * Auto-select rooms to accommodate given number of students
     * Selects rooms in order of capacity (largest first) until total >= required
     */
    public List<Long> selectRoomsForTotalStudents(int totalStudents) {

        log.info("Selecting rooms for {} students", totalStudents);

        double multiplier =2.0;
        int requiredCapacity = (int) (totalStudents * multiplier);

        // Fetch all available rooms, ordered by capacity descending
        List<Room> rooms = roomRepository
                .findAllByOrderByCapacityDesc();

        if (rooms.isEmpty()) {
            throw new InsufficientRoomCapacityException("No rooms available");
        }

        List<Long> selected = new ArrayList<>();
        int capacity = 0;

        for (Room room : rooms) {
            selected.add(room.getRoomId());
            capacity += room.getTotalCapacity();

            log.debug("Selected room {} (capacity: {}), cumulative: {}",
                    room.getRoomId(), room.getTotalCapacity(), capacity);

            if (capacity >= requiredCapacity) break;
        }

        if (capacity < requiredCapacity) {
            throw new InsufficientRoomCapacityException(
                    String.format("Insufficient capacity. Need: %d, Available: %d",
                            requiredCapacity, capacity)
            );
        }

        log.info("Selected {} rooms with total capacity {} (target: {})",
                selected.size(), capacity , requiredCapacity);

        return selected;
    }

    public void generatedSeatsForRoom(Long roomId) {

        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room Not Found"));

        int benches = room.getBenchesTotal();
        int perBench = room.getSeatsPerBench() == 0 ? 3 : room.getSeatsPerBench();

        List<Seat> seats = new ArrayList<>();

        for (int b = 1; b <= benches; b++) {

            int row = (int) Math.ceil(b / 3.0);
            int col = ((b - 1) % 3) + 1;

            for (int pos = 1; pos <= perBench; pos++) {

                seats.add(Seat.builder()
                        .room(room)
                        .benchIndex(b)
                        .rowNo(row)
                        .colNo(col)
                        .posNo(pos)
                        .seatLabel("R" + row + "-C" + col + "-P" + pos)
                        .build());
            }
        }

        seatRepository.saveAll(seats);
    }

    @Override
    public List<Room> addRoomsBulk(List<RoomRequest> list) {
        List<Room> rooms = new ArrayList<>();

        for (RoomRequest req : list) {

            Room room = Room.builder()
                    .name(req.getName())
                    .roomNumber(req.getRoomNumber())
                    .benchesTotal(req.getBenchesTotal())
                    .seatsPerBench(req.getSeatsPerBench())
                    .totalCapacity(req.getBenchesTotal() * req.getSeatsPerBench())
                    .location(req.getLocation())
                    .build();

            rooms.add(room);
        }

        List<Room> savedRooms = roomRepository.saveAll(rooms);

        // Auto-generate seats for each room
        for (Room r : savedRooms) {
            generatedSeatsForRoom(r.getRoomId());
        }

        return savedRooms;
    }





}
