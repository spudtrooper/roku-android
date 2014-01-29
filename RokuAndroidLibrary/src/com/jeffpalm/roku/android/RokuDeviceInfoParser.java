package com.jeffpalm.roku.android;

import java.io.IOException;
import java.io.InputStream;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;

final class RokuDeviceInfoParser {

  private final RokuDeviceInfo info;

  public RokuDeviceInfoParser(RokuDeviceInfo info) {
    this.info = info;
  }

  public void parse(InputStream in) throws XmlPullParserException, IOException {
    try {
      XmlPullParser parser = Xml.newPullParser();
      parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
      parser.setInput(in, null);
      parser.nextTag();
      readFeed(parser);
    } finally {
      in.close();
    }
  }

  private void readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
    parser.require(XmlPullParser.START_TAG, null, "root");
    while (parser.next() != XmlPullParser.END_TAG) {
      if (parser.getEventType() != XmlPullParser.START_TAG) {
        continue;
      }
      String name = parser.getName();
      if (name.equals("device")) {
        readDevice(parser);
      } else {
        skip(parser);
      }
    }
  }
  private void readDevice(XmlPullParser parser) throws XmlPullParserException, IOException {
    parser.require(XmlPullParser.START_TAG, null, "device");
    while (parser.next() != XmlPullParser.END_TAG) {
      if (parser.getEventType() != XmlPullParser.START_TAG) {
        continue;
      }
      String name = parser.getName();
      if (name.equals("deviceType")) {
        info.setDeviceType(readTextNode(parser, "deviceType"));
        continue;
      }
      if (name.equals("friendlyName")) {
        info.setFriendlyName(readTextNode(parser, "friendlyName"));
        continue;
      }
      if (name.equals("manufacturer")) {
        info.setManufacturer(readTextNode(parser, "manufacturer"));
        continue;
      }
      if (name.equals("manufacturerURL")) {
        info.setManufacturerURL(readTextNode(parser, "manufacturerURL"));
        continue;
      }
      if (name.equals("modelDescription")) {
        info.setModelDescription(readTextNode(parser, "modelDescription"));
        continue;
      }
      if (name.equals("modelName")) {
        info.setModelName(readTextNode(parser, "modelName"));
        continue;
      }
      if (name.equals("modelNumber")) {
        info.setModelNumber(readTextNode(parser, "modelNumber"));
        continue;
      }
      if (name.equals("modelURL")) {
        info.setModelURL(readTextNode(parser, "modelURL"));
        continue;
      }
      if (name.equals("serialNumber")) {
        info.setSerialNumber(readTextNode(parser, "serialNumber"));
        continue;
      }
      if (name.equals("UDN")) {
        info.setUDN(readTextNode(parser, "UDN"));
        continue;
      }
      skip(parser);
    }
    parser.require(XmlPullParser.END_TAG, null, "device");
  }
  private String readTextNode(XmlPullParser parser, String nodeName) throws XmlPullParserException,
      IOException {
    parser.require(XmlPullParser.START_TAG, null, nodeName);
    String text = readText(parser);
    parser.require(XmlPullParser.END_TAG, null, nodeName);
    return text;
  }

  private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
    String result = "";
    if (parser.next() == XmlPullParser.TEXT) {
      result = parser.getText();
      parser.nextTag();
    }
    return result;
  }

  private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
    if (parser.getEventType() != XmlPullParser.START_TAG) {
      throw new IllegalStateException();
    }
    int depth = 1;
    while (depth != 0) {
      switch (parser.next()) {
      case XmlPullParser.END_TAG:
        depth--;
        break;
      case XmlPullParser.START_TAG:
        depth++;
        break;
      }
    }
  }
}
