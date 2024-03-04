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
package org.openhab.binding.pikkomfortdomofon.handler;

import static org.openhab.binding.pikkomfortdomofon.discovery.PikKomfortDomofonDiscoveryService.bridgeList;

import java.util.Objects;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jetty.client.HttpClient;
import org.openhab.binding.pikkomfortdomofon.api.PikKomfortDomofonAPI;
import org.openhab.binding.pikkomfortdomofon.internal.PikKomfortDomofonConfiguration;
import org.openhab.core.thing.Bridge;
import org.openhab.core.thing.ChannelUID;
import org.openhab.core.thing.ThingStatus;
import org.openhab.core.thing.ThingStatusDetail;
import org.openhab.core.thing.binding.BaseBridgeHandler;
import org.openhab.core.types.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link PikKomfortDomofonBridge} is responsible for login to pik server
 *
 * @author Petr Shatsillo - Initial contribution
 */
@NonNullByDefault
public class PikKomfortDomofonBridge extends BaseBridgeHandler {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    private @Nullable ScheduledFuture<?> refreshPollingJob;
    public PikKomfortDomofonAPI domofon;

    public PikKomfortDomofonBridge(Bridge bridge, HttpClient httpClient) {
        super(bridge);
        domofon = new PikKomfortDomofonAPI(httpClient);
    }

    @Override
    public void initialize() {
        PikKomfortDomofonConfiguration config = getConfigAs(PikKomfortDomofonConfiguration.class);
        if (config.username.isEmpty() || config.password.isEmpty()) {
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.CONFIGURATION_ERROR, "Username or password is empty");
        } else {
            if (config.username.charAt(0) != '+' || config.username.length() != 12) {
                updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.CONFIGURATION_ERROR,
                        "Password is phone number must begins with + and length 11 digits");
            } else {
                if (domofon.initialize(config.username, config.password)) {
                    updateStatus(ThingStatus.ONLINE);
                } else {
                    updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.BRIDGE_UNINITIALIZED,
                            "Cannot fetch data from PIK server. Check log");
                }
            }
        }
        ScheduledFuture<?> refreshPollingJob = this.refreshPollingJob;
        if (refreshPollingJob == null || refreshPollingJob.isCancelled()) {
            refreshPollingJob = scheduler.scheduleWithFixedDelay(this::refresh, 10, 45, TimeUnit.SECONDS);
            this.refreshPollingJob = refreshPollingJob;
        }
        Objects.requireNonNull(bridgeList).add(this);
    }

    private void refresh() {
        // logger.warn("Refreshing intercoms");
        domofon.intercomsRefresh();
    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {
    }

    @Override
    public void dispose() {
        ScheduledFuture<?> refreshPollingJob = this.refreshPollingJob;
        if (refreshPollingJob != null && !refreshPollingJob.isCancelled()) {
            refreshPollingJob.cancel(true);
        }
        this.refreshPollingJob = null;
        super.dispose();
    }

    public void unlockDoor(Integer id) {
        domofon.unlockDoor(id);
    }
}
