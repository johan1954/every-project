package com.example.foodreview;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.foodreview.UserIdContract.*;
import java.util.ArrayList;

class DatabaseManager {

    private static DatabaseManager instance = null;
    private SQLiteDatabase db;
    private PasswordEncryptor encryptor;
    private Cursor databaseCursor;
    private int userIndex;

    // Singleton has a private constructor
    private DatabaseManager(Context context) {

        LoginDatabaseHelper fDBHelp = new LoginDatabaseHelper(context);
        db = fDBHelp.getWritableDatabase();
        databaseCursor = getCursor(tableUserIds.TABLE_NAME);
        encryptor = PasswordEncryptor.getInstance();
    }

    // This class functions as a singleton and always returns the same instance
    static DatabaseManager getInstance(Context newContext) {
        if (instance == null) {
            instance = new DatabaseManager(newContext);
        }
        else {

            System.out.println("Instance already exists");
        }
        return instance;
    }

    // This method takes given username and password to insert one new value to the database.
    // Does not return anything at the moment.
    void addItem(String username, String password, String nickname, int homeUniId) {

        byte[] salt = encryptor.getSalt(username);
        String hash = encryptor.encryptor(password, salt);

        ContentValues cv = new ContentValues();
        cv.put(tableUserIds.COLUMN_USERNAME, username);
        cv.put(tableUserIds.COLUMN_NICKNAME, nickname);
        cv.put(tableUserIds.COLUMN_PASSWORD, hash);
        cv.put(tableUserIds.COLUMN_SALT, salt);
        cv.put(tableUserIds.COLUMN_ADMIN, 0);
        cv.put(tableUserIds.COLUMN_HOMEUNIID, homeUniId);

        if (db.insert(tableUserIds.TABLE_NAME, null, cv) >= 0) {
            System.out.print(username);
        }
        System.out.println(" This username has been set.");


    }

    // Takes three strings. Uses username and oldPassword to confirm the user.
    // Updates the password hash using the primary key username as a Where clause.
    boolean changePassword(String username, String newPassword) {

        databaseCursor.moveToPosition(userIndex);
        byte[] thisSalt = databaseCursor.getBlob(databaseCursor.getColumnIndex(tableUserIds.COLUMN_SALT));
        String newHash = encryptor.encryptor(newPassword, thisSalt);
        ContentValues cv = new ContentValues();
        cv.put(tableUserIds.COLUMN_PASSWORD,newHash);

        String whereClause = tableUserIds.COLUMN_USERNAME.concat("=?");
        String[] whereArgs = {username};

        if (db.update(tableUserIds.TABLE_NAME, cv, whereClause,whereArgs) > 0) {
            return true;
        }
        else {
            System.out.println("Database update failed!");
            return false;
        }
    }

    // Method takes given username and password. Uses checkExistance -method to find the index.
    // Returns true if values match.
    boolean searchDatabase (String username, String password) {

        databaseCursor = getCursor(tableUserIds.TABLE_NAME);
        if (checkExistance(username)) {
            //uses class variable to move cursor to the position, where the username was located.
            // Removes the need for another for -loop.
            databaseCursor.moveToPosition(userIndex);
            //System.out.println(userIndex);
            String dbPassWord = databaseCursor.getString(databaseCursor.getColumnIndex(tableUserIds.COLUMN_PASSWORD));
            byte[] thisSalt = databaseCursor.getBlob(databaseCursor.getColumnIndex(tableUserIds.COLUMN_SALT));
            String newHash = encryptor.encryptor(password, thisSalt);

            return newHash.equals(dbPassWord);
        }
        else {
            return false;
        }
    }

