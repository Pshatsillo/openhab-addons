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
 * The {@link PikKomfortDomofonUsers} is describing login model json serialize.
 *
 * @author Petr Shatsillo - Initial contribution
 */
@NonNullByDefault
public class PikKomfortDomofonUsers implements Serializable {
    @Serial
    private static final long serialVersionUID = -1748312966538510299L;
    private Account account = new Account();
    @SerializedName("customer_devices")
    private List<CustomerDevices> customerDevices = new ArrayList<>();

    public Account getAccount() {
        return this.account;
    }

    public List<CustomerDevices> getCustomerDevices() {
        return this.customerDevices;
    }

    public void setCustomerDevices(CustomerDevices customerDevices) {
        this.customerDevices.add(customerDevices);
    }

    public static class Account implements Serializable {
        @Serial
        private static final long serialVersionUID = -1748312966538510299L;
        @SerializedName("apartment_id")
        private Integer apartmentId = 0;
        private String number = "";
        private String phone = "";
        @SerializedName("last_name")
        private String lastName = "";
        private Integer id = 0;
        @SerializedName("middle_name")
        private String middleName = "";
        @SerializedName("first_name")
        private String firstName = "";
        private String email = "";

        public Integer getApartmentId() {
            return this.apartmentId;
        }

        public String getNumber() {
            return this.number;
        }

        public String getPhone() {
            return this.phone;
        }

        public String getLastName() {
            return this.lastName;
        }

        public Integer getId() {
            return this.id;
        }

        public String getMiddleName() {
            return this.middleName;
        }

        public String getFirstName() {
            return this.firstName;
        }

        public String getEmail() {
            return this.email;
        }
    }

    public static class CustomerDevices implements Serializable {
        @Serial
        private static final long serialVersionUID = -1748312966538510299L;
        @SerializedName("apartment_id")
        private Integer apartmentId = 0;
        private String uid = "";
        @SerializedName("account_id")
        private Integer accountId = 0;
        private String os = "";
        private String kind = "";
        @SerializedName("mac_address")
        private String macAddress = "";
        @SerializedName("sip_account")
        private SipAccount sipAccount = new SipAccount();
        private String model = "";
        private Integer id = 0;
        @SerializedName("firmware_version")
        private String firmwareVersion = "";
        @SerializedName("deleted_at")
        private String deletedAt = "";

        public Integer getApartmentId() {
            return this.apartmentId;
        }

        public String getUid() {
            return this.uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public Integer getAccountId() {
            return this.accountId;
        }

        public void setAccountId(int accountId) {
            this.accountId = accountId;
        }

        public String getOs() {
            return this.os;
        }

        public void setOs(String os) {
            this.os = os;
        }

        public String getKind() {
            return this.kind;
        }

        public void setKind(String kind) {
            this.kind = kind;
        }

        public String getMacAddress() {
            return this.macAddress;
        }

        public SipAccount getSipAccount() {
            return this.sipAccount;
        }

        public void setSipAccount(SipAccount sip) {
            this.sipAccount = sip;
        }

        public String getModel() {
            return this.model;
        }

        public void setModel(String model) {
            this.model = model;
        }

        public Integer getId() {
            return this.id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public Object getFirmwareVersion() {
            return this.firmwareVersion;
        }

        public String getDeletedAt() {
            return this.deletedAt;
        }

        public static class SipAccount implements Serializable {
            @Serial
            private static final long serialVersionUID = -1748312966538510299L;
            private String proxy = "";

            private String password = "";

            private String alias = "";

            private String realm = "";
            @SerializedName("ex_user")
            private Integer exUser = 0;
            @SerializedName("remote_request_status")
            private String remoteRequestStatus = "";
            @SerializedName("ex_enable")
            private Boolean exEnable = false;

            public String getProxy() {
                return this.proxy;
            }

            public void setProxy(String proxy) {
                this.proxy = proxy;
            }

            public String getPassword() {
                return this.password;
            }

            public void setPassword(String password) {
                this.password = password;
            }

            public String getAlias() {
                return this.alias;
            }

            public String getRealm() {
                return this.realm;
            }

            public Integer getExUser() {
                return this.exUser;
            }

            public void setExUser(int exUser) {
                this.exUser = exUser;
            }

            public String getRemoteRequestStatus() {
                return this.remoteRequestStatus;
            }

            public Boolean getExEnable() {
                return this.exEnable;
            }
        }
    }
}
