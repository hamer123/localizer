package com.pw.localizer.service;

import javax.ejb.Local;

import com.pw.localizer.model.entity.AreaEvent;

@Local
public interface AreaMessageService {
	void sendMessage(AreaEvent areaEvent);
}