    void updateUsers() {
        databaseCursor = getCursor(tableUserIds.TABLE_NAME);
        ArrayList<User> newUsers = new ArrayList<>();
        UserManager userManager = UserManager.getInstance();

        int count = databaseCursor.getCount();

        for (int x = 0; x < count; x++) {
            databaseCursor.moveToPosition(x);
            String newUsername = databaseCursor.getString(databaseCursor.getColumnIndex(tableUserIds.COLUMN_USERNAME));
            String newNickname = databaseCursor.getString(databaseCursor.getColumnIndex(tableUserIds.COLUMN_NICKNAME));
            int newIsAdmin = databaseCursor.getInt(databaseCursor.getColumnIndex(tableUserIds.COLUMN_ADMIN));
            int newHomeUniId = databaseCursor.getInt(databaseCursor.getColumnIndex(tableUserIds.COLUMN_HOMEUNIID));

            User newUser = new User(newUsername, newNickname, newIsAdmin, newHomeUniId);
            newUsers.add(newUser);
        }

        userManager.setUsers(newUsers);

    }

    //Returns a user object to a single user
    User getOwnUser(String username) {

        String whereClause = tableUserIds.COLUMN_USERNAME + " = ?";
        String[] whereArgs = {username};
        databaseCursor = getCursorWithWhere(tableUserIds.TABLE_NAME, whereClause, whereArgs);

        databaseCursor.moveToFirst();

        String newUsername = databaseCursor.getString(databaseCursor.getColumnIndex(tableUserIds.COLUMN_USERNAME));
        String newNickname = databaseCursor.getString(databaseCursor.getColumnIndex(tableUserIds.COLUMN_NICKNAME));
        int newIsAdmin = databaseCursor.getInt(databaseCursor.getColumnIndex(tableUserIds.COLUMN_ADMIN));
        int newHomeUniId = databaseCursor.getInt(databaseCursor.getColumnIndex(tableUserIds.COLUMN_HOMEUNIID));

        return new User(newUsername, newNickname, newIsAdmin, newHomeUniId);
    }

    // Method to update nickname for the given username
    void modifyNickname (String username, String nickname) {

        ContentValues cv = new ContentValues();
        String whereClause = tableUserIds.COLUMN_USERNAME + " = ?";
        String[] whereArgs = {username};

        cv.put(tableUserIds.COLUMN_NICKNAME, nickname);

        if (db.update(tableUserIds.TABLE_NAME, cv, whereClause, whereArgs) <= 0) {
            System.out.println("Modifying failed.");
        }
    }


    // Admins ability to change other users data. Includes giving admin priviledges and modifying
    // users home University. Also checks if an admin is trying to remove his own admin rights!
    String modifyUser(String currentUser, String username, boolean isAdmin) {


        if (currentUser.equals(username) && (!isAdmin)) {
            System.out.println("adminError");
            return "adminError";
        }

        ContentValues cv = new ContentValues();
        String whereClause = tableUserIds.COLUMN_USERNAME + " = ?";
        String[] whereArgs = {username};

        int isAdminInt;
        if (isAdmin) {
            isAdminInt = 1;
        }
        else {
            isAdminInt = 0;
        }
        cv.put(tableUserIds.COLUMN_ADMIN, isAdminInt);

        if (db.update(tableUserIds.TABLE_NAME, cv, whereClause, whereArgs) <= 0) {
            System.out.println("Modifying failed.");
            return "databaseError";
        }
        return "success";
    }

    // Method for the user himself to update own home university.
    // Created as an own method to limit bugs or other leaks that might cause unwanted changes in
    // the user data.
    void updateNewHomeUni (int newHomeUniId, String username) {
        ContentValues cv = new ContentValues();
        String whereClause = tableUserIds.COLUMN_USERNAME + " = ?";
        String[] whereArgs = {username};
        cv.put(tableUserIds.COLUMN_HOMEUNIID, newHomeUniId);
        if (db.update(tableUserIds.TABLE_NAME, cv, whereClause, whereArgs) <= 0) {
            System.out.println("Modifying failed.");
        }
    }

