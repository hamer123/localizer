package com.pw.localizer.service;

import javax.ejb.Local;

import com.pw.localizer.model.google.component.Route;

@Local
public interface RouteService {

	double calculateLenghtMeters(Route route);
}
