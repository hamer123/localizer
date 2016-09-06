package com.pw.localizer.repository;

import javax.ejb.Local;

import com.pw.localizer.model.entity.Avatar;

@Local
public interface AvatarRepository extends JpaRepository<Avatar, Long>{

}
