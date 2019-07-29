package com.example.foodreview;

import android.provider.BaseColumns;

class UserIdContract{

    private UserIdContract() {}

    static final class tableUserIds implements BaseColumns {
        //Contains table names.
        static final String TABLE_NAME = "userIds";
        static final String COLUMN_NICKNAME = "nickname";
        static final String COLUMN_USERNAME = "username";
        static final String COLUMN_PASSWORD = "password";
        static final String COLUMN_SALT = "salt";
        static final String COLUMN_ADMIN = "admin";
        static final String COLUMN_HOMEUNIID = "homeUniId";
    }

    static final class tableAddresses implements BaseColumns {
        //Contains table names.
        static final String TABLE_NAME = "address";
        static final String COLUMN_ADDRESSID = "addressId";
        static final String COLUMN_ADDRESS = "address";
        static final String COLUMN_POSTALCODE = "postalCode";
        static final String COLUMN_CITY = "city";
    }

    static final class tableUniversity implements BaseColumns {
        //Contains table names.
        static final String TABLE_NAME = "university";
        static final String COLUMN_UNIID = "uniId";
        static final String COLUMN_UNINAME = "uniName";
    }

    static final class tableChef implements BaseColumns {
        //Contains table names.
        static final String TABLE_NAME = "chef";
        static final String COLUMN_CHEFID = "chefId";
        static final String COLUMN_FIRSTNAME = "firstName";
        static final String COLUMN_LASTNAME = "lastName";
    }

    static final class tableRestaurant implements BaseColumns {
        //Contains table names.
        static final String TABLE_NAME = "restaurant";
        static final String COLUMN_RESTAURANTID = "restaurantId";
        static final String COLUMN_RESTAURANTNAME = "restaurantName";
        static final String COLUMN_ISENABLED = "isEnabled";
        static final String COLUMN_UNIID = "uniId";
        static final String COLUMN_ADDRESSID = "addressId";
    }

    static final class tableFood implements BaseColumns {
        //Contains table names.
        static final String TABLE_NAME = "food";
        static final String COLUMN_FOODID = "foodId";
        static final String COLUMN_FOODPRICE = "foodPrice";
        static final String COLUMN_FOODNAME = "foodName";
        static final String COLUMN_DATE = "date";
        static final String COLUMN_RESTAURANTID = "restaurantID";
        static final String COLUMN_CHEFID = "chefId";
    }

    static final class tableReview implements BaseColumns {
        //Contains table names.
        static final String TABLE_NAME = "review";
        static final String COLUMN_REVIEWID = "reviewId";
        static final String COLUMN_REVIEW = "review";
        static final String COLUMN_STARS = "stars";
        static final String COLUMN_FOODID = "foodId";
        static final String COLUMN_USERNAME = "username";
    }

}
