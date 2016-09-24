package com.pw.localizer.model.entity;


import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.pw.localizer.model.enums.AreaFollow;

@Entity
@NamedEntityGraphs({
	@NamedEntityGraph(
			name = "Area.fetchAll",
			includeAllAttributes = true,
			subgraphs = @NamedSubgraph(
					name = "Polygon.path",
					attributeNodes = {@NamedAttributeNode("")}
			)
	),
	@NamedEntityGraph(

	)
})
@NamedQueries(value={
	          @NamedQuery(name="Area.updateByIdSetActive",
			             query="UPDATE Area a SET a.active =:active WHERE a.id =:areaId"),
	          @NamedQuery(name="Area.findByProviderId", 
    		              query="SELECT a FROM Area a WHERE a.provider.id = :id"),
		      @NamedQuery(name="Area.findByTargetId", 
		    		      query="SELECT a FROM Area a WHERE a.target.id = :id"),
		      @NamedQuery(name="Area.removeById",
		                  query="DELETE FROM Area a WHERE a.id = :id"),
		      @NamedQuery(name="Area.findAll",
		                  query="SELECT a FROM Area a"),
		      @NamedQuery(name="Area.findWithEagerFetchPointsAndTargetByProviderId",
		                  query="SELECT a FROM Area a JOIN FETCH a.target WHERE a.provider.id =:id"),
		      @NamedQuery(name="Area.findByAktywny",
		    		      query="SELECT a FROM Area a WHERE a.active =:aktywny"),
		      @NamedQuery(name="Area.findIdByProviderIdAndAktywny", 
		                  query="SELECT a.id FROM Area a WHERE a.provider.id =:id AND a.active =:active")
})
@XmlAccessorType(XmlAccessType.FIELD)
public class Area {
	@Id
	@GeneratedValue(strategy=GenerationType.TABLE)
	private long id;
    
	@NotNull
	@Size(min = 4, max = 16)
    @Column
	private String name;

	@XmlTransient
	@NotNull
	@ManyToOne(optional = false)
	private User target;

	@XmlTransient
	@NotNull
	@ManyToOne(optional = false)
	private User provider;

	private boolean active;

	@NotNull
	private String color;
	
	@NotNull
	@Enumerated(EnumType.STRING)
	private AreaFollow polygonFollowType;
	
	@OneToMany(mappedBy = "area", orphanRemoval = true, fetch = FetchType.LAZY,  cascade = {CascadeType.REMOVE})
	private List<AreaEventNetwork>areaEventNetworks;
	
	@OneToMany(mappedBy = "area", orphanRemoval = true, fetch = FetchType.LAZY,  cascade = {CascadeType.REMOVE})
	private List<AreaEventGPS>areaEventGPSs;
	
	@NotNull
	@OneToOne(orphanRemoval = true, cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
	private AreaMessageMail areaMessageMail;
	
	@OneToMany(mappedBy = "area", orphanRemoval = true, fetch=FetchType.LAZY, cascade = {CascadeType.ALL})
	@MapKey(name="number")
	private Map<Integer,AreaPoint>points = new HashMap<Integer, AreaPoint>();
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Map<Integer, AreaPoint> getPoints() {
		return points;
	}

	public void setPoints(Map<Integer, AreaPoint> points) {
		this.points = points;
	}

	public User getTarget() {
		return target;
	}

	public void setTarget(User target) {
		this.target = target;
	}

	public User getProvider() {
		return provider;
	}

	public void setProvider(User provider) {
		this.provider = provider;
	}

	public AreaFollow getAreaFollowType() {
		return polygonFollowType;
	}

	public void setAreaFollowType(AreaFollow polygonFollowType) {
		this.polygonFollowType = polygonFollowType;
	}

	public List<AreaEventNetwork> getAreaEventNetworks() {
		return areaEventNetworks;
	}

	public void setAreaEventNetworks(List<AreaEventNetwork> areaEventNetworks) {this.areaEventNetworks = areaEventNetworks;}

	public List<AreaEventGPS> getAreaEventGPSs() {
		return areaEventGPSs;
	}

	public void setAreaEventGPSs(List<AreaEventGPS> areaEventGPSs) {
		this.areaEventGPSs = areaEventGPSs;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean aktywny) {
		this.active = aktywny;
	}

	public AreaFollow getPolygonFollowType() {
		return polygonFollowType;
	}

	public void setPolygonFollowType(AreaFollow polygonFollowType) {
		this.polygonFollowType = polygonFollowType;
	}

	public AreaMessageMail getAreaMessageMail() {
		return areaMessageMail;
	}

	public void setAreaMessageMail(AreaMessageMail areaMessageMail) {
		this.areaMessageMail = areaMessageMail;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}
	
	public boolean contains(Location location){
		Path2D path2d = createPath(getPoints());
		Point2D point2d = new Point2D.Double(location.getLatitude(), location.getLongitude());
		return path2d.contains(point2d);
	}
	
	private Path2D createPath(Map<Integer, AreaPoint>points){
		Path2D path = new Path2D.Double();
		
		AreaPoint firstPoint = points.get(0);
		path.moveTo(firstPoint.getLat(), firstPoint.getLng());
		
		for(int index = 1; index < points.size(); index++){
			AreaPoint point = points.get(index);
			path.lineTo(point.getLat(), point.getLng());
		}		
		
		return path;
	}
}
