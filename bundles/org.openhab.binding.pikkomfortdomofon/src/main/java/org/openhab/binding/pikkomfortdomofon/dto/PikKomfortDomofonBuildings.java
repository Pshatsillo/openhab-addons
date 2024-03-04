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
 * The {@link PikKomfortDomofonBuildings} is describing login model json serialize.
 *
 * @author Petr Shatsillo - Initial contribution
 */
@NonNullByDefault
public class PikKomfortDomofonBuildings implements Serializable {
    @Serial
    private static final long serialVersionUID = -1748312966538510299L;
    @SerializedName("entrances_count")
    private Integer entrancesCount = 0;

    private String housing = "";

    private String street = "";

    private List<String> location = new ArrayList<>();

    private Integer id = 0;
    @SerializedName("district_id")
    private Integer districtId = 0;

    private String house = "";

    private String building = "";
    @SerializedName("management_company")
    private ManagementCompany managementCompany = new ManagementCompany();

    public Integer getEntrancesCount() {
        return this.entrancesCount;
    }

    public void setEntrancesCount(Integer entrancesCount) {
        this.entrancesCount = entrancesCount;
    }

    public String getHousing() {
        return this.housing;
    }

    public void setHousing(String housing) {
        this.housing = housing;
    }

    public String getStreet() {
        return this.street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public List<String> getLocation() {
        return this.location;
    }

    public void setLocation(List<String> location) {
        this.location = location;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getDistrictId() {
        return this.districtId;
    }

    public void setDistrictId(Integer districtId) {
        this.districtId = districtId;
    }

    public String getHouse() {
        return this.house;
    }

    public void setHouse(String house) {
        this.house = house;
    }

    public String getBuilding() {
        return this.building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public ManagementCompany getManagementCompany() {
        return this.managementCompany;
    }

    public void setManagementCompany(ManagementCompany managementCompany) {
        this.managementCompany = managementCompany;
    }

    public static class ManagementCompany implements Serializable {
        @Serial
        private static final long serialVersionUID = -1748312966538510299L;
        @SerializedName("work_schedule")
        private WorkSchedule workSchedule = new WorkSchedule();
        @SerializedName("phone_numbers")
        private List<PhoneNumbers> phoneNumbers = new ArrayList<>();

        private List<Images> images = new ArrayList<>();

        private String address = "";

        private String name = "";

        private Object coordinates = new Object();

        private List<Links> links = new ArrayList<>();

        private Integer id = 0;

        public WorkSchedule getWorkSchedule() {
            return this.workSchedule;
        }

        public void setWorkSchedule(WorkSchedule workSchedule) {
            this.workSchedule = workSchedule;
        }

        public List<PhoneNumbers> getPhoneNumbers() {
            return this.phoneNumbers;
        }

        public void setPhoneNumbers(List<PhoneNumbers> phoneNumbers) {
            this.phoneNumbers = phoneNumbers;
        }

        public List<Images> getImages() {
            return this.images;
        }

        public void setImages(List<Images> images) {
            this.images = images;
        }

        public String getAddress() {
            return this.address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getName() {
            return this.name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Object getCoordinates() {
            return this.coordinates;
        }

        public void setCoordinates(Object coordinates) {
            this.coordinates = coordinates;
        }

        public List<Links> getLinks() {
            return this.links;
        }

        public void setLinks(List<Links> links) {
            this.links = links;
        }

        public Integer getId() {
            return this.id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public static class WorkSchedule implements Serializable {
            @Serial
            private static final long serialVersionUID = -1748312966538510299L;
            private WorkTime thuesday = new WorkTime();
            private WorkTime sunday = new WorkTime();;
            private WorkTime saturday = new WorkTime();;
            private WorkTime wednesday = new WorkTime();;
            private WorkTime thursday = new WorkTime();;
            private WorkTime friday = new WorkTime();;
            private WorkTime monday = new WorkTime();;

            public WorkTime getThuesday() {
                return this.thuesday;
            }

            public void setThuesday(WorkTime thuesday) {
                this.thuesday = thuesday;
            }

            public WorkTime getSunday() {
                return this.sunday;
            }

            public void setSunday(WorkTime sunday) {
                this.sunday = sunday;
            }

            public WorkTime getSaturday() {
                return this.saturday;
            }

            public void setSaturday(WorkTime saturday) {
                this.saturday = saturday;
            }

            public WorkTime getWednesday() {
                return this.wednesday;
            }

            public void setWednesday(WorkTime wednesday) {
                this.wednesday = wednesday;
            }

            public WorkTime getThursday() {
                return this.thursday;
            }

            public void setThursday(WorkTime thursday) {
                this.thursday = thursday;
            }

            public WorkTime getFriday() {
                return this.friday;
            }

            public void setFriday(WorkTime friday) {
                this.friday = friday;
            }

            public WorkTime getMonday() {
                return this.monday;
            }

            public void setMonday(WorkTime monday) {
                this.monday = monday;
            }

            public static class WorkTime implements Serializable {
                @Serial
                private static final long serialVersionUID = -1748312966538510299L;
                private String from = "";

                private String to = "";

                private String key = "";

                public String getFrom() {
                    return this.from;
                }

                public void setFrom(String from) {
                    this.from = from;
                }

                public String getTo() {
                    return this.to;
                }

                public void setTo(String to) {
                    this.to = to;
                }

                public String getKey() {
                    return this.key;
                }

                public void setKey(String key) {
                    this.key = key;
                }
            }
        }

        public static class PhoneNumbers implements Serializable {
            @Serial
            private static final long serialVersionUID = -1748312966538510299L;
            private String phone = "";

            public String getPhone() {
                return this.phone;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }
        }

        public static class Images implements Serializable {
            @Serial
            private static final long serialVersionUID = -1748312966538510299L;
            @SerializedName("content_type")
            private String contentType = "";

            private String url = "";
            @SerializedName("file_size")
            private Integer fileSize = 0;

            public String getContentType() {
                return this.contentType;
            }

            public void setContentType(String contentType) {
                this.contentType = contentType;
            }

            public String getUrl() {
                return this.url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public Integer getFileSize() {
                return this.fileSize;
            }

            public void setFileSize(Integer fileSize) {
                this.fileSize = fileSize;
            }
        }

        public static class Links implements Serializable {
            @Serial
            private static final long serialVersionUID = -1748312966538510299L;
            private String url = "";

            public String getUrl() {
                return this.url;
            }

            public void setUrl(String url) {
                this.url = url;
            }
        }
    }
}
