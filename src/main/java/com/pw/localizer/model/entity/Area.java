package com.pw.localizer.model.entity;

import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pw.localizer.model.enums.AreaFollow;
import lombok.Getter;
import lombok.Setter;

@NamedEntityGraphs({
	@NamedEntityGraph(name = "graph.Area.areaPoints",
			attributeNodes = {@NamedAttributeNode(value = "points", subgraph = "points")})
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
		      @NamedQuery(name= "Area.findByProviderIdEagerFetchPoints",
		                  query="SELECT a FROM Area a WHERE a.provider.id =:id"),
		      @NamedQuery(name="Area.findByAktywny",
		    		      query="SELECT a FROM Area a WHERE a.active =:aktywny"),
		      @NamedQuery(name="Area.findIdByProviderIdAndAktywny", 
		                  query="SELECT a.id FROM Area a WHERE a.provider.id =:id AND a.active =:active"),
		      @NamedQuery(name="Area.findAreaPointsByAreaId",
			              query ="SELECT a.points FROM Area a WHERE a.id =:id")
})
@Entity
@Getter
@Setter
public class Area {
	@Id
	@GeneratedValue(strategy=GenerationType.TABLE)
	private long id;
    
	@NotNull
	@Size(min = 4, max = 16)
    @Column
	private String name;

	@NotNull
	@ManyToOne(optional = false)
	private User target;

	@JsonIgnore
	@NotNull
	@ManyToOne(optional = false)
	private User provider;

	private boolean active;

	@NotNull
	private String color;
	
	@NotNull
	@Enumerated(EnumType.STRING)
	private AreaFollow areaFollowType;

	@OneToMany(orphanRemoval = true, fetch=FetchType.LAZY, cascade = {CascadeType.ALL})
	@OrderBy(value = "number ASC")
	@JoinColumn
	private List<AreaPoint> points = new ArrayList();

	@NotNull
	@OneToOne(orphanRemoval = true, cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
	private AreaMessageMail areaMessageMail;

	public boolean contains(Location location){
		Path2D path2d = createPath(getPoints());
		Point2D point2d = new Point2D.Double(location.getLatitude(), location.getLongitude());
		return path2d.contains(point2d);
	}
	
	private Path2D createPath(List<AreaPoint>points){
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
