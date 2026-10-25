package com.aeroflex.flightservice.aircraft.enums;

public enum AircraftStatus {

    ACTIVE {
        @Override
        public boolean canTransitionTo(AircraftStatus next) {
            return next == MAINTENANCE || next == RETIRED;
        }
    },

    MAINTENANCE {
        @Override
        public boolean canTransitionTo(AircraftStatus next) {
            return next == ACTIVE || next == RETIRED;
        }
    },

    RETIRED {
        @Override
        public boolean canTransitionTo(AircraftStatus next) {
            return false;
        }
    };

    public abstract boolean canTransitionTo(AircraftStatus next);
}
