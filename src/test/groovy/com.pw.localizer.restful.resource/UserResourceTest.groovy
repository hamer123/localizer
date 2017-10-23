package com.pw.localizer.restful.resource

import com.jayway.jsonpath.JsonPath
import com.jayway.jsonpath.PathNotFoundException
import com.pw.localizer.jackson.CustomFieldsValidation
import com.pw.localizer.model.dto.UserDTO
import com.pw.localizer.model.entity.LocationGPS
import com.pw.localizer.model.entity.User
import com.pw.localizer.model.enums.Role
import com.pw.localizer.repository.user.UserRepository
import com.pw.localizer.restful.provider.jackson.JacksonConfig
import com.pw.localizer.restful.provider.mapper.exception.NoResultExceptionMapper
import com.pw.localizer.restful.resource.user.UserResource
import com.pw.localizer.service.utilitis.DiscoverLazyFetch
import org.dozer.DozerBeanMapperSingletonWrapper
import org.dozer.Mapper
import org.jboss.resteasy.core.Dispatcher
import org.jboss.resteasy.mock.MockDispatcherFactory
import org.jboss.resteasy.mock.MockHttpRequest
import org.jboss.resteasy.mock.MockHttpResponse
import spock.lang.Specification
import javax.ws.rs.core.MediaType

class UserResourceTest extends Specification {

    Dispatcher dispatcher = MockDispatcherFactory.createDispatcher()
    Mapper mapper = DozerBeanMapperSingletonWrapper.getInstance()
    User user = new User()
    UserDTO userDTO = new UserDTO()

    // mocks
    UserRepository userRepository = Mock(UserRepository)
    DiscoverLazyFetch discoverLazyFetch = Mock(DiscoverLazyFetch)
    Mapper mockMapper = Mock(Mapper)
    CustomFieldsValidation customFieldsValidation = Mock(CustomFieldsValidation)

