package com.techtricks.Exam_Seating.services;

import com.techtricks.Exam_Seating.dto.RoomRequest;
import com.techtricks.Exam_Seating.model.Room;
import com.techtricks.Exam_Seating.model.Seat;
import com.techtricks.Exam_Seating.repository.RoomRepository;
import com.techtricks.Exam_Seating.repository.SeatRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;

    private final SeatRepository seatRepository;

    public RoomServiceImpl(RoomRepository roomRepository, SeatRepository seatRepository) {
        this.roomRepository = roomRepository;
        this.seatRepository = seatRepository;
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
