package br.serializationconv;

import org.springframework.http.MediaType;
import org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;

public class YamlConverter extends AbstractJackson2HttpMessageConverter {
    public YamlConverter (){
        super(new YAMLMapper()
            .setSerializationInclusion(
                JsonInclude.Include.NON_NULL),
                MediaType.parseMediaType("application/x-yaml")
            );
    }
}
