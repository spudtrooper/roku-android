package com.jeffpalm.roku.android;

import java.io.IOException;
import java.io.InputStream;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Xml;

public class RokuDeviceInfo implements Parcelable {

  private String deviceType;
  private String friendlyName;
  private String manufacturer;
  private String manufacturerURL;
  private String modelDescription;
  private String modelName;
  private String modelNumber;
  private String modelURL;
  private String serialNumber;
  private String udn;

  public String getManufacturerURL() {
    return manufacturerURL;
  }

  void setManufacturerURL(String manufacturerURL) {
    this.manufacturerURL = manufacturerURL;
  }

  public String getModelDescription() {
    return modelDescription;
  }

  void setModelDescription(String modelDescription) {
    this.modelDescription = modelDescription;
  }

  public String getModelName() {
    return modelName;
  }

  void setModelName(String modelName) {
    this.modelName = modelName;
  }

  public String getModelNumber() {
    return modelNumber;
  }

  void setModelNumber(String modelNumber) {
    this.modelNumber = modelNumber;
  }

  public String getModelURL() {
    return modelURL;
  }

  void setModelURL(String modelURL) {
    this.modelURL = modelURL;
  }

  public String getSerialNumber() {
    return serialNumber;
  }

  void setSerialNumber(String serialNumber) {
    this.serialNumber = serialNumber;
  }

  public String getUDN() {
    return udn;
  }

  void setUDN(String udn) {
    this.udn = udn;
  }

  public String getDeviceType() {
    return deviceType;
  }

  void setDeviceType(String deviceType) {
    this.deviceType = deviceType;
  }

  public String getFriendlyName() {
    return friendlyName;
  }

  void setFriendlyName(String friendlyName) {
    this.friendlyName = friendlyName;
  }

  public String getManufacturer() {
    return manufacturer;
  }

  void setManufacturer(String manufacturer) {
    this.manufacturer = manufacturer;
  }

  public static RokuDeviceInfo fromInputStream(InputStream xml) throws XmlPullParserException,
      IOException {
    RokuDeviceInfo info = new RokuDeviceInfo();
    new RokuDeviceInfoParser(info).parse(xml);
    return info;
  }

  public int describeContents() {
    return 0;
  }

  public void writeToParcel(Parcel out, int flags) {
    out.writeString(deviceType);
    out.writeString(friendlyName);
    out.writeString(manufacturer);
    out.writeString(manufacturerURL);
    out.writeString(modelDescription);
    out.writeString(modelName);
    out.writeString(modelNumber);
    out.writeString(modelURL);
    out.writeString(serialNumber);
    out.writeString(udn);
  }

  public static final Parcelable.Creator<RokuDeviceInfo> CREATOR = new Parcelable.Creator<RokuDeviceInfo>() {
    public RokuDeviceInfo createFromParcel(Parcel in) {
      return new RokuDeviceInfo(in);
    }

    public RokuDeviceInfo[] newArray(int size) {
      return new RokuDeviceInfo[size];
    }
  };

  private RokuDeviceInfo() {

  }

  private RokuDeviceInfo(Parcel in) {
    deviceType = in.readString();
    friendlyName = in.readString();
    manufacturer = in.readString();
    manufacturerURL = in.readString();
    modelDescription = in.readString();
    modelName = in.readString();
    modelNumber = in.readString();
    modelURL = in.readString();
    serialNumber = in.readString();
    udn = in.readString();
  }
}
