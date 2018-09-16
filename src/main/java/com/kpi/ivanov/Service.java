package com.kpi.ivanov;

/**
 * Webhosting's service.
 */
final class Service {
    private final int type;

    //Integer is chosen because variation is optional and null represent no value
    private final Integer variation;

    Service(int type) {
        checkType(type);

        this.type = type;
        this.variation = null;
    }

    Service(int type, int variation) {
        checkType(type);
        checkVariation(variation);

        this.type = type;
        this.variation = variation;
    }

    boolean isMatches(Service service) {
        return type == service.type && (service.variation == null || service.variation.equals(variation));
    }

    private static void checkType(int type) {
        if (type < 1 || type > 10) {
            throw new IllegalArgumentException("Invalid value for service type. " +
                    "The type can take values from 1 to 10 but not the " + type);
        }
    }

    private static void checkVariation(int variation) {
        if (variation < 1 || variation > 3) {
            throw new IllegalArgumentException("Invalid value for service type. " +
                    "The variation can take values from 1 to 3 but not the " + variation);
        }
    }
}
