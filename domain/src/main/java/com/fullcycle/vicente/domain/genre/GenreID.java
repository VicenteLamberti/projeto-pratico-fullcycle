package com.fullcycle.vicente.domain.genre;

import com.fullcycle.vicente.domain.Identifier;

import java.util.Objects;
import java.util.UUID;

public class GenreID extends Identifier {

    private final String value;

    private GenreID(final String value){
        Objects.requireNonNull(value);
        this.value = value;
    }

    public static GenreID unique(){
        return new GenreID(UUID.randomUUID().toString().toLowerCase());
    }

    public static GenreID from(final String anId){
        return new GenreID(anId);
    }
    public static GenreID from(final UUID anId){
        return new GenreID(anId.toString().toLowerCase());
    }

    @Override
    public String getValue() {
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GenreID genreID = (GenreID) o;
        return Objects.equals(getValue(), genreID.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getValue());
    }
}
