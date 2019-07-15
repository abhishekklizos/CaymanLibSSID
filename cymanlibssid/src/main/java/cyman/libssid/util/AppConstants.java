package cyman.libssid.util;

import cyman.libssid.BuildConfig;

public class AppConstants {

    public static final String COUNTRY_STATE_CITY_LIST_PART_URL = "Appsettings";
    public static final String MULTIPLE_USAGE_PART_URL = "Wallet";
    public static final String CONNECTION_SEND_PART_URL = "Connections";
    public static final String CREDENTIAL_DATAREQUEST_PART_URL = "Credential";
    public static final String CREDENTIAL_LICENCE_DATA_SCAN = "Ocr";


    public static final String CHANGE_PASSWORD_MODE = "change-password";
    public static final String COUNTRY_STATE_CITY_LIST_MODE = "locations";
    public static final String SIGNUP_MODE = "signup";
    public static final String LOGIN_MODE = "signin";
    public static final String PROFILE_MODE = "user-profile";
    public static final String CONNECTION_SEND_MODE = "send-connection-request";
    public static final String CONNECTION_PENDING_LISTING_MODE = "view-connection-request";
    public static final String CONNECTION_ACCEPTED_LISTING_MODE = "connection-list";
    public static final String CREDENTIAL_DATA_REQUEST_LISTING_MODE = "view-all-credential-offer";
    public static final String CREDENTIAL_DATA_SEND_MODE = "user-accept-credential";
    public static final String CONNECTION_REQUEST_ACCEPT_MODE = "accept-connection-request";
    public static final String CONNECTION_REQUEST_DECLINE_MODE = "reject-connection-request";
    public static final String CONNECTED_USER_DETAILS_MODE = "wallet-activity";
    public static final String FORGOT_PASSWORD_REQUEST_MODE = "forgot-password-request";
    public static final String FORGOT_PASSWORD_CODE_CHECK_MODE = "forgot-password-check-code";
    public static final String FORGOT_PASSWORD_FINAL_HIT_MODE = "forgot-password-change-password";
    public static final String CREDENTIAL_DATA_LIST_REQUEST_MODE = "view-all-info-credentials";
    public static final String RESEND_EMAIL_CODE_MODE = "resend-email-varification-code";
    public static final String AUTHENTICATION_MODE = "check-google-authenticator";
    public static final String ENABLE_AUTHENTICATION_MODE = "enable-google-authenticator";
    public static final String EMAIL_VERIFICATION_MODE = "email-varification";
    public static final String ADDRESS_SEND_MODE = "address-verification-user";
    public static final String PASSPORT_SEND_MODE = "passport-verification-user";
    public static final String SCHEMA_REQUEST_MODE = "get-verified-information";
    public static final String REQUEST_FOR_LICENCE_SCAN_MODE = "get-driving-image-text";
    public static final String SEND_LICENCE_DATA_MODE = "";
    public static final String REQUEST_SUBMITTED_BUT_NOT_VERIFIED_DATA_MODE = "view-cryto-credential";


    public static final long PHONE_VERIFICATION_TIMEOUT = 60;

    public static final int MINIMUM_AGE_FOR_SIGNUP = 18;

    public static final String PREF_NAME = "forummanagement_pref";

    public static final String TIMESTAMP_FORMAT = "yyyyMMdd_HHmmss";
    public static final int REQUEST_CODE_STORAGE_PERMS = 321;

//        public static final String HOST = "149.28.93.35";

//    public static final String HOST = ssidApplication.getString(R.string.base_host);

//    private static Resources res = ssidApplication.getResources();
//    private static String only_key_stored = SharedPrefManager.getInstance(ssidApplication).getURLKey();
//    private static String variable_key_for_url = String.format(res.
//            getString(R.string.base_host_dynamic), only_key_stored);
//    private static final String HOST = variable_key_for_url;
//    public static final String BASE_URL = "https://" + HOST + "/api/";

    public static final String BASE_URL = BuildConfig.SSID_BASE_URL; // Declared in app.gradle

    public static final String OTP_INPUT = "otp_input";
    public static final String LOG_OUT_CONFIRM = "log_out";
    public static final String CHECK_FOR_USE_LISTING_ACTIVITY_FOR_PENDING_LISTING = "PENDING_LISTING";
    public static final String CHECK_FOR_USE_LISTING_ACTIVITY_FOR_ACCEPTED_LISTING = "ACCEPTED_LISTING";
    public static final String CHECK_FOR_USE_LISTING_ACTIVITY_FOR_CERTIFIED_DATA_REQUEST_LISTING = "CERTIFIED_DATA_REQUEST_LISTING";

    public static final String CHECK_FOR_FINGERPRINT_PRESENT = "Fingerprint";
    public static final String CHECK_FOR_PATTERN_OR_PIN_PRESENT = "Pattern_or_pin";

    public static final String address_for_certificate_data_request = "address";
    public static final String name_for_certificate_data_request = "name";
    public static final String first_name_for_certificate_data_request = "firstName";
    public static final String middle_name_for_certificate_data_request = "middleName";
    public static final String last_name_for_certificate_data_request = "lastName";
    public static final String dob_for_certificate_data_request = "dateofBirth";
    public static final String email_for_certificate_data_request = "email";
    public static final String phone_for_certificate_data_request = "phoneNo";
    public static final String address_one_for_certificate_data_request = "addressOne";
    public static final String address_two_for_certificate_data_request = "addressTwo";
    public static final String city_for_certificate_data_request = "city";
    public static final String state_for_certificate_data_request = "state";
    public static final String country_for_certificate_data_request = "country";
    public static final String zipcode_for_certificate_data_request = "zipcode";
    public static final String docUploaded_for_certificate_data_request = "docUploaded";
    public static final String isVerified_for_certificate_data_request = "isVerified";
    public static final String walletName_for_certificate_data_request = "walletName";
    public static final String endpointDid_for_certificate_data_request = "endpointDid";
    public static final String varifiedOn_for_certificate_data_request = "varifiedOn";
    public static final String PHONE_VERIFICATION_MODE = "check-phoneNo";

    public static final String INTENT_TO_GO_TO_SOCIAL_LOGIN = "socialLoginIntent";
    public static final String INTENT_TO_GO_TO_LOGIN = "loginIntent";
    public static final String INTENT_TO_GO_TO_MAIN = "mainIntent";
    public static final String DATABASE_NAME = "SSIDDB";
    public static final int DATABASE_VERSION = 3;


    public static final String COUNTRY_ISO_INDIA = "IND";
    public static final String COUNTRY_ISO_CAYMAN = "CYM";


    public static final String OPEN_FOR_SELF_VERIFICATION = "self_verification";
    public static final String OPEN_FOR_NOTARY_VERIFICATION = "notary_verification";
    public static final long TIME_AUTO_LOGOUT = 180000;  // 3 minutes

    public static final String KEY_NAME_LOCK_SCREEN = "SSIDSecurity";

    public static String THIRED_PARTY_PACKAGE = "";


    public AppConstants() {
    }
}
