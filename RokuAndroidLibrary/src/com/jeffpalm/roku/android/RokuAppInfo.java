package com.jeffpalm.roku.android;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;

import android.os.Parcel;
import android.os.Parcelable;

public final class RokuAppInfo implements Parcelable {

  private int id;
  private String version;
  private String name;

  public int getId() {
    return id;
  }
  public String getVersion() {
    return version;
  }
  public String getName() {
    return name;
  }
  RokuAppInfo setId(int id) {
    this.id = id;
    return this;
  }
  RokuAppInfo setVersion(String version) {
    assert (this.version == null);
    this.version = version;
    return this;
  }
  RokuAppInfo setName(String name) {
    assert (this.name == null);
    this.name = name;
    return this;
  }

  public boolean equals(Object o) {
    if (!(o instanceof RokuAppInfo)) {
      return false;
    }
    RokuAppInfo that = (RokuAppInfo) o;
    return this.getId() == that.getId() && this.getVersion().equals(that.getVersion())
        && this.getName().equals(that.getName());
  }

  public static List<RokuAppInfo> fromInputStream(InputStream xml) throws XmlPullParserException,
      IOException {
    List<RokuAppInfo> infos = new ArrayList<RokuAppInfo>();
    new RokuAppInfoParser(infos).parse(xml);
    return infos;
  }

  public int describeContents() {
    return 0;
  }

  public void writeToParcel(Parcel out, int flags) {
    out.writeInt(id);
    out.writeString(version);
    out.writeString(name);
  }

  public static final Parcelable.Creator<RokuAppInfo> CREATOR = new Parcelable.Creator<RokuAppInfo>() {
    public RokuAppInfo createFromParcel(Parcel in) {
      return new RokuAppInfo(in);
    }

    public RokuAppInfo[] newArray(int size) {
      return new RokuAppInfo[size];
    }
  };

  private RokuAppInfo(Parcel in) {
    id = in.readInt();
    version = in.readString();
    name = in.readString();
  }

  public RokuAppInfo() {}

}
