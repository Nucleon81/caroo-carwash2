package com.practice.project_1.config;  // Adjust package if needed

import com.practice.project_1.model.Location;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

@ReadingConverter
public class StringToLocationConverter implements Converter<String, Location> {
    @Override
    public Location convert(String source) {
        if (source == null || source.isEmpty()) {
            return new Location(0.0, 0.0);  // Default or null location
        }
        // Simple parsing logic: Assume old string is "lat,long" or handle as needed
        // For example, if old address is not parseable, set defaults or log error
        try {
            String[] parts = source.split(",");
            double lat = Double.parseDouble(parts[0].trim());
            double lon = Double.parseDouble(parts[1].trim());
            return new Location(lat, lon);
        } catch (Exception e) {
            // Fallback: If can't parse, return default
            return new Location(0.0, 0.0);
        }
    }
}
