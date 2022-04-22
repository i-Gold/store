package com.company.storehouse.converter.listener;

import org.modelmapper.AbstractConverter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ContextRefreshedListener {

    private final ModelMapper modelMapper;
    private final List<? extends AbstractConverter<?, ?>> converters;

    @Autowired
    public ContextRefreshedListener(ModelMapper modelMapper, List<? extends AbstractConverter<?, ?>> converters) {
        this.modelMapper = modelMapper;
        this.converters = converters;
    }

    @EventListener(classes = ContextRefreshedEvent.class)
    public void addConvertersToModelMapper() {
        converters.forEach(modelMapper::addConverter);
    }

}
