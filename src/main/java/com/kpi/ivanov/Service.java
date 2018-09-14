package com.kpi.ivanov;

/**
 * Webhosting's service.
 */
final class Service {
    private final int type;

    //Integer is chosen because variation is optional and null represent no value
    private final Integer variation;

    Service(int type) {
        if (type <=0 || type > 10) {
            throw new RuntimeException("Invalid value for service type " + type);
        }

        this.type = type;
        this.variation = null;
    }

    Service(int type, int variation) {
        if (type <= 0 || type > 10) {
            throw new RuntimeException("Invalid value for service type " + type);
        }

        if (variation <= 0 || variation > 3) {
            throw new RuntimeException("Invalid value for service variation " + variation);
        }

        this.type = type;
        this.variation = variation;
    }

    boolean isMatches(Service service) {
        return type == service.type && (service.variation == null || service.variation.equals(variation));
    }
}
