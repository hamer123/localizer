/**
 * Created by Patryk on 2016-10-06.
 */

var gmap_sizes = ['min','max'];
var gmap_size = gmap_sizes[0];

/** change gmap height on menu toggle */
function changeGMapHeight(){
    if(gmap_size == gmap_sizes[0]){
        setGMapHeightToMax();
        gmap_size = gmap_sizes[1];
    } else {
        setGMapHeightToMin();
        gmap_size = gmap_sizes[0];
    }
}

/** change GMap to min height */
function setGMapHeightToMax(){
    var height = $(window).height() - 180;
    setGMapHeight(height);
}

/** change GMap to max height */
function setGMapHeightToMin(){
    setGMapHeight(500);
}

/** Set GMap height */
function setGMapHeight(height){
    var gmap = PF('gmap').getMap();
    var myDiv = gmap.getDiv();
    myDiv.style.height = height.toString().concat("px");
    google.maps.event.trigger(gmap, 'resize');
}

/** Refresh after poll to not change prefer stats*/
function onPollComplete(){
    if(gmap_size == gmap_sizes[0]){
        setGMapHeight(500);
    } else {
        setGMapHeightToMax();
    }
}