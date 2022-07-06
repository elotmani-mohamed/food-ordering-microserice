package com.food.ordering.system.service.domain.objectvalue;

import java.util.Objects;
import java.util.UUID;

public class StreetAddress {

    private final UUID id;
    private final String street;
    private final String postCode;
    private final String citty;

    public StreetAddress(UUID id, String street, String postCode, String citty) {
        this.id = id;
        this.street = street;
        this.postCode = postCode;
        this.citty = citty;
    }

    public UUID getId() {
        return id;
    }

    public String getStreet() {
        return street;
    }

    public String getPostCode() {
        return postCode;
    }

    public String getCitty() {
        return citty;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StreetAddress that = (StreetAddress) o;
        return street.equals(that.street) && postCode.equals(that.postCode) && citty.equals(that.citty);
    }

    @Override
    public int hashCode() {
        return Objects.hash(street, postCode, citty);
    }
}
