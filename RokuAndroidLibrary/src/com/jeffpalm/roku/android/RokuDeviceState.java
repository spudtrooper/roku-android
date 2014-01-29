package com.jeffpalm.roku.android;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

public final class RokuDeviceState implements Parcelable {

  private final RokuDeviceInfo deviceInfo;
  private final String host;
  private final List<RokuAppInfo> appInfos = new ArrayList<RokuAppInfo>();

  public RokuDeviceState(RokuDeviceInfo deviceInfo, String host) {
    this.deviceInfo = deviceInfo;
    this.host = host;
  }

  public RokuDeviceInfo getDeviceInfo() {
    return deviceInfo;
  }

  public String getHost() {
    return host;
  }

  public List<RokuAppInfo> getAppInfos() {
    return appInfos;
  }

  /** Adds {@code appInfo} and returns whether it was added. */
  public boolean addAppInfo(RokuAppInfo appInfo) {
    return appInfos.contains(appInfo) ? false : appInfos.add(appInfo);
  }

  public void addAllAppInfos(List<RokuAppInfo> appInfos) {
    this.appInfos.addAll(appInfos);
  }

  public int describeContents() {
    return 0;
  }

  public void writeToParcel(Parcel out, int flags) {
    out.writeString(host);
    out.writeList(appInfos);
    out.writeParcelable(deviceInfo, flags);
  }

  public static final Parcelable.Creator<RokuDeviceState> CREATOR = new Parcelable.Creator<RokuDeviceState>() {
    public RokuDeviceState createFromParcel(Parcel in) {
      return new RokuDeviceState(in);
    }

    public RokuDeviceState[] newArray(int size) {
      return new RokuDeviceState[size];
    }
  };

  private RokuDeviceState(Parcel in) {
    host = in.readString();
    in.readList(appInfos, RokuAppInfo.class.getClassLoader());
    deviceInfo = in.readParcelable(RokuDeviceInfo.class.getClassLoader());
  }

}
