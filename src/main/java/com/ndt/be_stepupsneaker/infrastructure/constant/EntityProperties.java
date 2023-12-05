package com.ndt.be_stepupsneaker.infrastructure.constant;

public final class EntityProperties {
    private EntityProperties() {
    }

    public static final byte LENGTH_NAME_SHORT = 50;
    public static final byte LENGTH_ID = 36;
    public static final short LENGTH_URL = 255;
    public static final short LENGTH_NAME = 255;
    public static final short LENGTH_DESCRIPTION = 1000;
    public static final short LENGTH_HREF = 400;
    public static final short LENGTH_ADDRESS = 500;
    public static final byte LENGTH_CODE = 10;
    public static final byte LENGTH_GENDER = 50;
    public static final int LENGTH_PASSWORD = 150;
    public static final byte LENGTH_ACCOUNT = 20;
    public static final byte LENGTH_EMAIL = 50;
    public static final byte LENGTH_PHONE = 20;
    public static final byte LENGTH_CITY = 50;
    public static final byte LENGTH_PROVINCE = 50;
    public static final byte LENGTH_COUNTRY = 50;
    public static final byte LENGTH_PENDING_ORDER = 5;
    public static final String GHN_API_FEE_URL = "https://online-gateway.ghn.vn/shiip/public-api/v2/shipping-order/fee";
    public static final String VITE_GHN_USER_TOKEN = "2bddbda8-7165-11ee-af43-6ead57e9219a";
    public static final String VITE_GHN_SHOP_ID = "4649903";
    public static final String SECRET = "QHMBQfsViR66wU3Yx/MOdkKcHdmJeRy4JdbDbrjmZdfu35Q7yzH6b3vJCrQcNgoOEFfsGyhOeF5Pby7R+YzG0w==";
    public static final String NOT_FOUND = " Not Found!";
    public static final String IS_EXIST = " Is Exist!";
    public static final String NOT_EXIST = " Not Is Exist!";
    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    public static final String ROLE_STAFF = "ROLE_STAFF";
    public static final String ROLE_CUSTOMER = "ROLE_CUSTOMER";
    public static final String ADMIN = "ADMIN";
    public static final String STAFF = "STAFF";
    public static final String CUSTOMER = "CUSTOMER";
    public static final String URL_RESET = "http://localhost:8080/auth/reset-password?token=";
    public static final int EXPIRATION = 5;

}
