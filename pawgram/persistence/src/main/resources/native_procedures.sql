CREATE OR REPLACE FUNCTION get_post_by_range (lat1 double precision,
    lon1 double precision,
    range INTEGER) 
 RETURNS TABLE (
    postId BIGINT,
    title    VARCHAR,
    description VARCHAR,
    contact_phone    VARCHAR(32),
    event_date TIMESTAMP,
    category VARCHAR,
    pet VARCHAR,
    is_male BOOLEAN,
    latitude double precision,
    longitude double precision,
    userId BIGINT
) 
AS
'BEGIN
 RETURN QUERY SELECT ss.postId,
    ss.title,
    ss.description,
    ss.contact_phone,
    ss.event_date,
    ss.category,
    ss.pet,
    ss.is_male,
    ss.latitude,
    ss.longitude,
    ss.userId FROM
  (SELECT *,haversine_distance(lat1,lon1,p.latitude,p.longitude) as distance FROM posts AS p) 
 	 ss WHERE distance <= range ORDER BY distance DESC; 
END
'
 
LANGUAGE 'plpgsql';