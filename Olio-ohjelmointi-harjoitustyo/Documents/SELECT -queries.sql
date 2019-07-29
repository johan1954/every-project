SELECT * FROM "university";

SELECT * FROM "restaurant" INNER JOIN "address"
ON "restaurant"."addressId" = "address"."addressId"
WHERE "uniId" = ?;

SELECT "foodName", "foodPrice", "restaurantId", "foodId" 
FROM "food";

SELECT "foodName" FROM "food" WHERE "date" = ? AND "restaurantId" = ?;

SELECT "reviewId", "review", "stars", "username", "review"."foodId", "food"."date"  
FROM "review" INNER JOIN "food" 
ON "review"."foodId" = "food"."foodId" 
WHERE "food"."foodName" = ?;

SELECT * FROM "review"
WHERE "username" = ?;

SELECT * FROM "userIDs";

SELECT "admin" FROM "userIDs"
WHERE "username" = ?;

SELECT * FROM "food"
WHERE "restaurantId" = ?
ORDER BY  "SUBSTR('date', 4, 2), SUBSTR('date', 1, 2)";

SELECT * FROM "review"
INNER JOIN "food"
ON "review"."foodId" = "food"."foodId" 
INNER JOIN "chef" 
ON "chef"."chefId" = "food"."chefId"
WHERE "chef"."firstName" = ? AND "chef"."lastName" = ?;
