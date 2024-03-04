/**
 * Copyright (c) 2010-2024 Contributors to the openHAB project
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.openhab.binding.pikkomfortdomofon.dto;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.google.gson.annotations.SerializedName;

/**
 * The {@link PikKomfortDomofonIntercoms} is describing login model json serialize.
 *
 * @author Petr Shatsillo - Initial contribution
 */
@NonNullByDefault
public class PikKomfortDomofonIntercoms implements Serializable {
    @Serial
    private static final long serialVersionUID = -1748312966538510299L;
    @SerializedName("is_face_detection")
    private Boolean isFaceDetection = false;
    @SerializedName("sip_account")
    private SipAccount sipAccount = new SipAccount();
    @SerializedName("live_snapshot_url")
    private String liveSnapshotUrl = "";
    private String name = "";
    private List<Relays> relays = new ArrayList<>();
    private Integer id = 0;
    @SerializedName("geo_unit")
    private PikKomfortDomofonIntercoms.geoUnit geoUnit = new geoUnit();
    @SerializedName("client_id")
    private Integer clientId = 0;
    @SerializedName("webrtc_supported")
    private Boolean webrtcSupported = false;
    private String status = "";

    public Boolean getIsFaceDetection() {
        return this.isFaceDetection;
    }

    public void setIsFaceDetection(Boolean isFaceDetection) {
        this.isFaceDetection = isFaceDetection;
    }

    public SipAccount getSipAccount() {
        return this.sipAccount;
    }

    public void setSipAccount(SipAccount sipAccount) {
        this.sipAccount = sipAccount;
    }

    public Object getLiveSnapshotUrl() {
        return this.liveSnapshotUrl;
    }

    public void setLiveSnapshotUrl(String liveSnapshotUrl) {
        this.liveSnapshotUrl = liveSnapshotUrl;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Relays> getRelays() {
        return this.relays;
    }

    public void setRelays(List<Relays> relays) {
        this.relays = relays;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public PikKomfortDomofonIntercoms.geoUnit getGeoUnit() {
        return this.geoUnit;
    }

    public void setGeoUnit(PikKomfortDomofonIntercoms.geoUnit geoUnit) {
        this.geoUnit = geoUnit;
    }

    public Integer getClientId() {
        return this.clientId;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    public Boolean getWebrtcSupported() {
        return this.webrtcSupported;
    }

    public void setWebrtcSupported(Boolean webrtcSupported) {
        this.webrtcSupported = webrtcSupported;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static class Relays implements Serializable {
        @Serial
        private static final long serialVersionUID = -1748312966538510299L;
        @SerializedName("user_settings")
        private UserSettings userSettings = new UserSettings();
        @SerializedName("rtsp_url")
        private String rtspUrl = "";
        @SerializedName("live_snapshot_url")
        private String liveSnapshotUrl = "";
        private String name = "";
        private Integer id = 0;
        @SerializedName("geo_unit")
        private Geo_unit geoUnit = new Geo_unit();
        @SerializedName("property_geo_units")
        private List<Relays.propertyGeoUnits> propertyGeoUnits = new ArrayList<>();

        public UserSettings getUserSettings() {
            return this.userSettings;
        }

        public void setUserSettings(UserSettings userSettings) {
            this.userSettings = userSettings;
        }

        public String getRtspUrl() {
            return this.rtspUrl;
        }

        public void setRtspUrl(String rtspUrl) {
            this.rtspUrl = rtspUrl;
        }

        public String getLiveSnapshotUrl() {
            return this.liveSnapshotUrl;
        }

        public void setLiveSnapshotUrl(String liveSnapshotUrl) {
            this.liveSnapshotUrl = liveSnapshotUrl;
        }

        public String getName() {
            return this.name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getId() {
            return this.id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Geo_unit getGeoUnit() {
            return this.geoUnit;
        }

        public void setGeoUnit(Geo_unit geoUnit) {
            this.geoUnit = geoUnit;
        }

        public List<Relays.propertyGeoUnits> getPropertyGeoUnits() {
            return this.propertyGeoUnits;
        }

        public void setPropertyGeoUnits(List<Relays.propertyGeoUnits> propertyGeoUnits) {
            this.propertyGeoUnits = propertyGeoUnits;
        }

        public static class Geo_unit implements Serializable {
            @Serial
            private static final long serialVersionUID = -1748312966538510299L;
            @SerializedName("full_name")
            private String fullName = "";

            private Integer id = 0;

            public String getFullName() {
                return this.fullName;
            }

            public void setFullName(String fullName) {
                this.fullName = fullName;
            }

            public Integer getId() {
                return this.id;
            }

            public void setId(Integer id) {
                this.id = id;
            }
        }

        public static class propertyGeoUnits implements Serializable {
            @Serial
            private static final long serialVersionUID = -1748312966538510299L;
            @SerializedName("post_name")
            private String postName = "";
            private Integer id = 0;
            private String type = "";

            public String getPostName() {
                return this.postName;
            }

            public void setPostName(String postName) {
                this.postName = postName;
            }

            public Integer getId() {
                return this.id;
            }

            public void setId(Integer id) {
                this.id = id;
            }

            public String getType() {
                return this.type;
            }

            public void setType(String type) {
                this.type = type;
            }
        }
    }

    public static class geoUnit implements Serializable {
        @Serial
        private static final long serialVersionUID = -1748312966538510299L;
        @SerializedName("full_name")
        private String fullName = "";
        @SerializedName("short_name")
        private String shortName = "";
        private Integer id = 0;

        public String getFullName() {
            return this.fullName;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
        }

        public String getShortName() {
            return this.shortName;
        }

        public void setShortName(String shortName) {
            this.shortName = shortName;
        }

        public Integer getId() {
            return this.id;
        }

        public void setId(Integer id) {
            this.id = id;
        }
    }

    public static class SipAccount implements Serializable {
        @Serial
        private static final long serialVersionUID = -1748312966538510299L;
        Settings settings = new Settings();

        public Settings getSettings() {
            return this.settings;
        }

        public void setSettings(Settings settings) {
            this.settings = settings;
        }

        public static class Settings {
            String proxy = "";
            @SerializedName("ex_user")
            Integer exUser = 0;

            public void setProxy(String proxy) {
                this.proxy = proxy;
            }

            public String getProxy() {
                return this.proxy;
            }

            public Integer getSettings() {
                return this.exUser;
            }

            public void setSettings(Integer exUser) {
                this.exUser = exUser;
            }
        }
    }

    public static class UserSettings implements Serializable {
        @Serial
        private static final long serialVersionUID = -1748312966538510299L;
        @SerializedName("custom_name")
        private String customName = "";
        @SerializedName("is_favorite")
        private Boolean isFavorite = false;
        @SerializedName("is_hidden")
        private Boolean isHidden = false;

        public String getCustomName() {
            return this.customName;
        }

        public void setCustomName(String customName) {
            this.customName = customName;
        }

        public Boolean getIsFavorite() {
            return this.isFavorite;
        }

        public void setIsFavorite(Boolean isFavorite) {
            this.isFavorite = isFavorite;
        }

        public Boolean getIsHidden() {
            return this.isHidden;
        }

        public void setIsHidden(Boolean isHidden) {
            this.isHidden = isHidden;
        }
    }
}
