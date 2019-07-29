CREATE TABLE "userIDs" (
	"username"	TEXT NOT NULL PRIMARY KEY,
	"nickname" TEXT NOT NULL,
	"password"	TEXT NOT NULL,
	"salt"	BLOB NOT NULL,
	"homeUniId" INTEGER, 
	"admin"	INTEGER NOT NULL CHECK("admin" = 0 or ("admin" = 1))
);
CREATE TABLE "university" (
	"uniId"	INTEGER NOT NULL PRIMARY KEY,
	"uniName"	TEXT NOT NULL
);
CREATE TABLE "address" (
	"addressId"	INTEGER NOT NULL PRIMARY KEY,
	"address"	TEXT NOT NULL,
	"postalCode"	INTEGER NOT NULL,
	"city"	TEXT NOT NULL
);
CREATE TABLE "restaurant" (
	"restaurantId"	INTEGER NOT NULL PRIMARY KEY,
	"restaurantName"	TEXT NOT NULL,
	"addressId"	INTEGER,
	"uniId"	INTEGER,
	"isEnabled" INTEGER CHECK("isEnabled" = 0 or ("isEnabled" = 1)), 
	FOREIGN KEY("uniId") REFERENCES "university"("uniId") ON DELETE CASCADE,
	FOREIGN KEY("addressId") REFERENCES "address"("addressId") ON DELETE CASCADE
);
CREATE TABLE "chef" (
	"chefId"	INTEGER NOT NULL PRIMARY KEY,
	"firstName"	TEXT NOT NULL,
	"lastName"	TEXT NOT NULL
);
CREATE TABLE "food" (
	"foodId"	INTEGER NOT NULL PRIMARY KEY,
	"foodPrice"	REAL,
	"foodName"	TEXT NOT NULL,
	"date"	TEXT NOT NULL,
	"restaurantId"	INTEGER,
	"chefId"	INTEGER,
	FOREIGN KEY("chefId") REFERENCES "chef"("chefId"),
	FOREIGN KEY("restaurantId") REFERENCES "restaurant"("restaurantId") ON DELETE CASCADE
);
CREATE TABLE "review" (
	"reviewId"	INTEGER NOT NULL PRIMARY KEY,
	"review"	TEXT NOT NULL,
	"stars"	REAL NOT NULL,
	"username"	INTEGER,
	"foodId"	INTEGER,
	FOREIGN KEY("foodId") REFERENCES "food"("foodId") ON DELETE CASCADE,
	FOREIGN KEY("username") REFERENCES "userIds"("username")
);

INSERT INTO "userIds" VALUES('admin', 'Superadmin', 'apiosdjihujwkoaiesurhgefijwkoaijsuhrdiuwe3741b3j4uuahjdf1j123j12','HX24 23JS 22JJ', 1, 1);
INSERT INTO "userIds" VALUES('johan1954', 'Juhani', 'asoidhjsidfohsoidhfoahishdiu123iu4h198e7wydvfhuiw3q7rhdusd1n3hb3','t3u8934tre78yuireguhir', 1, 1);

INSERT INTO "university" ("uniName") VALUES ('Lappeenrannan-Lahden Teknillinen Yliopisto LUT');

INSERT INTO "chef" ("firstName", "lastName") VALUES ('Pekka', 'Rautavaara');

INSERT INTO "address" ("address", "postalCode", "city") VALUES ('Skinnarilankatu 28', 53950, 'Lappeenranta');

INSERT INTO "restaurant" ("restaurantName", "uniId", "addressId") 
VALUES ('Aalef - Meidän ravintola', 
1, 
(SELECT "addressId" FROM "address" WHERE ("postalCode" = 53950 AND "address" = 'Skinnarilankatu 28'))); 

INSERT INTO "food" ("foodName", "foodPrice", "date", "restaurantId", "chefId")
VALUES ('Lihapulla-tomaattikastike ja spagetti', 2.60, '23.7.2019', 
(SELECT "restaurantId" FROM "restaurant" WHERE "restaurantName" = 'Aalef - Meidän ravintola'),
(SELECT "chefId" FROM "chef" WHERE ("firstName" = 'Pekka' AND "lastName" = 'Rautavaara')));

INSERT INTO "review" ("review", "stars", "foodId", "username")
VALUES ('Ruoka oli oikein hyvän makuista. Suolaa tuntui olevan kuitenkin hieman liikaa.', 3.5, 
(SELECT "foodId" FROM "food" WHERE "foodName" = 'Lihapulla-tomaattikastike ja spagetti'), 
'johan1954');