package com.twa.flights.api.itineraries.search.facade;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.twa.flights.api.itineraries.search.connector.ProviderBetaConnector;
import com.twa.flights.common.dto.enums.Provider;
import com.twa.flights.common.dto.itinerary.ItineraryDTO;
import com.twa.flights.common.dto.request.AvailabilityRequestDTO;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@Component
public class ProviderBetaFacade implements ProviderFacade {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProviderBetaFacade.class);

    private ProviderBetaConnector providerBetaConnector;

    @Autowired
    public ProviderBetaFacade(final ProviderBetaConnector providerBetaConnector) {
        this.providerBetaConnector = providerBetaConnector;
    }

    @CircuitBreaker(name = "provider-beta")
    public List<ItineraryDTO> availability(final AvailabilityRequestDTO request) {
        LOGGER.debug("Obtain the information about the flights");
        return providerBetaConnector.availability(request);
    }

    @Override
    public Provider getProvider() {
        return Provider.BETA;
    }

}