    def setup() {
        user.setId(1L)
        user.setLogin("hamer123")
        user.setEmail("hamer123@vp.pl")
        user.setPhone("123-123-123")
        user.setPassword("q1w2e3")
        user.setRoles(Arrays.asList(Role.ADMIN, Role.USER))

        LocationGPS locationGPS = new LocationGPS();
        locationGPS.setId(1L);
        locationGPS.setLatitude(123.123)
        locationGPS.setLongitude(321.321)

        user.setLastLocationGPS(locationGPS)

        userDTO = mapper.map(user, UserDTO.class, "full")

        UserResource userResource = new UserResource()
        userResource.setUserRepository(userRepository)
        userResource.setDiscoverLazyFetch(discoverLazyFetch)
        userResource.setMapper(mockMapper)
        userResource.setCustomFieldsValidation(customFieldsValidation)
        dispatcher.getRegistry().addSingletonResource(userResource)
        dispatcher.getProviderFactory().registerProvider(NoResultExceptionMapper.class)
        dispatcher.getProviderFactory().registerProvider(JacksonConfig.class, true)
    }
//
//    def "should return 200 and userDto in json format"() {
//        setup: "setup mock request and mock response"
//        MockHttpRequest request = MockHttpRequest.get("/users/1")
//        MockHttpResponse response = new MockHttpResponse()
//
//        when:
//        dispatcher.invoke(request, response)
//        def json = response.contentAsString
//
//        then:
//        1 * userRepository.findById(1) >> user
//        1 * discoverLazyFetch.discoverAndSetToNull(user) >> user
//        1 * mockMapper.map(user, UserDTO.class, "full") >> userDTO
//
//        when:
//        JsonPath.read(json, '$.password') == ''
//
//        then:
//        thrown(PathNotFoundException.class)
//
//        expect:
//        response.status == 200
//        response.outputHeaders["Content-Type"][0] == MediaType.APPLICATION_JSON_TYPE
//
//        with(json) {
//            JsonPath.read(json, '$.id') == 1
//            JsonPath.read(json, '$.login') == "hamer123"
//            JsonPath.read(json, '$.email') == "hamer123@vp.pl"
//            JsonPath.read(json, '$.phone') == "123-123-123"
//        }
//    }
//
//    def "should not found user and return 404"() {
//        given:
//        MockHttpRequest request = MockHttpRequest.get("/users/999")
//        MockHttpResponse response = new MockHttpResponse()
//
//        when:
//        dispatcher.invoke(request, response)
//        def json = response.contentAsString
//
//        then:
//        1 * userRepository.findById(999) >> null
//
//        expect:
//        response.outputHeaders["Content-Type"][0] == MediaType.APPLICATION_JSON_TYPE
//        JsonPath.read(json, '$.message') == "Resource has not been founded"
//        JsonPath.read(json, '$.status') == "NOT_FOUND"
//        JsonPath.read(json, '$.errors[0]') == "User has not been founded with id 999"
//    }
//
//    def "should find user and return user-last-locations-dto and 200 code"() {
//        given:
//        MockHttpRequest request = MockHttpRequest.get("/users/2/last-locations")
//        MockHttpResponse response = new MockHttpResponse()
//
//        when:
//        dispatcher.invoke(request, response)
//        def json = response.contentAsString
//
//        then:
//        1 * userRepository.findById(2) >> user
//        1 * discoverLazyFetch.discoverAndSetToNull(user) >> user
//        1 * mockMapper.map(user, UserDTO.class, "full") >> userDTO
//
//        expect:
//        with(response) {
//            status == 200
//            outputHeaders["Content-Type"][0] == MediaType.APPLICATION_JSON_TYPE
//        }
//
//        with(json) {
//            JsonPath.read(json, '$.gps.id') == 1
//            JsonPath.read(json, '$.gps.latitude') == 123.123
//            JsonPath.read(json, '$.gps.longitude') == 321.321
//            JsonPath.read(json, '$.networkNasz') == null
//            JsonPath.read(json, '$.networkNasz') == null
//        }
//    }
//
//    def "should get all"() {
//        given:
//        MockHttpRequest request = MockHttpRequest.get("/users?size=2&offset=100")
//        MockHttpResponse response = new MockHttpResponse()
//
//        User user2 = new User()
//        user2.setId(123)
//        user2.setLogin("login2")
//        user2.setEmail("login2@vp.pl")
//        user2.setPhone("123-123-123")
//
//        UserDTO userDTO2 = mapper.map(user2,UserDTO.class,"full")
//
//        def userList = new ArrayList<>()
//        userList.add(user)
//        userList.add(user2)
//
//        when:
//        dispatcher.invoke(request,response)
//        def json = response.contentAsString
//
//        then:
//        1 * userRepository.findAll(100,2) >> userList
//        2 * discoverLazyFetch.discoverAndSetToNull(!null) >> user >> user2
//        2 * mockMapper.map(!null, UserDTO.class, "full") >> userDTO >> userDTO2
//        response.status == 200
//        response.outputHeaders['Content-Type'][0] == MediaType.APPLICATION_JSON_TYPE
//
//        expect:
//        with(json) {
//            JsonPath.read(json, '$.length()') == 2
//            JsonPath.read(json, '$[0][?(@.id in [1,123])]')
//            JsonPath.read(json, '$[1][?(@.id in [1,123])]')
//            JsonPath.read(json, '$..[?(@.id == 1)]')
//            JsonPath.read(json, '$..[?(@.id == 123)]')
//            JsonPath.read(json, '$..[?(@.login == "hamer123")]')
//        }
//    }
//
//    def "should return logins"() {
//        given:
//        MockHttpRequest request = MockHttpRequest.get("/users/search/findLoginByLoginLike?loginLike=hamer")
//        MockHttpResponse response = new MockHttpResponse()
//
//        List<String>loginsList = new ArrayList<>()
//        loginsList.add("hamer123")
//        loginsList.add("hamer12345")
//
//        when:
//        dispatcher.invoke(request,response)
//        def json = response.contentAsString
//
//        then:
//        1 * userRepository.findLoginByLoginLike("hamer") >> loginsList
//
//        expect:
//        JsonPath.read(json, '$[?(@ in ["hamer123"])]')
//        JsonPath.read(json, '$[?(@ in ["hamer12345"])]')
//    }
//
//    def "should return list custom field set of user"() {
//        given:
//        MockHttpRequest request = MockHttpRequest.get("/users/search/findByIdIn?idList=2&fieldSet=id&fieldSet=login")
//        MockHttpResponse response = new MockHttpResponse()
//        List<User>userList = new ArrayList<>()
//        userList.add(user)
//
//        when:
//        dispatcher.invoke(request,response)
//        def json = response.contentAsString;
//
//        then:
//        1 * userRepository.findByIdIn({it.size() == 1}) >> userList
//        1 * customFieldsValidation.validateField(UserDTO.class, "id") >> true
//        1 * customFieldsValidation.validateField(UserDTO.class, "login") >> true
//        1 * discoverLazyFetch.discoverAndSetToNull(!null) >> user
//        1 * mockMapper.map(user,UserDTO.class,"full") >> userDTO
//
//        when:
//        JsonPath.read(json,'$.email')
//
//        then:
//        thrown(PathNotFoundException)
//
//        expect:
//        response.status == 200
//        response.outputHeaders['Content-Type'][0] == MediaType.APPLICATION_JSON_TYPE
//        JsonPath.read(json, '$.[0]resource[?(@.id == 1)]')
//        JsonPath.read(json, '$.[0]resource[?(@.login == "hamer123")]')
//    }
//
//    def "should update user and return see other"() {
//
//    }
}
