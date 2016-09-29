package com.pw.localizer.model.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.xml.bind.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.pw.localizer.model.enums.Role;

//javax.persistence.loadgraph
@Entity
@NamedEntityGraphs({
	@NamedEntityGraph(name = "User.graph.areas",
			          attributeNodes = {@NamedAttributeNode(value = "areas", subgraph = "points")},
			          subgraphs = @NamedSubgraph(name = "points", attributeNodes = @NamedAttributeNode("points"))
	)
})
@NamedQueries(
		value = {
				@NamedQuery(name ="USER.findAll",
						query="SELECT u FROM User u"),
				@NamedQuery(name ="USER.findByLoginLikeAndEmailLikeAndPhoneLike",
				            query = "SELECT new com.pw.localizer.model.entity.User(u.login, u.email, u.phone) " +
									"FROM User u WHERE u.login LIKE :login AND u.email LIKE :email AND u.phone LIKE :phone"),
				@NamedQuery(name ="USER.findByLogins",
						query="SELECT u FROM User u WHERE u.login IN (:logins)"),
				@NamedQuery(name="USER.findLastGpsLocationByUserId",
						query="SELECT u.lastLocationGPS FROM User u WHERE u.id =:id"),
				@NamedQuery(name="USER.findLastNetworkNaszLocationByUserId",
						query="SELECT u.lastLocationNetworkNaszaUsluga FROM User u WHERE u.id =:id"),
				@NamedQuery(name="USER.findLastNetworkObcyLocationByUserId",
						query="SELECT u.lastLocationNetworObcaUsluga FROM User u WHERE u.id =:id"),
				@NamedQuery(name ="USER.findByLogin",
						query="SELECT u FROM User u WHERE u.login =:login"),
				@NamedQuery(name ="USER.findByLoginAndPassword",
						query="SELECT u FROM User u WHERE u.login = :login AND u.password =:password"),
				@NamedQuery(name ="USER.findLoginByLoginLike",
						query="SELECT u.login FROM User u WHERE u.login LIKE :login"),
				@NamedQuery(name ="USER.findUserWithPolygonsByLogin",
						query="SELECT u FROM User u INNER JOIN FETCH u.areas WHERE u.login =:login"),
				@NamedQuery(name ="USER.findUsersByIds",
						query="SELECT u FROM User u WHERE u.id IN (:ids)"),
				@NamedQuery(name ="USER.findUserFetchRolesByLoginAndPassword",
						query="SELECT u FROM User u LEFT JOIN FETCH u.roles WHERE u.login =:login AND u.password =:password"),
				@NamedQuery(name ="USER.findByEmail",
						query="SELECT u FROM User u WHERE u.email =:email"),
				@NamedQuery(name ="USER.deleteByID",
						query="DELETE FROM User u WHERE u.id = :id")
		})
public class User implements Serializable {
	@Id
	@GeneratedValue(strategy=GenerationType.TABLE)
	private long id;
	
	@OneToOne(optional = false, orphanRemoval = true, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
	private UserSetting userSetting;

	@Pattern(regexp = "^[a-zA-Z0-9_-]{4,16}$")
	private String login;

	@Pattern(regexp = "^[a-zA-Z0-9_-]{4,16}$")
	private String password;

	@Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\."
			+"[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@"
			+"(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?")
	private String email;

	@NotNull
	private String phone;

	@ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
	@Enumerated(EnumType.STRING)
	private List<Role> roles = new ArrayList<>();
	
	@OneToOne(orphanRemoval = true, cascade = CascadeType.ALL)
	private Avatar avatar;
	
	@OneToOne
	private LocationGPS lastLocationGPS;
	
	@OneToOne
	private LocationNetwork lastLocationNetworkNaszaUsluga;
	
	@OneToOne
	private LocationNetwork lastLocationNetworObcaUsluga;

//	@JsonIgnore
	@OneToMany(mappedBy = "provider", orphanRemoval = true, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
	private Set<Area> areas;

	public User(){}

	public User(String login, String email, String phone) {
		this.login = login;
		this.email = email;
		this.phone = phone;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public LocationGPS getLastLocationGPS() {
		return lastLocationGPS;
	}

	public void setLastLocationGPS(LocationGPS lastLocationGPS) {
		this.lastLocationGPS = lastLocationGPS;
	}

	public Set<Area> getAreas() {
		return areas;
	}

	public void setAreas(Set<Area> polygons) {
		this.areas = polygons;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public LocationNetwork getLastLocationNetworkNaszaUsluga() {
		return lastLocationNetworkNaszaUsluga;
	}

	public void setLastLocationNetworkNaszaUsluga(
			LocationNetwork lastLocationNetworkNaszaUsluga) {
		this.lastLocationNetworkNaszaUsluga = lastLocationNetworkNaszaUsluga;
	}

	public LocationNetwork getLastLocationNetworObcaUsluga() {
		return lastLocationNetworObcaUsluga;
	}

	public void setLastLocationNetworObcaUsluga(
			LocationNetwork lastLocationNetworObcaUsluga) {
		this.lastLocationNetworObcaUsluga = lastLocationNetworObcaUsluga;
	}

	public UserSetting getUserSetting() {
		return userSetting;
	}

	public void setUserSetting(UserSetting userSetting) {
		this.userSetting = userSetting;
	}

	public Avatar getAvatar() {
		return avatar;
	}

	public void setAvatar(Avatar avatar) {
		this.avatar = avatar;
	}

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}
}
