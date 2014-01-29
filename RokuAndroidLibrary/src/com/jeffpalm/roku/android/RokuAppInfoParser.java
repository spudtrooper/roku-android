package com.jeffpalm.roku.android;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;

final class RokuAppInfoParser {

  private final List<RokuAppInfo> infos;

  public RokuAppInfoParser(List<RokuAppInfo> infos) {
    this.infos = infos;
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
    parser.require(XmlPullParser.START_TAG, null, "apps");
    while (parser.next() != XmlPullParser.END_TAG) {
      if (parser.getEventType() != XmlPullParser.START_TAG) {
        continue;
      }
      String name = parser.getName();
      if (name.equals("app")) {
        RokuAppInfo app = new RokuAppInfo();
        readApp(parser, app);
        infos.add(app);
      } else {
        skip(parser);
      }
    }
  }
  private void readApp(XmlPullParser parser, RokuAppInfo app) throws XmlPullParserException,
      IOException {
    parser.require(XmlPullParser.START_TAG, null, "app");
    String id = parser.getAttributeValue(null, "id");
    String version = parser.getAttributeValue(null, "version");
    app.setId(Integer.parseInt(id));
    app.setVersion(version);
    String name = readText(parser);
    app.setName(name);
    parser.require(XmlPullParser.END_TAG, null, "app");
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