    // Looks through the database and looks for an existing username. Returns true if a username exists in the Database.
    // Also sets a class variable index to the position of the located username.
    boolean checkExistance (String username) {
        databaseCursor = getCursor(tableUserIds.TABLE_NAME);
        databaseCursor.moveToFirst();
        int count = databaseCursor.getCount();

        for (int x = 0; x < count; x++) {

            databaseCursor.moveToPosition(x);
            String dbUserName = databaseCursor.getString(databaseCursor.getColumnIndex(tableUserIds.COLUMN_USERNAME));
            //System.out.println(x);
            if (dbUserName.equals(username)) {
                System.out.println(x);
                userIndex = x;
                return true;
            }
        }
        return false;
    }

    // Looks through the database to check if a string exists in the database.
    // Also takes table name and column.
    private boolean checkStringExistance (String search, String tableName, String column) {
        databaseCursor = getCursor(tableName);
        databaseCursor.moveToFirst();
        int count = databaseCursor.getCount();

        for (int x = 0; x < count; x++) {
            databaseCursor.moveToPosition(x);
            String dbResult = databaseCursor.getString(databaseCursor.getColumnIndex(column));

            if (dbResult.equals(search)) {
                return true;
            }
        }
        return false;
    }

    // Looks through the database, checks integer instead of String
//    boolean checkIdExistance (int id, String tableName, String column) {
//        databaseCursor = getCursor(tableName);
//        databaseCursor.moveToFirst();
//        int count = databaseCursor.getCount();
//
//        for (int x = 0; x < count; x++) {
//            databaseCursor.moveToPosition(x);
//            int dbResult = databaseCursor.getInt(databaseCursor.getColumnIndex(column));
//
//            if (dbResult == id) {
//                return true;
//            }
//        }
//        return false;
//    }

    //A simple method to check an admin property from the database.
    boolean isAdmin (String username){
        checkStringExistance(username, tableUserIds.TABLE_NAME, tableUserIds.COLUMN_USERNAME);
        databaseCursor.moveToPosition(userIndex);
        int data = databaseCursor.getInt(databaseCursor.getColumnIndex(tableUserIds.COLUMN_ADMIN));
        return (data == 1);
    }

    // Method is called, when a university has been changed.
    // Updates the UniversityManager object to reflect the data in the database.
    void updateUniversities() {
        UniversityManager uniMan = UniversityManager.getInstance();
        Cursor newCursor = getCursor(tableUniversity.TABLE_NAME);

        int numberOfItems = newCursor.getCount();

        String uniName;
        int uniId;
        ArrayList<University> universities = new ArrayList<>();

        for (int x = 0; x < numberOfItems; x++) {
            newCursor.moveToPosition(x);
            uniId = newCursor.getInt(newCursor.getColumnIndex(tableUniversity.COLUMN_UNIID));
            uniName = newCursor.getString(newCursor.getColumnIndex(tableUniversity.COLUMN_UNINAME));
            universities.add(new University(uniId, uniName));
        }
        newCursor.close();
        uniMan.setUniversities(universities);
        newCursor.close();
    }

