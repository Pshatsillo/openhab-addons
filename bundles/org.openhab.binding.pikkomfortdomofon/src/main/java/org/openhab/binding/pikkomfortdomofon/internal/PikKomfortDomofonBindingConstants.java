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
package org.openhab.binding.pikkomfortdomofon.internal;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.openhab.core.thing.ThingTypeUID;

/**
 * The {@link PikKomfortDomofonBindingConstants} class defines common constants, which are
 * used across the whole binding.
 *
 * @author Petr Shatsillo - Initial contribution
 */
@NonNullByDefault
public class PikKomfortDomofonBindingConstants {

    public static final String BINDING_ID = "pikkomfortdomofon";

    // List of all Thing Type UIDs
    public static final ThingTypeUID THING_BUILDING = new ThingTypeUID(BINDING_ID, "domofon");
    public static final ThingTypeUID THING_BRIDGE = new ThingTypeUID(BINDING_ID, "bridge");
    public static final String PIK_INTERCOM_URL = "https://intercom.rubetek.com";
    public static final String PIK_IOT_URL = "https://iot.rubetek.com";

    public static final String CHANNEL_SNAPSHOT_URL = "snapshotUrl";
    public static final String CHANNEL_SNAPSHOT = "snapshot";
    public static final String CHANNEL_RTSP_URL = "rtspUrl";
    public static final String CHANNEL_RELAY = "relay";
}
