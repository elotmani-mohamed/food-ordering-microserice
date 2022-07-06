package com.food.ordering.system.service.domain.objectvalue;

import com.food.ordering.system.domain.valueobjects.BaseId;

import java.util.UUID;

public class TrackingId extends BaseId<UUID> {

    public TrackingId(UUID value) {
        super(value);
    }
}
