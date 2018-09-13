package com.kpi.ivanov;

import java.util.Objects;

/**
 * Represents a service
 * Service has two fields type and variation, variation can be optional
 */
public final class Service {
    private final int type;
    private final Integer variation;

    public Service(int type, int variation) {
        this.type = type;
        this.variation = variation;
    }

    public Service(int type) {
        this.type = type;
        variation = null;
    }

    boolean isMatches(Service service) {
        if (service.variation == null) {
            return type == service.type;
        }

        if (variation != null) {
            return type == service.type && variation.equals(service.variation);
        }

        return false;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Service)) {
            return false;
        }

        Service service = (Service) obj;

        if (this.variation != null && service.variation != null) {
            return this.type == service.type && this.variation.equals(service.variation);
        }

        if (this.variation== null && service.variation == null) {
            return this.type == service.type;
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, variation);
    }
}
