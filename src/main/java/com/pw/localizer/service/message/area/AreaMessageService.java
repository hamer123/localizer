package com.pw.localizer.service.message.area;

import javax.ejb.Local;

import com.pw.localizer.model.entity.AreaEvent;

@Local
public interface AreaMessageService {
	void sendMessage(AreaEvent areaEvent);
}
