package com.pw.localizer.repository.user;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import javax.ejb.Local;

import com.pw.localizer.model.entity.Location;
import com.pw.localizer.model.entity.User;
import com.pw.localizer.model.utilitis.UserAdvanceSearch;
import com.pw.localizer.repository.JpaRepository;

@Local
public interface UserRepository extends JpaRepository<User, Long>, Serializable {
	User findByLoginAndPassword(String login, String password);
	User findByLogin(String login);
    List<String> findLoginByLoginLike(String loginLike);
    User findByEmail(String email);
	boolean isLoginExist(String login);
	Location findLastGpsLocationByUserId(long id);
	Location findLastNetworkNaszLocationByUserId(long id);
	Location findLastNetworkObcyLocationByUserId(long id);
	List<User> findByLoginLikeAndEmailLikeAndPhoneLike(UserAdvanceSearch userAdvanceSearch);
}