    // Update Methods:
    // Update methods use the same principle, and so the principle has only been explained in the first
    // method. Update methods are made to parse database data, and to save the data in the right
    // format to be used by the program.
    private void updateRestaurants (University thisUniversity) {

        //System.out.println("Getting data from university: "+ thisUniversity.getUniName());

        //Creates an SQL query clause. Uses class variables as table names and attributes
        String restaurantQuery = "SELECT * FROM "+tableRestaurant.TABLE_NAME+
                " INNER JOIN "+ tableAddresses.TABLE_NAME+
                " ON "+tableRestaurant.TABLE_NAME+"."+tableRestaurant.COLUMN_ADDRESSID+
                " = "+tableAddresses.TABLE_NAME+"."+tableAddresses.COLUMN_ADDRESSID+
                " WHERE "+tableRestaurant.COLUMN_UNIID+" = ?;";

        //Creates the argument string array to be appended in where -clause.
        String[] argument = {String.valueOf(thisUniversity.getUniId())};

        //Executes the sql query.
        Cursor newCursor = getRawCursor(restaurantQuery, argument);
        ArrayList<Restaurant> restaurants = new ArrayList<>();

        int count = newCursor.getCount();

        // For -loop goes through every row of data, and appends it to corresponding class attributes.
        // Saves objects to an ArrayList that is later set to corresponding parent objects.
        for (int x = 0; x < count; x++) {
            newCursor.moveToPosition(x);

            String[] newAddressArray = new String[]{newCursor.getString(newCursor.getColumnIndex(tableAddresses.COLUMN_ADDRESS)),
                    Integer.toString(newCursor.getInt(newCursor.getColumnIndex(tableAddresses.COLUMN_POSTALCODE))),
                    newCursor.getString(newCursor.getColumnIndex(tableAddresses.COLUMN_CITY))};

            int newId = newCursor.getInt(newCursor.getColumnIndex(tableRestaurant.COLUMN_RESTAURANTID));
            String newName = newCursor.getString(newCursor.getColumnIndex(tableRestaurant.COLUMN_RESTAURANTNAME));
            int newAddressId = newCursor.getInt(newCursor.getColumnIndex(tableRestaurant.COLUMN_ADDRESSID));
            int newIsEnabled = newCursor.getInt(newCursor.getColumnIndex(tableRestaurant.COLUMN_ISENABLED));
            Restaurant newRestaurant = new Restaurant(newId, newName, newAddressArray, newAddressId, newIsEnabled);
            restaurants.add(newRestaurant);
        }
        newCursor.close();
        thisUniversity.setRestaurants(restaurants);
        newCursor.close();
    }

    private void updateFoods (Restaurant restaurant) {

        String whereClause = tableFood.COLUMN_RESTAURANTID + " = ?";
        String[] arguments = {Integer.toString(restaurant.getRestaurantId())};
        String orderBy = "SUBSTR("+tableFood.COLUMN_DATE+", 4, 2), SUBSTR("+tableFood.COLUMN_DATE+", 1, 2)";

        Cursor newCursor = getCursorWithWhereAndOrder(tableFood.TABLE_NAME, whereClause, arguments, orderBy);
        ArrayList<Food> foods = new ArrayList<>();
        Food foodTemp;

        for (int x = 0; x < newCursor.getCount(); x++) {
            newCursor.moveToPosition(x);
            String newFoodName = newCursor.getString(newCursor.getColumnIndex(tableFood.COLUMN_FOODNAME));
            float newFoodPrice = newCursor.getFloat(newCursor.getColumnIndex(tableFood.COLUMN_FOODPRICE));
            int newFoodId = newCursor.getInt(newCursor.getColumnIndex(tableFood.COLUMN_FOODID));
            String newDate = newCursor.getString(newCursor.getColumnIndex(tableFood.COLUMN_DATE));

            foodTemp = new Food(newFoodName,newFoodId,newFoodPrice,newDate);
            foods.add(foodTemp);
        }
        newCursor.close();
        restaurant.setRestaurantFoods(foods);
        newCursor.close();

    }

