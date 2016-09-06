package com.pw.localizer.repository;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import javax.ejb.Local;

import com.pw.localizer.model.entity.User;
@Local
public interface UserRepository extends JpaRepository<User, Long>, Serializable {
	User findUserFeatchRolesByLoginAndPassword(String login, String password);
	User findByLogin(String login);
	List<User> findByLogin(List<String> logins);
	User findByLoginFetchArea(String login);
    List<String> findLoginByLoginLike(String loginLike);
    List<User> findByIds(Set<Long>id);
    User findByEmail(String email);
	boolean isLoginExist(String login);
}
