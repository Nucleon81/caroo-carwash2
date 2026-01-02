package com.practice.project_1.service;

import com.practice.project_1.model.AddOn;
import com.practice.project_1.repository.AddOnRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddOnService {

    @Autowired
    private AddOnRepository addOnRepository;

    public List<AddOn> getAllAddOns() {
        return addOnRepository.findAll();
    }

    @PostConstruct
    public void seedAddOns() {
        if (addOnRepository.count() == 0) {
            addOnRepository.save(new AddOn(null, "Ceramic Coating", "Protective ceramic layer", 11798.82));
            addOnRepository.save(new AddOn(null, "Graphene Coating", "Advanced graphene protection", 17698.82));
            addOnRepository.save(new AddOn(null, "Headlight Restoration", "Restore foggy headlights", 1178.82));
            addOnRepository.save(new AddOn(null, "AC Disinfection", "Clean and disinfect AC system", 706.82));
            addOnRepository.save(new AddOn(null, "Teflon Coating", "Teflon protective coating", 1768.82));
            addOnRepository.save(new AddOn(null, "Rubbing & Polishing", "Polish and buff exterior", 2358.82));
        }
    }
}
