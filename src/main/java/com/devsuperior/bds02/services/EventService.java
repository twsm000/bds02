package com.devsuperior.bds02.services;

import com.devsuperior.bds02.dto.EventDTO;
import com.devsuperior.bds02.entities.Event;
import com.devsuperior.bds02.repositories.CityRepository;
import com.devsuperior.bds02.repositories.EventRepository;
import com.devsuperior.bds02.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Service
public class EventService {
    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private CityRepository cityRepository;

    @Transactional
    public EventDTO update(Long id, EventDTO dto) {
        try {
            var event = eventRepository.getOne(id);
            copyDTOToEntity(dto, event);
            return new EventDTO(eventRepository.save(event));
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException(String.format("Event with id %d not found", id));
        }
    }

    @Transactional(readOnly = true)
    private void copyDTOToEntity(EventDTO dto, Event entity) {
        try {
            var city = cityRepository.findById(dto.getCityId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            String.format("City with id %d not found.", dto.getCityId())));
            entity.setCity(city);
            entity.setDate(dto.getDate());
            entity.setName(dto.getName());
            entity.setUrl(dto.getUrl());
        } catch (IllegalArgumentException e) {
            throw new ResourceNotFoundException("City id not informed");
        }
    }
}
