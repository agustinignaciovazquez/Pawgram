$( document ).ready(function() {
     $('.leftmenutrigger').on('click', function(e) {
     $('.side-nav').toggleClass("open");
     e.preventDefault();
    });

    $('.radio-group .radio').click(function(){
        $(this).parent().find('.radio').removeClass('selected');
        $(this).addClass('selected');
        var val = $(this).attr('data-value');
        //alert(val);
        $(this).parent().find('input').val(val);
    });


    $('#myCarousel').carousel({
        interval: 5000
    });
 
    $('#carousel-text').html($('#slide-content-0').html());
 
    //Handles the carousel thumbnails
    $('[id^=carousel-selector-]').click( function(){
        var id = this.id.substr(this.id.lastIndexOf("-") + 1);
        var id = parseInt(id);
        $('#myCarousel').carousel(id);
    });
 
 
        // When the carousel slides, auto update the text
    $('#myCarousel').on('slid.bs.carousel', function (e) {
        var id = $('.item.active').data('slide-number');
        $('#carousel-text').html($('#slide-content-'+id).html());
    });    

    $('.pass_show').append('<span class="ptxt">Mostrar</span>');  


    

});

      

    $(document).on('click','.pass_show .ptxt', function(){ 

    $(this).text($(this).text() == "Mostrar" ? "Ocultar" : "Mostrar"); 

    $(this).prev().attr('type', function(index, attr){return attr == 'password' ? 'text' : 'password'; }); 

    });  

    function displayLocation(latitude,longitude,elem,msg){
        var request = new XMLHttpRequest();

        var method = 'GET';
        var url = 'https://maps.googleapis.com/maps/api/geocode/json?latlng='+latitude+','+longitude+'&sensor=true&key=AIzaSyAsqLEThGLQ6T4Ayox_K7Em1S4DuAT-wm8';
        var async = true;

        request.open(method, url, async);
        request.onreadystatechange = function(){
          if(request.readyState == 4 && request.status == 200){
            var data = JSON.parse(request.responseText);
            var address = data.results[0];
            $(elem).html(msg + ' ' +address.formatted_address);
          }
        };
        request.send();
    };

    function showZoneNames(msg){
        $(".zonetext,.zonetext2").each(function(){
              var latZone = $(this).find(".latitudeZone").val();
              var lonZone = $(this).find(".longitudeZone").val();
              displayLocation(latZone,lonZone,this,msg);
            });
    }
    function noenter() {
        return !(window.event && window.event.keyCode == 13);
    }  
    
   function showAlert(classy,message) {
      $(classy).find('.alertmsg').html(" "+message);
      $(classy).removeClass('hide').addClass('show');
    }

