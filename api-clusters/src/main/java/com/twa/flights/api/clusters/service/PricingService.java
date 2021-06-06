package com.twa.flights.api.clusters.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.twa.flights.api.clusters.dto.UpdatedPaxPriceDTO;
import com.twa.flights.common.dto.itinerary.*;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.twa.flights.api.clusters.connector.PricingConnector;
import com.twa.flights.api.clusters.dto.UpdatedPriceInfoDTO;

@Service
public class PricingService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PricingService.class);

    private PricingConnector pricingConnector;

    @Autowired
    public PricingService(PricingConnector pricingConnector) {
        this.pricingConnector = pricingConnector;
    }

    @CircuitBreaker(name = "pricingService", fallbackMethod = "fallbackPriceItineraries")
    public List<ItineraryDTO> priceItineraries(List<ItineraryDTO> itineraries) {
        LOGGER.info("Pricing itineraries");

        List<UpdatedPriceInfoDTO> updatedPrices = pricingConnector.priceItineraries(itineraries);

        for (UpdatedPriceInfoDTO updatedPriceInfo : updatedPrices) {
            Optional<ItineraryDTO> itinerary = itineraries.stream()
                    .filter(iti -> iti.getId().equals(updatedPriceInfo.getItineraryId())).findFirst();

            if (itinerary.isPresent()) {
                updatePriceInfo(updatedPriceInfo, itinerary.get());
            }
        }

        return itineraries.stream().filter(iti -> iti.getPriceInfo().getAdults().getMarkup() != null)
                .collect(Collectors.toList());
    }

    @SuppressWarnings("unused")
    private List<ItineraryDTO> fallbackPriceItineraries(List<ItineraryDTO> itineraries, RuntimeException exception) {

        LOGGER.info("FALLBACK Pricing itineraries");

        for (ItineraryDTO itinerary : itineraries) {

            UpdatedPriceInfoDTO updatedPriceInfo = new UpdatedPriceInfoDTO();

            updatedPriceInfo.setAdults(getDefaultUpdatedPaxPriceDTO(itinerary.getPriceInfo().getAdults()));

            if (itinerary.getPriceInfo().getChildren() != null) {
                updatedPriceInfo.setChildren(getDefaultUpdatedPaxPriceDTO(itinerary.getPriceInfo().getChildren()));
            }

            if (itinerary.getPriceInfo().getInfants() != null) {
                updatedPriceInfo.setInfants(getDefaultUpdatedPaxPriceDTO(itinerary.getPriceInfo().getInfants()));
            }

            updatePriceInfo(updatedPriceInfo, itinerary);
        }

        return itineraries;
    }

    private UpdatedPaxPriceDTO getDefaultUpdatedPaxPriceDTO(PaxPriceDTO paxPrice) {
        UpdatedPaxPriceDTO updatedPaxPrice = new UpdatedPaxPriceDTO();
        updatedPaxPrice.setMarkup(new MarkupDTO(BigDecimal.ZERO, BigDecimal.ZERO));
        updatedPaxPrice.setTotal(paxPrice.getTotal());

        return updatedPaxPrice;
    }

    private void updatePriceInfo(UpdatedPriceInfoDTO updatedPriceInfo, ItineraryDTO itinerary) {
        PriceInfoDTO priceInfo = itinerary.getPriceInfo();
        priceInfo.getAdults().setMarkup(updatedPriceInfo.getAdults().getMarkup());
        priceInfo.getAdults().setTotal(updatedPriceInfo.getAdults().getTotal());

        if (priceInfo.getChildren() != null) {
            priceInfo.getChildren().setMarkup(updatedPriceInfo.getChildren().getMarkup());
            priceInfo.getChildren().setTotal(updatedPriceInfo.getChildren().getTotal());
        }

        if (priceInfo.getInfants() != null) {
            priceInfo.getInfants().setMarkup(updatedPriceInfo.getInfants().getMarkup());
            priceInfo.getInfants().setTotal(updatedPriceInfo.getInfants().getTotal());
        }
    }
}
