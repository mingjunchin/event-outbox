package com.example.eventoutbox.repository;

import com.example.eventoutbox.entity.Event;
import org.springframework.data.repository.CrudRepository;

public interface EventRepository extends CrudRepository<Event, String> {

}
