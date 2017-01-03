package com.pw.localizer.produces;

import com.pw.localizer.config.LazyInitializeDozerFieldMapper;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class DozerMapperProducer {

    private DozerBeanMapper dozerBeanMapper = new DozerBeanMapper();

    @PostConstruct
    public void postConstruct() {
        List<String> myMappingFiles = new ArrayList<>();
        myMappingFiles.add("dozerBeanMapping.xml");
        dozerBeanMapper.setCustomFieldMapper(new LazyInitializeDozerFieldMapper());
        dozerBeanMapper.setMappingFiles(myMappingFiles);
    }

    @Produces
    public Mapper mapper() {
        return dozerBeanMapper;
    }
}
