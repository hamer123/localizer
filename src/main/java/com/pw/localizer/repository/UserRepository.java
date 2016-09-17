package com.pw.localizer.repository;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import javax.ejb.Local;

import com.pw.localizer.model.entity.Location;
import com.pw.localizer.model.entity.User;
@Local
public interface UserRepository extends JpaRepository<User, Long>, Serializable {
	User findByLoginAndPassword(String login, String password);
	User findByLogin(String login);
//	List<User> findByLogin(List<String> getLogins);
//	User findByLoginFetchArea(String login);
    List<String> findLoginByLoginLike(String loginLike);
//    List<User> findByIds(Set<Long>id);
    User findByEmail(String email);
	boolean isLoginExist(String login);

	Location findLastGpsLocationByUserId(long id);
	Location findLastNetworkNaszLocationByUserId(long id);
	Location findLastNetworkObcyLocationByUserId(long id);
}
