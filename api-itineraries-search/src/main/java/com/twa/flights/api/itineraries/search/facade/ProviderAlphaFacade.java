package com.twa.flights.api.itineraries.search.facade;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.twa.flights.api.itineraries.search.connector.ProviderAlphaConnector;
import com.twa.flights.common.dto.enums.Provider;
import com.twa.flights.common.dto.itinerary.ItineraryDTO;
import com.twa.flights.common.dto.request.AvailabilityRequestDTO;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@Component
public class ProviderAlphaFacade implements ProviderFacade {

    static final Logger LOGGER = LoggerFactory.getLogger(ProviderAlphaFacade.class);

    private ProviderAlphaConnector providerAlphaConnector;

    @Autowired
    public ProviderAlphaFacade(final ProviderAlphaConnector providerAlphaConnector) {
        this.providerAlphaConnector = providerAlphaConnector;
    }

    @CircuitBreaker(name = "provider-alpha")
    public List<ItineraryDTO> availability(final AvailabilityRequestDTO request) {
        LOGGER.info("Obtain the information about the flights");
        return providerAlphaConnector.availability(request);
    }

    @Override
    public Provider getProvider() {
        return Provider.ALPHA;
    }

}
