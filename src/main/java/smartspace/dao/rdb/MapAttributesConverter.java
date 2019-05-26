package smartspace.dao.rdb;

import java.util.Map;

import javax.persistence.AttributeConverter;

import com.fasterxml.jackson.databind.ObjectMapper;

public class MapAttributesConverter implements AttributeConverter<Map<String, Object>, String> {
	private ObjectMapper jackson;

	public MapAttributesConverter() {
		this.jackson = new ObjectMapper();
	}

	@Override
	public String convertToDatabaseColumn(Map<String, Object> map) {
		try {
			return this.jackson.writeValueAsString(map);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Map<String, Object> convertToEntityAttribute(String json) {
		try {
			return this.jackson.readValue(json, Map.class);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
