package com.example.dto;

import java.util.List;

public class AssignAttributesRequest {
    private List<AttributeDto> attributes;

    public List<AttributeDto> getAttributes() { return attributes; }
    public void setAttributes(List<AttributeDto> attributes) { this.attributes = attributes; }

    public static class AttributeDto {
        private String name;
        private List<String> values;

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public List<String> getValues() { return values; }
        public void setValues(List<String> values) { this.values = values; }
    }
}
