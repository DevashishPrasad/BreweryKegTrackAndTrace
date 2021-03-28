package com.example.brewerykegtrackandtrace;

public class Constants {

    public final static String ROOT_URL = "http://103.93.16.220/BreweryKegTrackAndTrace/";

    // USER API
    public final static String USER_LOGIN_URL = ROOT_URL + "user/login.php";
    public final static String USER_EDIT_URL = ROOT_URL + "user/edit.php";
    public final static String USER_DELETE_URL = ROOT_URL + "user/delete.php";
    public final static String USER_REGISTER_URL = ROOT_URL + "user/register.php";
    public final static String USER_LIST_URL = ROOT_URL + "user/users.php";

    // LOCATION API
    public final static String LOCATIONS_LIST_URL = ROOT_URL + "location/locations.php";
    public final static String LOCATIONS_DELETE_URL = ROOT_URL + "location/delete.php";
    public final static String LOCATIONS_EDIT_URL = ROOT_URL + "location/edit.php";
    public final static String LOCATIONS_REGISTER_URL = ROOT_URL + "location/register.php";
    public final static String LOCATION_URL = ROOT_URL + "location/location.php";

    // ASSETS API
    public final static String ASSETS_LIST_URL = ROOT_URL + "asset/assets.php";
    public final static String ASSETS_DELETE_URL = ROOT_URL + "asset/delete.php";
    public final static String ASSETS_EDIT_URL = ROOT_URL + "asset/edit.php";
    public final static String ASSETS_REGISTER_URL = ROOT_URL + "asset/register.php";
    public final static String ASSET_URL = ROOT_URL + "asset/asset.php";

    // TRANSPORTS API
    public final static String TRANSPORTS_LIST_URL = ROOT_URL + "transport/trucks.php";
    public final static String TRANSPORTS_DELETE_URL = ROOT_URL + "transport/delete.php";
    public final static String TRANSPORTS_EDIT_URL = ROOT_URL + "transport/edit.php";
    public final static String TRANSPORTS_REGISTER_URL = ROOT_URL + "transport/register.php";

    // REPORT API
    public final static String REPORT_LIST_URL = ROOT_URL + "transaction/report.php";

    // TRANSACTION API
    public final static String TRANSACTION_URL = ROOT_URL + "transaction/register.php";
}