    void updateReviews (Food food) {

        ArrayList<Review> reviews = new ArrayList<>();
        Review reviewTemp;

        //Creates the argument string array to be appended in where -clause.
        String whereClause = tableReview.COLUMN_FOODID + " = ?";
        String[] arguments = {Integer.toString(food.getFoodId())};
        Cursor newCursor = getCursorWithWhere(tableReview.TABLE_NAME, whereClause, arguments);

        //For -loop to go through every column in the current sql query.
        for (int x = 0; x < newCursor.getCount(); x++) {
            newCursor.moveToPosition(x);
            int reviewId = newCursor.getInt(newCursor.getColumnIndex(tableReview.COLUMN_REVIEWID));
            int foodId = newCursor.getInt(newCursor.getColumnIndex(tableReview.COLUMN_FOODID));
            String review = newCursor.getString(newCursor.getColumnIndex(tableReview.COLUMN_REVIEW));
            float stars = newCursor.getFloat(newCursor.getColumnIndex(tableReview.COLUMN_STARS));
            String username = newCursor.getString(newCursor.getColumnIndex(tableReview.COLUMN_USERNAME));

            reviewTemp = new Review(reviewId ,foodId, stars, review, username);
            reviews.add(reviewTemp);
        }
        newCursor.close();
        food.setReviews(reviews);
        newCursor.close();
    }



    // Method to update every item related to selected university.
    // Called every time when some data related to the university has been changed (or university deleted).
    void updateCascade (University university) {
        ArrayList<Restaurant> restaurants;
        ArrayList<Food> foods;

        updateRestaurants(university);
        restaurants = university.getRestaurants();
        Restaurant tempRestaurant;

        // Update all related foods
        for (int x = 0; x < restaurants.size(); x++) {
            tempRestaurant = restaurants.get(x);
            updateFoods(tempRestaurant);
            foods = tempRestaurant.getFoods();

            //Update all related Reviews
            System.out.println(foods.size());
            for (int y = 0; y < foods.size(); y++) {
                updateReviews(foods.get(y));
            }
        }

    }

    // Method sets a new university directly to database.
//    void setNewUniversity (String newUniName) {
//
//        ContentValues cv = new ContentValues();
//
//        cv.put(tableUniversity.COLUMN_UNINAME, newUniName);
//        long insertedId = db.insert(tableUniversity.TABLE_NAME, null, cv);
//        updateUniversities();
//
//        System.out.println("New university id is: "+insertedId);
//    }

    // Method sets a new restaurant directly to database.
    void setNewRestaurant (String[] newAddress, String newRestaurantName, int whichUni) {

        ContentValues cvAddress = new ContentValues();
        cvAddress.put(tableAddresses.COLUMN_ADDRESS,newAddress[0]);
        cvAddress.put(tableAddresses.COLUMN_POSTALCODE, Integer.parseInt(newAddress[1]));
        cvAddress.put(tableAddresses.COLUMN_CITY, newAddress[2]);
        long newAddressId = db.insert(tableAddresses.TABLE_NAME, null, cvAddress);
        System.out.println("New address id is: "+newAddressId);

        int isEnabledInt = 1;

        ContentValues cvRestaurant = new ContentValues();
        cvRestaurant.put(tableRestaurant.COLUMN_ADDRESSID, newAddressId);
        cvRestaurant.put(tableRestaurant.COLUMN_RESTAURANTNAME, newRestaurantName);
        cvRestaurant.put(tableRestaurant.COLUMN_UNIID,whichUni);
        cvRestaurant.put(tableRestaurant.COLUMN_ISENABLED, isEnabledInt);
        long newAddedRestaurantId = db.insert(tableRestaurant.TABLE_NAME,null, cvRestaurant);
        System.out.println("New restaurant id is: "+newAddedRestaurantId);
    }

    // Method sets a new food directly to database.
    void setNewFood(String newFoodName, float newFoodPrice, int newRestaurantId, String newFoodDate) {
        ContentValues cv = new ContentValues();
        cv.put(tableFood.COLUMN_RESTAURANTID, newRestaurantId);
        cv.put(tableFood.COLUMN_FOODNAME, newFoodName);
        cv.put(tableFood.COLUMN_FOODPRICE, newFoodPrice);
        cv.put(tableFood.COLUMN_DATE, newFoodDate);
        long insertedId = db.insert(tableFood.TABLE_NAME,null, cv);
        System.out.println("New food id is: "+insertedId);
    }

