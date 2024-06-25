package com.ludo.study.studymatchingplatform.common.converter;

import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class IntegerListConverter implements AttributeConverter<List<Integer>, String> {

	private final ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public String convertToDatabaseColumn(List<Integer> attribute) {
		try {
			return objectMapper.writeValueAsString(attribute);
		} catch (Exception e) {
			throw new IllegalArgumentException("Error converting list to JSON", e);
		}
	}

	@Override
	public List<Integer> convertToEntityAttribute(String dbData) {
		try {
			return objectMapper.readValue(dbData, new TypeReference<List<Integer>>() {
			});
		} catch (Exception e) {
			throw new IllegalArgumentException("Error converting JSON to list", e);
		}
	}
}
