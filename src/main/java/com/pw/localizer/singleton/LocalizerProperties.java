package com.pw.localizer.singleton;

import javax.annotation.PostConstruct;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.pw.localizer.exception.PropertiesNotFound;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.jboss.logging.Logger;

import java.io.File;

@Startup
@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
public class LocalizerProperties {
	@Inject
	private Logger logger;

	/** Configuration contains all properties from localizer.properties */
	private Configuration configurationLocalizer;

	/** Properties for marker */
	private Marker marker;

	/** Properties for google map */
	private GoogleMapDefault GoogleMapDefault;

	/** Properties for circle */
	private Circle circle;

	/** Properties for polygon */
	private Polygon polygon;

	/** Properties for polyline */
	private Polyline polyline;

	/** Resource Paths */
	private ResourcePath resourcePath;

	@PostConstruct
	public void postConstruct(){
		try {
			Configurations configs = new Configurations();
			configurationLocalizer = configs.properties(new File("localizer.properties"));
			marker = new Marker();
			circle = new Circle();
			polygon = new Polygon();
			polyline = new Polyline();
			GoogleMapDefault = new GoogleMapDefault();
			resourcePath = new ResourcePath();
		} catch (ConfigurationException e) {
			logger.error(e.getMessage(), e);
			//we have to throw it because we do not want application to start
			throw new RuntimeException(e);
		}
	}

	public String getString(String name) {
		String value = configurationLocalizer.getString(name);
		if(value == null || value.equals("")) {
			throw new PropertiesNotFound("Properties has not been found " + name);
		}
		return value;
	}

	public int getInt(String name) {
		String value = configurationLocalizer.getString(name);
		if(value == null || value.equals("")) {
			throw new PropertiesNotFound("Properties has not been found " + name);
		}
		return configurationLocalizer.getInt(name);
	}

	public double getDouble(String name) {
		String value = configurationLocalizer.getString(name);
		if(value == null || value.equals("")) {
			throw new PropertiesNotFound("Properties has not been found " + name);
		}
		return configurationLocalizer.getDouble(name);
	}

	public Marker Marker() {
		return marker;
	}

	public GoogleMapDefault GoogleMapDefault() {
		return GoogleMapDefault;
	}

	public Circle Circle() {
		return circle;
	}

	public Polygon Polygon() {
		return polygon;
	}

	public Polyline Polyline() {
		return polyline;
	}

	public ResourcePath ResourcePath() {
		return resourcePath;
	}

	// MARKER
	public class Marker {
		public final String GPS;
		public final String NETWORK_OBCY;
		public final String NETWORK_NASZ;
		public final String START_ROUTE;
		public final String END_ROUTE;

		private Marker() {
			GPS = getString("marker.icon.gps.url");
			NETWORK_NASZ = getString("marker.icon.network.nasz.url");
			NETWORK_OBCY  = getString("marker.icon.network.obcy.url");
			START_ROUTE = getString("marker.icon.start.route.url");
			END_ROUTE = getString("marker.icon.end.route.url");
		}
	}

	// CIRCLE
	public class Circle {
		public final String GPS_COLOR;
		public final String NETWORK_NASZ_COLOR;
		public final String NETWORK_OBCY_COLOR;
		public final String GPS_STROKE_COLOR;
		public final String NETWORK_NASZ_STROKE_COLOR;
		public final String NETWORK_OBCY_STROKE_COLOR;
		public final double STROKE_OPACITY;
		public final double FILL_OPACITY;

		private Circle() {
			GPS_COLOR = getString("circle.gps.color");
			NETWORK_NASZ_COLOR = getString("circle.network.nasz.color");
			NETWORK_OBCY_COLOR = getString("circle.network.obcy.color");
			GPS_STROKE_COLOR = getString("circle.gps.stroke.color");
			NETWORK_NASZ_STROKE_COLOR = getString("circle.network.nasz.stroke.color");
			NETWORK_OBCY_STROKE_COLOR = getString("circle.network.obcy.stroke.color");
			STROKE_OPACITY = getDouble("circle.stroke.opacity");
			FILL_OPACITY = getDouble("circle.fill.opacity");
		}
	}

	//POLYGON
	public class Polygon {
		public final String STROKE_COLOR;
		public final String FILL_COLOR;
		public final double FILL_OPACITY;
		public final double STROKE_OPACITY;

		private Polygon() {
			STROKE_COLOR = getString("polygon.stroke.color");
			FILL_COLOR = getString("polygon.fill.color");
			FILL_OPACITY = getDouble("polygon.fill.opacity");
			STROKE_OPACITY = getDouble("polygon.stroke.opacity");
		}
	}

	//POLYLINE
	public class Polyline {
		public final String GPS_COLOR;
		public final String NETWORK_NASZ_COLOR;
		public final String NETWORK_OBCY_COLOR;
		public final double STROKE_OPACITY;
		public final int STROKE_WEIGHT;

		private Polyline() {
			GPS_COLOR = getString("polyline.gps.color");
			NETWORK_NASZ_COLOR = getString("polyline.network.nasz.color");
			NETWORK_OBCY_COLOR = getString("polyline.network.obcy.color");
			STROKE_OPACITY = getDouble("polyline.stroke.opacity");
			STROKE_WEIGHT = getInt("polyline.stroke.weight");
		}
	}

	// GOOGLE MAP SETTING
	public class GoogleMapDefault {
		public final int DEFAULT_ZOOM;
		public final String DEFAULT_CENTER;

		private GoogleMapDefault() {
			DEFAULT_CENTER = getString("google.map.center.default");
			DEFAULT_ZOOM = getInt("google.map.zoom.default");
		}
	}

	//RESOURCE PATH
	public class ResourcePath {
		public final String RESOURCE_MAIN;
		//roots
		public final String IMAGE;
		public final String VIDEO;
		//imgage
		public final String AVATAR;

		private ResourcePath() {
			String homePath = System.getProperty("user.home");
			RESOURCE_MAIN = homePath + getString("resource.path.home");
			//roots
			IMAGE = RESOURCE_MAIN + getString("resource.path.image");
			VIDEO = RESOURCE_MAIN + getString("resource.path.video");
			//imgage
			AVATAR = IMAGE + getString("resource.path.avatar");
		}
	}
}
