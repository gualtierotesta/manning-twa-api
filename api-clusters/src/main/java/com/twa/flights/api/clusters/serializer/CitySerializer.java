package com.twa.flights.api.clusters.serializer;

import com.twa.flights.api.clusters.dto.CityDTO;
import com.twa.flights.api.clusters.dto.ClusterSearchDTO;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Service;

@Service
public class CitySerializer implements RedisSerializer<CityDTO> {

    @Override
    public byte[] serialize(final CityDTO pCityDTO) {
        return JsonSerializer.serialize(pCityDTO);
    }

    @Override
    public CityDTO deserialize(final byte[] bytes) {
        return JsonSerializer.deserialize(bytes, CityDTO.class);
    }
}