    //Method sets a new review directly to the database.
    void setNewReview (String newReview, float newStars, String newUsername, int newFoodId) {
        ContentValues cv = new ContentValues();
        cv.put(tableReview.COLUMN_FOODID, newFoodId);
        cv.put(tableReview.COLUMN_STARS, newStars);
        cv.put(tableReview.COLUMN_REVIEW, newReview);
        cv.put(tableReview.COLUMN_USERNAME, newUsername);
        long insertedId = db.insert(tableReview.TABLE_NAME, null, cv);
        System.out.println("New review id is: "+ insertedId);
    }

    //Methods to modify database data:

    // Modify restaurant data. Call when needed to change restaurant data. Use null on String values
    // if value is not to be changed. Use -1 on integers if value is not to be changed.
    boolean modifyRestaurantData (Restaurant restaurant, String[] newAddress, String newRestaurantName, int whichUni, boolean isEnabled) {
        ContentValues cvAddress = new ContentValues();
        ContentValues cvRestaurant = new ContentValues();

        int isEnabledInt;
        if (isEnabled) {
            isEnabledInt = 1;
        }
        else {
            isEnabledInt = 0;
        }

        if (newAddress != null) {
            String whereClauseAddress = tableAddresses.COLUMN_ADDRESSID + " = ?";
            String[] whereArgsAddressrestaurant = {Integer.toString(restaurant.getRestaurantAddressId())};
            cvAddress.put(tableAddresses.COLUMN_ADDRESS, newAddress[0]);
            cvAddress.put(tableAddresses.COLUMN_POSTALCODE, Integer.parseInt(newAddress[1]));
            cvAddress.put(tableAddresses.COLUMN_CITY, newAddress[2]);

            if (db.update(tableAddresses.TABLE_NAME, cvAddress, whereClauseAddress, whereArgsAddressrestaurant) <= 0) {
                return false;
            }

        }
        if (newRestaurantName != null) {
            cvRestaurant.put(tableRestaurant.COLUMN_RESTAURANTNAME, newRestaurantName);
        }
        if (whichUni != -1) {
            cvRestaurant.put(tableRestaurant.COLUMN_UNIID, whichUni);
        }
        cvRestaurant.put(tableRestaurant.COLUMN_ISENABLED, isEnabledInt);
            String whereClauseRestaurant = tableRestaurant.COLUMN_RESTAURANTID + " = ?";
            String[] whereArgsRestaurant = {Integer.toString(restaurant.getRestaurantId())};

        return db.update(tableRestaurant.TABLE_NAME, cvRestaurant, whereClauseRestaurant, whereArgsRestaurant) > 0;
    }

    // Modify food's data in the database. Again, use unmodified strings as null and other values as -1
    boolean modifyFoodData(Food food, String foodName, float foodPrice, String foodDate) {
        ContentValues cv = new ContentValues();
        String whereClause = tableFood.COLUMN_FOODID+ " = ?";
        String[] whereArgs = {Integer.toString(food.getFoodId())};

        if (foodName != null) {
            cv.put(tableFood.COLUMN_FOODNAME, foodName);
        }
        if (foodPrice != -1f) {
            cv.put(tableFood.COLUMN_FOODPRICE, foodPrice);
        }
        if (foodDate != null) {
            cv.put(tableFood.COLUMN_DATE, foodDate);
        }

        return db.update(tableFood.TABLE_NAME, cv, whereClause, whereArgs) > 0;
    }

    boolean modifyReviewData (Review reviewObject, String writtenReview, float newStars) {

        ContentValues cv = new ContentValues();
        String whereClause = tableReview.COLUMN_REVIEWID + " = ?";
        String[] whereArgs = {Integer.toString(reviewObject.getReviewId())};

        cv.put(tableReview.COLUMN_REVIEW, writtenReview);
        cv.put(tableReview.COLUMN_STARS, newStars);


        return db.update(tableReview.TABLE_NAME, cv, whereClause, whereArgs) > 0;
    }

