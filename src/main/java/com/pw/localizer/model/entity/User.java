package com.pw.localizer.model.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.Pattern;
import com.pw.localizer.model.enums.Role;
import lombok.Getter;
import lombok.Setter;

//javax.persistence.loadgraph
@Entity
@Getter
@Setter
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
						query="DELETE FROM User u WHERE u.id = :id"),
				@NamedQuery(name="USER.findByIdIn",
						query="SELECT u FROM User u WHERE u.id IN :idCollection")
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

	@Pattern(regexp = "([0-9]{9})|([0-9]{3}-[0-9]{3}-[0-9]{3})|([0-9]{3} [0-9]{3} [0-9]{3})")
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

	@OneToMany(mappedBy = "provider", orphanRemoval = true, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
	private Set<Area> areas = new HashSet<>();

	public User(){}

	public User(String login, String email, String phone) {
		this.login = login;
		this.email = email;
		this.phone = phone;
	}
}
