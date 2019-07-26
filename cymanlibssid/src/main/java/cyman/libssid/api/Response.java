
package cyman.libssid.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Response implements Serializable {

    @SerializedName("appName")
    @Expose
    private String appName;
    @SerializedName("walletName")
    @Expose
    private String walletName;
    @SerializedName("walletKey")
    @Expose
    private String walletKey;

    @SerializedName("isConnected")
    @Expose
    private String isConnected;

    @SerializedName("orgDid")
    @Expose
    private String orgDid;

    @SerializedName("userDid")
    @Expose
    private String userDid;

    @SerializedName("appKey")
    @Expose
    private String appKey;




    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getWalletName() {
        return walletName;
    }

    public void setWalletName(String walletName) {
        this.walletName = walletName;
    }

    public String getWalletKey() {
        return walletKey;
    }

    public void setWalletKey(String walletKey) {
        this.walletKey = walletKey;
    }

    public String getIsConnected() {
        return isConnected;
    }

    public void setIsConnected(String isConnected) {
        this.isConnected = isConnected;
    }

    public String getOrgDid() {
        return orgDid;
    }

    public void setOrgDid(String orgDid) {
        this.orgDid = orgDid;
    }

    public String getUserDid() {
        return userDid;
    }

    public void setUserDid(String userDid) {
        this.userDid = userDid;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }
}
