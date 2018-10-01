function getLocation() {
        if (navigator.geolocation) {
            navigator.geolocation.getCurrentPosition(setPositionInputs);
        } /*else {
            console.log("Geolocation is not supported by this browser.");
        }*/
    }

function setPositionInputs(position) {
  $('#latitude_input').val(position.coords.latitude);
  $('#longitude_input').val(position.coords.longitude);
}
