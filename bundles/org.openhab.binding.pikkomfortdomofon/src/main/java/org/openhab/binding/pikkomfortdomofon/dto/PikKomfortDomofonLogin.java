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

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.google.gson.annotations.SerializedName;

/**
 * The {@link PikKomfortDomofonLogin} is describing login model json serialize.
 *
 * @author Petr Shatsillo - Initial contribution
 */
@NonNullByDefault
public class PikKomfortDomofonLogin implements Serializable {
    @Serial
    private static final long serialVersionUID = -1748312966538510299L;
    Account account = new Account();
    @SerializedName("customer_device")
    CustomerDevice customerDevice = new CustomerDevice();

    public PikKomfortDomofonLogin(String username, String password) {
        account.phone = username;
        account.password = password;
    }

    public CustomerDevice getCustomerDevice() {
        return this.customerDevice;
    }

    public Account getAccount() {
        return this.account;
    }

    public static class Account implements Serializable {
        @Serial
        private static final long serialVersionUID = -1748312966538510299L;
        public String phone = "";
        public String password = "";

        public String getPhone() {
            return this.phone;
        }

        public String getPassword() {
            return this.password;
        }
    }

    public static class CustomerDevice implements Serializable {
        @Serial
        private static final long serialVersionUID = -1748312966538510299L;
        public String uid = "";

        public String getUid() {
            return this.uid;
        }
    }
}
