package com.aeroflex.flightservice.config;

import com.aeroflex.common.enums.FlightStatus;
import com.aeroflex.flightservice.aircraft.entity.Aircraft;
import com.aeroflex.flightservice.aircraft.entity.AircraftType;
import com.aeroflex.flightservice.aircraft.enums.AircraftStatus;
import com.aeroflex.flightservice.aircraft.repository.AircraftRepository;
import com.aeroflex.flightservice.aircraft.repository.AircraftTypeRepository;
import com.aeroflex.flightservice.airline.entity.Airline;
import com.aeroflex.flightservice.airline.repository.AirlineRepository;
import com.aeroflex.flightservice.airport.entity.Airport;
import com.aeroflex.flightservice.airport.repository.AirportRepository;
import com.aeroflex.flightservice.flight.entity.Flight;
import com.aeroflex.flightservice.flight.repository.FlightRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final AirportRepository airportRepository;
    private final AirlineRepository airlineRepository;
    private final AircraftTypeRepository aircraftTypeRepository;
    private final AircraftRepository aircraftRepository;
    private final FlightRepository flightRepository;

    @Override
    public void run(String... args) {
        if (airportRepository.count() == 0) {
            log.info("Seeding initial Airport data...");
            Airport del = airportRepository.save(Airport.builder().code("DEL").icaoCode("VIDP").name("Indira Gandhi International Airport").city("New Delhi").country("India").timezone("Asia/Kolkata").build());
            Airport bom = airportRepository.save(Airport.builder().code("BOM").icaoCode("VABB").name("Chhatrapati Shivaji Maharaj International Airport").city("Mumbai").country("India").timezone("Asia/Kolkata").build());
            Airport blr = airportRepository.save(Airport.builder().code("BLR").icaoCode("VOBL").name("Kempegowda International Airport").city("Bengaluru").country("India").timezone("Asia/Kolkata").build());
            Airport jfk = airportRepository.save(Airport.builder().code("JFK").icaoCode("KJFK").name("John F. Kennedy International Airport").city("New York").country("United States").timezone("America/New_York").build());
            Airport lhr = airportRepository.save(Airport.builder().code("LHR").icaoCode("EGLL").name("London Heathrow Airport").city("London").country("United Kingdom").timezone("Europe/London").build());
            Airport dxb = airportRepository.save(Airport.builder().code("DXB").icaoCode("OMDB").name("Dubai International Airport").city("Dubai").country("United Arab Emirates").timezone("Asia/Dubai").build());
            Airport sin = airportRepository.save(Airport.builder().code("SIN").icaoCode("WSSS").name("Singapore Changi Airport").city("Singapore").country("Singapore").timezone("Asia/Singapore").build());

            log.info("Seeding initial Airline data...");
            UUID systemOwner = UUID.randomUUID();
            Airline ai = airlineRepository.save(Airline.builder().iataCode("AI").airlineName("Air India").country("India").headquarters("New Delhi").contactNumber("+911124622220").email("contact@airindia.in").foundedYear(1932).websiteUrl("https://www.airindia.in").airlineLogo("https://assets.aeroflex.com/airlines/ai.png").commissionPercentage(new BigDecimal("10.00")).ownerId(systemOwner).active(true).build());
            Airline sixE = airlineRepository.save(Airline.builder().iataCode("6E").airlineName("IndiGo").country("India").headquarters("Gurugram").contactNumber("+911246173838").email("customer.relations@goindigo.in").foundedYear(2006).websiteUrl("https://www.goindigo.in").airlineLogo("https://assets.aeroflex.com/airlines/6e.png").commissionPercentage(new BigDecimal("8.50")).ownerId(systemOwner).active(true).build());
            Airline ba = airlineRepository.save(Airline.builder().iataCode("BA").airlineName("British Airways").country("United Kingdom").headquarters("London").contactNumber("+443444930787").email("support@ba.com").foundedYear(1974).websiteUrl("https://www.britishairways.com").airlineLogo("https://assets.aeroflex.com/airlines/ba.png").commissionPercentage(new BigDecimal("12.00")).ownerId(systemOwner).active(true).build());
            Airline ek = airlineRepository.save(Airline.builder().iataCode("EK").airlineName("Emirates").country("United Arab Emirates").headquarters("Dubai").contactNumber("+971600555555").email("customer.affairs@emirates.com").foundedYear(1985).websiteUrl("https://www.emirates.com").airlineLogo("https://assets.aeroflex.com/airlines/ek.png").commissionPercentage(new BigDecimal("11.00")).ownerId(systemOwner).active(true).build());

            log.info("Seeding initial AircraftType data...");
            AircraftType b777Type = aircraftTypeRepository.save(AircraftType.builder().model("Boeing 777-300ER").manufacturer("Boeing").economyCapacity(280).premiumEconomyCapacity(30).businessCapacity(26).firstClassCapacity(6).seatingConfig("3-4-3").active(true).build());
            AircraftType a320Type = aircraftTypeRepository.save(AircraftType.builder().model("Airbus A320neo").manufacturer("Airbus").economyCapacity(180).premiumEconomyCapacity(0).businessCapacity(0).firstClassCapacity(0).seatingConfig("3-3").active(true).build());
            AircraftType a350Type = aircraftTypeRepository.save(AircraftType.builder().model("Airbus A350-900").manufacturer("Airbus").economyCapacity(264).premiumEconomyCapacity(24).businessCapacity(28).firstClassCapacity(0).seatingConfig("3-3-3").active(true).build());

            log.info("Seeding initial Aircraft data...");
            Aircraft b777 = aircraftRepository.save(Aircraft.builder().registrationNumber("VT-ALN").name("Boeing 777 AI").airline(ai).aircraftType(b777Type).status(AircraftStatus.ACTIVE).active(true).build());
            Aircraft a320 = aircraftRepository.save(Aircraft.builder().registrationNumber("VT-IZI").name("A320 IndiGo").airline(sixE).aircraftType(a320Type).status(AircraftStatus.ACTIVE).active(true).build());
            Aircraft a350 = aircraftRepository.save(Aircraft.builder().registrationNumber("G-XWBA").name("A350 BA").airline(ba).aircraftType(a350Type).status(AircraftStatus.ACTIVE).active(true).build());

            log.info("Seeding initial Flight data...");
            flightRepository.save(Flight.builder().flightNumber("AI-101").airline(ai).aircraft(b777).departureAirport(del).arrivalAirport(jfk).durationMinutes(930).distanceKm(11750).status(FlightStatus.SCHEDULED).active(true).build());
            flightRepository.save(Flight.builder().flightNumber("AI-102").airline(ai).aircraft(b777).departureAirport(jfk).arrivalAirport(del).durationMinutes(890).distanceKm(11750).status(FlightStatus.SCHEDULED).active(true).build());
            flightRepository.save(Flight.builder().flightNumber("6E-501").airline(sixE).aircraft(a320).departureAirport(del).arrivalAirport(bom).durationMinutes(130).distanceKm(1148).status(FlightStatus.SCHEDULED).active(true).build());
            flightRepository.save(Flight.builder().flightNumber("6E-502").airline(sixE).aircraft(a320).departureAirport(bom).arrivalAirport(blr).durationMinutes(105).distanceKm(840).status(FlightStatus.SCHEDULED).active(true).build());
            flightRepository.save(Flight.builder().flightNumber("BA-142").airline(ba).aircraft(a350).departureAirport(del).arrivalAirport(lhr).durationMinutes(560).distanceKm(6710).status(FlightStatus.SCHEDULED).active(true).build());
            flightRepository.save(Flight.builder().flightNumber("EK-511").airline(ek).aircraft(b777).departureAirport(del).arrivalAirport(dxb).durationMinutes(240).distanceKm(2180).status(FlightStatus.SCHEDULED).active(true).build());
            flightRepository.save(Flight.builder().flightNumber("AI-380").airline(ai).aircraft(a350).departureAirport(del).arrivalAirport(sin).durationMinutes(345).distanceKm(4150).status(FlightStatus.SCHEDULED).active(true).build());

            log.info("Flight data seeding completed successfully!");
        }
    }
}
