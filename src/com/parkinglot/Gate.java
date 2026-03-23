package com.parkinglot;

public class Gate {
    private final int id;

    public Gate(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Gate gate = (Gate) o;
        return id == gate.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}
