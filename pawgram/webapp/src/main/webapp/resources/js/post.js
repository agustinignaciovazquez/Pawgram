var total_img = 1;
function hideImagesDiv(){
	$('.image-div').each(function(index){
		if(index >= total_img){
			$( this ).hide();
		}else{
            $( this ).show();
        }
	});
}
function hideButtonFixPeruano(){
	$('.image-div').each(function(){
	  		$(this).find('.add-img').hide();
	  	});
	if($('.image-div:hidden:last').length){
  		$('.image-div:visible:last').find('.add-img').show();
  	}
}

$(document).ready(function(){
    //set input in form is refreshed
    if($(".pet-value").val().length){
        $("#type-"+$(".pet-value").val()).addClass("selected");
       
    }
    
     if($(".sex-value").val().length){

        if($(".sex-value").val() == "true"){
            $("#sex-male").addClass("selected");
        }else{
            $("#sex-female").addClass("selected");    
        }
        
    }
    //plus and minus button functionality
	hideImagesDiv();
    hideButtonFixPeruano();
	$( ".add-img" ).click(function() {
		if($('.image-div:hidden:last').length){
			  $('.image-div:hidden:last').show();
			  hideButtonFixPeruano();
			  total_img++;
		}
	});
	$( ".remove-img" ).click(function() {
	  if(total_img > 1){
	  	$(this).parents('.image-div').hide();
        $(this).siblings('.image-input').val('');
	  	hideButtonFixPeruano();
		total_img--;
		}
	});
});

var latitude = parseFloat(document.getElementById('lat').value);
var longitude = parseFloat(document.getElementById('lon').value);
function initMap() {
	
	if(isNaN(latitude) || isNaN(longitude)){
		latitude = -34.6031933;
		longitude = -58.3677853;
	}else{
		var uluru = {lat: latitude, lng: longitude};
	}
var map = new google.maps.Map(document.getElementById('map'), {
  center: {lat: latitude, lng: longitude},
  zoom: 13
});
var input = document.getElementById('searchInput');
map.controls[google.maps.ControlPosition.TOP_LEFT].push(input);

var autocomplete = new google.maps.places.Autocomplete(input);
autocomplete.bindTo('bounds', map);

var infowindow = new google.maps.InfoWindow();
if (typeof uluru !== 'undefined') {
	var marker = new google.maps.Marker({position: uluru, map: map});
}else{
    var marker = new google.maps.Marker({
        map: map,
        anchorPoint: new google.maps.Point(0, -29)
    });
}

autocomplete.addListener('place_changed', function() {
    infowindow.close();
    marker.setVisible(false);
    var place = autocomplete.getPlace();
    if (!place.geometry) {
        showAlert('.alert-danger:first','<spring:message code="nogeometry"/>');  
        return;
    }

    // If the place has a geometry, then present it on a map.
    if (place.geometry.viewport) {
        map.fitBounds(place.geometry.viewport);
    } else {
        map.setCenter(place.geometry.location);
        map.setZoom(17);
    }
    marker.setIcon(({
        url: place.icon,
        size: new google.maps.Size(71, 71),
        origin: new google.maps.Point(0, 0),
        anchor: new google.maps.Point(17, 34),
        scaledSize: new google.maps.Size(35, 35)
    }));
    marker.setPosition(place.geometry.location);
    marker.setVisible(true);

    var address = '';
    if (place.address_components) {
        address = [
          (place.address_components[0] && place.address_components[0].short_name || ''),
          (place.address_components[1] && place.address_components[1].short_name || ''),
          (place.address_components[2] && place.address_components[2].short_name || '')
        ].join(' ');
    }

    infowindow.setContent('<div><strong>' + place.name + '</strong><br>' + address);
    infowindow.open(map, marker);
  
    //Location details
   /* for (var i = 0; i < place.address_components.length; i++) {
        if(place.address_components[i].types[0] == 'postal_code'){
            document.getElementById('postal_code').innerHTML = place.address_components[i].long_name;
        }
        if(place.address_components[i].types[0] == 'country'){
            document.getElementById('country').innerHTML = place.address_components[i].long_name;
        }
    }*/
    //document.getElementById('location').innerHTML = place.formatted_address;
    document.getElementById('lat').value  = place.geometry.location.lat();
    document.getElementById('lon').value  = place.geometry.location.lng();

});

}