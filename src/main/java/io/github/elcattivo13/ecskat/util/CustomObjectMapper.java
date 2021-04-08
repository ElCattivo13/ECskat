package io.github.elcattivo13.ecskat.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.jackson.ObjectMapperCustomizer;

import javax.enterprise.inject.Instance;
import javax.inject.Singleton;

public class CustomObjectMapper {

    // Replaces the CDI producer for ObjectMapper built into Quarkus
    @Singleton
    ObjectMapper objectMapper(Instance<ObjectMapperCustomizer> customizers) {
    	ObjectMapper mapper = new ObjectMapper();
        mapper.getSerializerProvider().setNullKeySerializer(new NullKeySerializer());

        // Apply all ObjectMapperCustomerizer beans (incl. Quarkus)
        for (ObjectMapperCustomizer customizer : customizers) {
            customizer.customize(mapper);
        }

        return mapper;
    }
}
