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
 * The {@link PikKomfortDomofonProperties} is describing login model json serialize.
 *
 * @author Petr Shatsillo - Initial contribution
 */
@NonNullByDefault
public class PikKomfortDomofonProperties implements Serializable {
    @Serial
    private static final long serialVersionUID = -1748312966538510299L;
    private List<Apartments> apartments = new ArrayList<>();

    public List<Apartments> getApartments() {
        return this.apartments;
    }

    public void setApartments(List<Apartments> apartments) {
        this.apartments = apartments;
    }

    public static class Apartments implements Serializable {
        @Serial
        private static final long serialVersionUID = -1748312966538510299L;
        private String number = "";
        @SerializedName("account_number")
        private Object accountNumber = new Object();
        @SerializedName("building_id")
        private Integer buildingId = 0;
        @SerializedName("scheme_id")
        private Integer schemeId = 0;

        private Integer section = 0;

        private Integer id = 0;
        @SerializedName("district_id")
        private Integer districtId = 0;

        public String getNumber() {
            return this.number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public Object getAccountNumber() {
            return this.accountNumber;
        }

        public void setAccountNumber(Object accountNumber) {
            this.accountNumber = accountNumber;
        }

        public Integer getBuildingId() {
            return this.buildingId;
        }

        public void setBuildingId(Integer buildingId) {
            this.buildingId = buildingId;
        }

        public Integer getSchemeId() {
            return this.schemeId;
        }

        public void setSchemeId(Integer schemeId) {
            this.schemeId = schemeId;
        }

        public Integer getSection() {
            return this.section;
        }

        public void setSection(Integer section) {
            this.section = section;
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
    }
}