    ArrayList<Review> getReviewsForUser (String username) {

        String reviewQuery = "SELECT * FROM "+tableReview.TABLE_NAME+
                " INNER JOIN "+ tableFood.TABLE_NAME+
                " ON "+tableReview.TABLE_NAME+"."+tableReview.COLUMN_FOODID+
                " = "+tableFood.TABLE_NAME+"."+tableFood.COLUMN_FOODID+
                " WHERE "+tableReview.COLUMN_USERNAME+" = ?;";

        ArrayList<Review> newReviews = new ArrayList<>();

        String[] whereArgs = {username};
        Cursor cursor = getRawCursor(reviewQuery, whereArgs);

        int count = cursor.getCount();

        for (int x = 0; x < count; x++) {
            cursor.moveToPosition(x);
            int reviewId = cursor.getInt(cursor.getColumnIndex(tableReview.COLUMN_REVIEWID));
            int foodId = cursor.getInt(cursor.getColumnIndex(tableFood.COLUMN_FOODID));
            String review = cursor.getString(cursor.getColumnIndex(tableReview.COLUMN_REVIEW));
            float grade = cursor.getFloat(cursor.getColumnIndex(tableReview.COLUMN_STARS));
            String userId = cursor.getString(cursor.getColumnIndex(tableReview.COLUMN_USERNAME));
            String foodName = cursor.getString(cursor.getColumnIndex(tableFood.COLUMN_FOODNAME));

            Review newReview = new Review(reviewId, foodId, grade, review, userId);
            newReview.setFoodName(foodName);

            newReviews.add(newReview);
        }
        return newReviews;
    }

    ArrayList<Review> getAllReviews () {
        String reviewQuery = "SELECT * FROM "+tableReview.TABLE_NAME+
                " INNER JOIN "+ tableFood.TABLE_NAME+
                " ON "+tableReview.TABLE_NAME+"."+tableReview.COLUMN_FOODID+
                " = "+tableFood.TABLE_NAME+"."+tableFood.COLUMN_FOODID+";";
        ArrayList<Review> newReviews = new ArrayList<>();

        Cursor cursor = getRawCursor(reviewQuery, null);

        int count = cursor.getCount();

        for (int x = 0; x < count; x++) {
            cursor.moveToPosition(x);
            int reviewId = cursor.getInt(cursor.getColumnIndex(tableReview.COLUMN_REVIEWID));
            int foodId = cursor.getInt(cursor.getColumnIndex(tableFood.COLUMN_FOODID));
            String review = cursor.getString(cursor.getColumnIndex(tableReview.COLUMN_REVIEW));
            float grade = cursor.getFloat(cursor.getColumnIndex(tableReview.COLUMN_STARS));
            String userId = cursor.getString(cursor.getColumnIndex(tableReview.COLUMN_USERNAME));
            String foodName = cursor.getString(cursor.getColumnIndex(tableFood.COLUMN_FOODNAME));

            Review newReview = new Review(reviewId, foodId, grade, review, userId);
            newReview.setFoodName(foodName);

            newReviews.add(newReview);
        }
        return newReviews;
    }


    // Methods to remove items from database:
    // All methods take in the corresponding object. Method takes it's id from the object and
    // removes the corresponding row from the database. Runs update -methods afterwards to
    // keep program's object

//    void deleteUniversity(University university) {
//        String whereClause = tableUniversity.COLUMN_UNIID +" = ?";
//        String[] whereArgs = {Integer.toString(university.getUniId())};
//        db.delete(tableUniversity.TABLE_NAME, whereClause, whereArgs);
//
//        updateUniversities();
//    }

