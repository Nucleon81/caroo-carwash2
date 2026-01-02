package com.practice.project_1.service;

import com.practice.project_1.model.CarType;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class PriceService {
    private static final Map<String, Map<CarType, Double>> priceTable = new HashMap<>();

    static {
        Map<CarType, Double> basic = new HashMap<>();
        basic.put(CarType.HATCHBACK, 469.64);
        basic.put(CarType.SEDAN, 587.64);
        basic.put(CarType.SUV_MUV, 705.64);
        basic.put(CarType.LUXURY, 941.64);
        priceTable.put("Basic Wash", basic);

        Map<CarType, Double> premium = new HashMap<>();
        premium.put(CarType.HATCHBACK, 705.64);
        premium.put(CarType.SEDAN, 823.64);
        premium.put(CarType.SUV_MUV, 941.64);
        premium.put(CarType.LUXURY, 1295.64);
        priceTable.put("Premium Wash", premium);

        Map<CarType, Double> hybrid = new HashMap<>();
        hybrid.put(CarType.HATCHBACK, 1295.64);
        hybrid.put(CarType.SEDAN, 1649.64);
        hybrid.put(CarType.SUV_MUV, 1885.64);
        hybrid.put(CarType.LUXURY, 2475.64);
        priceTable.put("Hybrid Wash", premium);

        // TODO: Add other services and prices as needed
    }

    public double getPrice(String serviceName, CarType carType, boolean ceramicCoated) {
        double price = priceTable.getOrDefault(serviceName, new HashMap<>()).getOrDefault(carType, 0.0);
        // Example: add-on ceramic, adjust/add logic as needed
        if (ceramicCoated && !"Ceramic Coating".equals(serviceName)) {
            price += 500.0;
        }
        return price;
    }
}
