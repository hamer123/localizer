package dozer;

import static org.junit.Assert.*;
import com.pw.localizer.model.dto.BasicUserDTO;
import com.pw.localizer.model.dto.LocationGPSDTO;
import com.pw.localizer.model.dto.LocationNetworkDTO;
import com.pw.localizer.model.dto.UserDTO;
import com.pw.localizer.model.entity.*;
import org.dozer.DozerBeanMapperSingletonWrapper;
import org.dozer.Mapper;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by Patryk on 2016-10-05.
 */
public class DozerTest {

    private Mapper mapper;

    @Before
    public void before(){
        mapper = DozerBeanMapperSingletonWrapper.getInstance();
    }

    @Test
    public void convertBasicUserDTOToUser(){
        User user = new User();
        user.setId(1L);
        user.setLogin("hamer123");
        user.setEmail("hamer123@vp.pl");

        BasicUserDTO basicUserDTO = mapper.map(user, BasicUserDTO.class);

        assertEquals(1L, basicUserDTO.getId());
        assertEquals("hamer123", basicUserDTO.getLogin());
        assertEquals("hamer123@vp.pl", basicUserDTO.getEmail());

        User repeat = mapper.map(basicUserDTO, User.class);

        assertEquals(1L, repeat.getId());
        assertEquals("hamer123", repeat.getLogin());
        assertEquals("hamer123@vp.pl", repeat.getEmail());
    }

    @Test
    public void convertUserDTOToUser(){
        User user = new User();
        user.setId(1L);
        user.setLogin("hamer123");
        user.setEmail("hamer123@vp.pl");

        LocationGPS locationGPS = new LocationGPS();
        locationGPS.setId(1L);
        locationGPS.setLatitude(123123123.12312312);
        user.setLastLocationGPS(locationGPS);

        LocationNetwork locationNetwork = new LocationNetwork();
        locationNetwork.setLatitude(123123.123123123);
        user.setLastLocationNetworObcaUsluga(locationNetwork);

        UserDTO userDTO = mapper.map(user, UserDTO.class, "full");

        assertEquals(1L, userDTO.getId());
        assertEquals("hamer123", userDTO.getLogin());
        assertEquals("hamer123@vp.pl", userDTO.getEmail());
        System.out.println(userDTO.getUserLastLocationsDTO().getLastLocationGps().getLatitude());
        System.out.println(userDTO.getUserLastLocationsDTO().getLastLocationNetworkObcy().getLatitude());

        User repeat = mapper.map(userDTO, User.class);

        assertEquals(1L, repeat.getId());
        assertEquals("hamer123", repeat.getLogin());
        assertEquals("hamer123@vp.pl", repeat.getEmail());
    }

    @Test
    public void convertLocationGPSToLocationGPSDTO(){
        LocationGPS locationGPS = new LocationGPS();
        User user = new User();
        user.setLogin("hamer123");
        locationGPS.setUser(user);

        LocationGPSDTO locationGPSDTO = mapper.map(locationGPS, LocationGPSDTO.class);

        assertEquals("hamer123", locationGPSDTO.getUser().getLogin());
        System.out.println(locationGPSDTO.getUser().getLogin());
    }

    @Test
    public void convertLocationNetworkToLocationNetworkDTO(){
        LocationNetwork locationNetwork = new LocationNetwork();
        WifiInfo wifiInfo = new WifiInfo();
        CellInfoGSM cellInfoGSM = new CellInfoGSM();
        locationNetwork.setWifiInfo(wifiInfo);
        locationNetwork.setCellInfoMobile(cellInfoGSM);

        LocationNetworkDTO locationNetworkDTO = mapper.map(locationNetwork, LocationNetworkDTO.class);
        assertTrue(locationNetworkDTO.getWifiInfo() == locationNetwork.getWifiInfo());
        assertTrue(locationNetworkDTO.getCellInfoMobile() == locationNetwork.getCellInfoMobile());
    }


}
