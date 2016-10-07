package com.pw.localizer.restful.resource.TEST;

import com.pw.localizer.model.dto.AreaDTO;
import com.pw.localizer.model.dto.BasicUserDTO;
import com.pw.localizer.model.dto.DTOUtilitis;
import com.pw.localizer.model.dto.LocationGPSDTO;
import com.pw.localizer.model.entity.Area;
import com.pw.localizer.model.entity.Location;
import com.pw.localizer.model.entity.User;
import com.pw.localizer.model.query.LocationSearch;
import com.pw.localizer.repository.area.AreaRepository;
import com.pw.localizer.repository.location.LocationGPSRepository;
import com.pw.localizer.repository.location.LocationNetworkRepository;
import com.pw.localizer.repository.user.UserRepository;
import com.pw.localizer.service.area.AreaService;
import org.dozer.DozerBeanMapperSingletonWrapper;
import org.dozer.Mapper;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Date;

/**
 * Created by Patryk on 2016-09-24.
 */

@Path("test")
public class TestResource {
    @Inject
    private LocationNetworkRepository locationNetworkRepository;
    @Inject
    private UserRepository userRepository;
    @Inject
    private AreaService areaService;
    @Inject
    private LocationGPSRepository locationGPSRepository;
    @Inject
    private AreaRepository areaRepository;

    @Path("{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Location location(@PathParam("id") long id){
        return locationNetworkRepository.findById(id);
    }

//    @GET
//    @Path("/test1")
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response getOne(){
//        Bean bean = new Bean();
//        bean.setName("ala");
//        bean.setName2("kasia");
//        bean.setName3("zosia");
//        return Response.ok(bean).build();
//    }

    @GET
    @Path("/test2")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTwo(){
        User user = userRepository.findById(2L);
        DTOUtilitis dtoUtilitis = new DTOUtilitis();
        dtoUtilitis.convertHibernateProxyToNull(user);
        return Response.ok(user).build();
    }

//    @GET
//    @Path("/test3")
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response getThree(){
//        User user = userRepository.findById(2L);
//        BasicUserDTO basicUserDTO = BasicUserDTO.convertToBasicUserDTO(user);
//        return Response.ok(basicUserDTO).build();
//    }

//    @GET
//    @Path("/test4")
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response getFour(){
//        return Response.ok(LocationGPSDTO.convertToLocationGpsDTO(locationGPSRepository.findById(67L))).build();
//    }

    @GET
    @Path("/test5")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFive(){
        LocationSearch locationSearch = new LocationSearch();
        locationSearch.setFromDate(new Date());
        locationSearch.setToDate(new Date());
        locationSearch.setLogin("hamer123");

        return Response.ok(locationSearch).build();
    }

    @GET
    @Path("/test6")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSix(){
        Area area = areaRepository.findById(7L);
//        DTOUtilitis dtoUtilitis = new DTOUtilitis();
//        area = dtoUtilitis.convertHibernateProxyToNull(area);

        Mapper mapper = DozerBeanMapperSingletonWrapper.getInstance();
        return Response.ok(mapper.map(area,AreaDTO.class)).build();
    }

}
