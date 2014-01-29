package com.jeffpalm.roku.android;

import java.util.Arrays;
import java.util.List;

public class RokuTestUtils {
  
  /** My host (TODO(jeff) DOn't hard core this and use a mock. */
  public final static String HOST = "192.168.1.114";
  
  /** The name of the file that contains app xml. */
  public final static String TEST_APP_XML = "query_apps.xml";

  /** The list of app infos contained in the XML of {@link #TEST_APP_XML}. */
  public final static List<RokuAppInfo> TEST_APP_INFOS = Arrays.asList(new RokuAppInfo().setId(12)
      .setVersion("3.1.6014").setName("Netflix"), new RokuAppInfo().setId(13).setVersion("5.1.3")
      .setName("Amazon Instant Video"),
      new RokuAppInfo().setId(2285).setVersion("2.7.6").setName("Hulu Plus"), new RokuAppInfo()
          .setId(2542).setVersion("0.0.0").setName("Media Player"), new RokuAppInfo().setId(2898)
          .setVersion("0.1.0").setName("Weather Underground"), new RokuAppInfo().setId(2906)
          .setVersion("1.0.101109").setName("blip.tv home"), new RokuAppInfo().setId(2946)
          .setVersion("2.0.33").setName("Fox News Channel"), new RokuAppInfo().setId(3423)
          .setVersion("1.4.21").setName("Rdio"), new RokuAppInfo().setId(28).setVersion("3.1.8")
          .setName("Pandora"),
      new RokuAppInfo().setId(15).setVersion("2.1.103").setName("Facebook Photos & Videos"),
      new RokuAppInfo().setId(45).setVersion("1.9.0").setName("Revision3"), new RokuAppInfo()
          .setId(121).setVersion("1.0.4").setName("Flickr"), new RokuAppInfo().setId(199)
          .setVersion("2.50.11").setName("TWiT.tv"),
      new RokuAppInfo().setId(1616).setVersion("0.0.0").setName("NASA TV"), new RokuAppInfo()
          .setId(1719).setVersion("1.0.100").setName("LastFM"), new RokuAppInfo().setId(1883)
          .setVersion("1.0.3").setName("Picasa Web Albums"), new RokuAppInfo().setId(2016)
          .setVersion("3.2.9").setName("Crackle"),
      new RokuAppInfo().setId(2251).setVersion("1.3.12").setName("CNET"),
      new RokuAppInfo().setId(14).setVersion("3.0.34").setName("MLB.TV"),
      new RokuAppInfo().setId(13842).setVersion("1.3.6").setName("VUDU"),
      new RokuAppInfo().setId(21952).setVersion("2.3.136").setName("Blockbuster"),
      new RokuAppInfo().setId(6119).setVersion("1.5.0").setName("Popcornflix"), new RokuAppInfo()
          .setId(13535).setVersion("2.8.4").setName("Plex"));

  /** The name fo the file that contains a device info. */
  public static final String DEVICE_INFO_XML = "device_info.xml";

  public static final Comparable<RokuDeviceInfo> TEST_DEVICE_INFO_COMPARABLE = new Comparable<RokuDeviceInfo>() {
    public int compareTo(RokuDeviceInfo info) {
      return "urn:roku-com:device:player:1-0".equals(info.getDeviceType())
          && "Roku Streaming Player".equals(info.getFriendlyName())
          && "Roku".equals(info.getManufacturer())
          && "http://www.roku.com/".equals(info.getManufacturerURL())
          && "Roku Streaming Player Network Media".equals(info.getModelDescription())
          && "Roku Streaming Player 3050X".equals(info.getModelName())
          && "3050X".equals(info.getModelNumber())
          && "http://www.roku.com/".equals(info.getModelURL())
          && "12G34V024290".equals(info.getSerialNumber())
          && "uuid:31324733-3456-e25e-0000-0503f70b0000".equals(info.getUDN()) ? 0 : 1;
    }
  };
}
