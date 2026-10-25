package com.aeroflex.flightscheduleservice.config;

import com.aeroflex.common.enums.FlightStatus;
import com.aeroflex.flightscheduleservice.entity.FlightSchedule;
import com.aeroflex.flightscheduleservice.repository.FlightScheduleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final FlightScheduleRepository scheduleRepository;

    @Override
    public void run(String... args) {
        if (scheduleRepository.count() == 0) {
            log.info("Seeding initial Flight Schedules for the next 7 days...");

            LocalDate today = LocalDate.now();

            for (int i = 0; i < 7; i++) {
                LocalDate date = today.plusDays(i);

                // AI-101 DEL -> JFK
                scheduleRepository.save(FlightSchedule.builder()
                        .flightNumber("AI-101")
                        .flightId(UUID.randomUUID())
                        .airlineCode("AI")
                        .airlineName("Air India")
                        .departureAirportCode("DEL")
                        .arrivalAirportCode("JFK")
                        .departureCity("New Delhi")
                        .arrivalCity("New York")
                        .scheduledDepartureTime(LocalDateTime.of(date, LocalTime.of(2, 20)))
                        .scheduledArrivalTime(LocalDateTime.of(date, LocalTime.of(7, 35)))
                        .aircraftModel("Boeing 777-300ER")
                        .aircraftId(UUID.randomUUID())
                        .status(FlightStatus.SCHEDULED)
                        .gate("Gate 14")
                        .terminal("Terminal 3")
                        .economyPrice(new BigDecimal("750.00"))
                        .premiumEconomyPrice(new BigDecimal("1200.00"))
                        .businessPrice(new BigDecimal("2800.00"))
                        .firstClassPrice(new BigDecimal("5500.00"))
                        .totalSeats(342)
                        .availableSeats(342)
                        .build());

                // 6E-501 DEL -> BOM
                scheduleRepository.save(FlightSchedule.builder()
                        .flightNumber("6E-501")
                        .flightId(UUID.randomUUID())
                        .airlineCode("6E")
                        .airlineName("IndiGo")
                        .departureAirportCode("DEL")
                        .arrivalAirportCode("BOM")
                        .departureCity("New Delhi")
                        .arrivalCity("Mumbai")
                        .scheduledDepartureTime(LocalDateTime.of(date, LocalTime.of(8, 0)))
                        .scheduledArrivalTime(LocalDateTime.of(date, LocalTime.of(10, 15)))
                        .aircraftModel("Airbus A320neo")
                        .aircraftId(UUID.randomUUID())
                        .status(FlightStatus.SCHEDULED)
                        .gate("Gate 22")
                        .terminal("Terminal 2")
                        .economyPrice(new BigDecimal("85.00"))
                        .premiumEconomyPrice(new BigDecimal("120.00"))
                        .businessPrice(new BigDecimal("250.00"))
                        .firstClassPrice(new BigDecimal("400.00"))
                        .totalSeats(180)
                        .availableSeats(180)
                        .build());

                // BA-142 DEL -> LHR
                scheduleRepository.save(FlightSchedule.builder()
                        .flightNumber("BA-142")
                        .flightId(UUID.randomUUID())
                        .airlineCode("BA")
                        .airlineName("British Airways")
                        .departureAirportCode("DEL")
                        .arrivalAirportCode("LHR")
                        .departureCity("New Delhi")
                        .arrivalCity("London")
                        .scheduledDepartureTime(LocalDateTime.of(date, LocalTime.of(10, 45)))
                        .scheduledArrivalTime(LocalDateTime.of(date, LocalTime.of(15, 30)))
                        .aircraftModel("Airbus A350-900")
                        .aircraftId(UUID.randomUUID())
                        .status(FlightStatus.SCHEDULED)
                        .gate("Gate 8")
                        .terminal("Terminal 3")
                        .economyPrice(new BigDecimal("620.00"))
                        .premiumEconomyPrice(new BigDecimal("980.00"))
                        .businessPrice(new BigDecimal("2400.00"))
                        .firstClassPrice(new BigDecimal("4800.00"))
                        .totalSeats(316)
                        .availableSeats(316)
                        .build());

                // EK-511 DEL -> DXB
                scheduleRepository.save(FlightSchedule.builder()
                        .flightNumber("EK-511")
                        .flightId(UUID.randomUUID())
                        .airlineCode("EK")
                        .airlineName("Emirates")
                        .departureAirportCode("DEL")
                        .arrivalAirportCode("DXB")
                        .departureCity("New Delhi")
                        .arrivalCity("Dubai")
                        .scheduledDepartureTime(LocalDateTime.of(date, LocalTime.of(15, 30)))
                        .scheduledArrivalTime(LocalDateTime.of(date, LocalTime.of(17, 55)))
                        .aircraftModel("Boeing 777-300ER")
                        .aircraftId(UUID.randomUUID())
                        .status(FlightStatus.SCHEDULED)
                        .gate("Gate 18")
                        .terminal("Terminal 3")
                        .economyPrice(new BigDecimal("280.00"))
                        .premiumEconomyPrice(new BigDecimal("450.00"))
                        .businessPrice(new BigDecimal("1100.00"))
                        .firstClassPrice(new BigDecimal("2200.00"))
                        .totalSeats(342)
                        .availableSeats(342)
                        .build());
            }

            log.info("Flight Schedules seeded successfully!");
        }
    }
}
