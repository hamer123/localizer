package com.pw.localizer.produces;

import org.dozer.DozerBeanMapperSingletonWrapper;
import org.dozer.Mapper;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;


/**
 * Created by Patryk on 2016-10-06.
 */

public class DozerMapperProducer {
    @Produces @ApplicationScoped
    private Mapper mapper = DozerBeanMapperSingletonWrapper.getInstance();
}
