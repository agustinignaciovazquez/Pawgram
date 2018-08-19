-- Haversine Formula based geodistance in miles (constant is diameter of Earth in miles)
-- Based on a similar PostgreSQL function found here: https://gist.github.com/831833
-- Updated to use distance formulas found here: http://www.codecodex.com/wiki/Calculate_distance_between_two_points_on_a_globe
CREATE OR REPLACE FUNCTION distance(
    lat1 double precision,
    lon1 double precision,
    lat2 double precision,
    lon2 double precision)
  RETURNS double precision AS
'DECLARE
R integer = 6371e3; -- Meters
rad double precision = 0.01745329252;
φ1 double precision = lat1 * rad;
φ2 double precision = lat2 * rad;
Δφ double precision = (lat2-lat1) * rad;
Δλ double precision = (lon2-lon1) * rad;
a double precision = sin(Δφ/2) * sin(Δφ/2) + cos(φ1) * cos(φ2) * sin(Δλ/2) * sin(Δλ/2);
c double precision = 2 * atan2(sqrt(a), sqrt(1-a));    
BEGIN                                                     
    RETURN R * c;        
END  
'
LANGUAGE plpgsql VOLATILE
COST 100;