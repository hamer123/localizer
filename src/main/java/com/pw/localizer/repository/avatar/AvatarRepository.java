package com.pw.localizer.repository.avatar;

import javax.ejb.Local;

import com.pw.localizer.model.entity.Avatar;
import com.pw.localizer.repository.JpaRepository;

@Local
public interface AvatarRepository extends JpaRepository<Avatar, Long> {

}