    void deleteRestaurant(Restaurant restaurant, University university) {
        String pragma = "PRAGMA foreign_keys = ON;";
        db.execSQL(pragma);
        String whereClause = tableRestaurant.COLUMN_RESTAURANTID +" = ?";
        String[] whereArgsRestaurant = {Integer.toString(restaurant.getRestaurantId())};
        db.delete(tableRestaurant.TABLE_NAME, whereClause, whereArgsRestaurant);

        whereClause = tableAddresses.COLUMN_ADDRESSID +" = ?";
        String[] whereArgsAddress = {Integer.toString(restaurant.getRestaurantAddressId())};
        db.delete(tableAddresses.TABLE_NAME, whereClause, whereArgsAddress);

        pragma = "PRAGMA foreign_keys = OFF;";
        db.execSQL(pragma);
        updateCascade(university);
    }

    void deleteFood(Food food, Restaurant restaurant) {
        String whereClause = tableFood.COLUMN_FOODID +" = ?";
        String[] whereArgs = {Integer.toString(food.getFoodId())};
        db.delete(tableFood.TABLE_NAME, whereClause, whereArgs);

        updateFoods(restaurant);
        ArrayList<Food> tempFoods = restaurant.getFoods();
        for (int x = 0; x < tempFoods.size(); x++) {
            updateReviews(tempFoods.get(x));
        }
    }

    void deleteReview(Review review) {
        String whereClause = tableReview.COLUMN_REVIEWID +" = ?";
        String[] whereArgs = {Integer.toString(review.getReviewId())};
        db.delete(tableReview.TABLE_NAME, whereClause, whereArgs);

    }

    void deleteUser(String username) {
        String whereClause = tableUserIds.COLUMN_USERNAME +" = ?";
        String[] whereArgs = {username};
        db.delete(tableReview.TABLE_NAME, whereClause, whereArgs);

        updateUsers();
    }

    // This method makes a query to get the data from the database. Returns cursor.
    private Cursor getCursor(String whatTable) {
        return db.query(whatTable,
                null,
                null,
                null,
                null,
                null,
                null);
    }

    // This method uses a where -clause to get data from the database.
    private Cursor getCursorWithWhere(String whatTable, String whereClause, String[] whereArgs) {
        return db.query(whatTable,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null);
    }

    private Cursor getCursorWithWhereAndOrder(String whatTable, String whereClause, String[] whereArgs, String orderBy) {
        return db.query(whatTable,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                orderBy);
    }

    // This method fetches a cursor using a raw SQL select -clause.
    // Used instead of plain fetch (above) when joining tables or requiring very specific data.
    private Cursor getRawCursor (String query, String[] arguments) {
        return db.rawQuery(query, arguments);

    }

    // Hardcode admin in to the database.
    private void createAdmin() {
        encryptor = PasswordEncryptor.getInstance();
        byte[] salt = encryptor.getSalt("admin");
        ContentValues cv = new ContentValues();
        String password = encryptor.encryptor("admin", salt);
        cv.put(tableUserIds.COLUMN_USERNAME, "admin");
        cv.put(tableUserIds.COLUMN_NICKNAME, "SUPERADMIN");
        cv.put(tableUserIds.COLUMN_PASSWORD, password);
        cv.put(tableUserIds.COLUMN_SALT, salt);
        cv.put(tableUserIds.COLUMN_ADMIN, 1);
        cv.put(tableUserIds.COLUMN_HOMEUNIID, 1);
        db.insert(tableUserIds.TABLE_NAME, null, cv);
    }



    // This method puts the Test Data in to the database, while running checks to qualify the existance
    // of the data that is being added.
    void hardCodeDatabaseTestData() {
        // users
        if (!checkStringExistance("admin", tableUserIds.TABLE_NAME, tableUserIds.COLUMN_USERNAME)) {
            createAdmin();
        }
        if (!checkStringExistance("testikayttaja", tableUserIds.TABLE_NAME, tableUserIds.COLUMN_USERNAME)) {
            addItem("testikayttaja", "salasana", "Testi Kayttaja", 1);
        }
    }
}
