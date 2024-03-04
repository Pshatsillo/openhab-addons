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
package org.openhab.binding.pikkomfortdomofon.discovery;

import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.binding.pikkomfortdomofon.dto.PikKomfortDomofonBuildings;
import org.openhab.binding.pikkomfortdomofon.dto.PikKomfortDomofonProperties;
import org.openhab.binding.pikkomfortdomofon.handler.PikKomfortDomofonBridge;
import org.openhab.binding.pikkomfortdomofon.internal.PikKomfortDomofonBindingConstants;
import org.openhab.core.config.discovery.AbstractDiscoveryService;
import org.openhab.core.config.discovery.DiscoveryResult;
import org.openhab.core.config.discovery.DiscoveryResultBuilder;
import org.openhab.core.config.discovery.DiscoveryService;
import org.openhab.core.thing.ThingUID;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Discovery service for Pik komfort
 *
 * @author Petr Shatsillo - Initial contribution
 *
 */
@Component(service = DiscoveryService.class, configurationPid = "discovery.pikkomfortdomofon")
@NonNullByDefault
public class PikKomfortDomofonDiscoveryService extends AbstractDiscoveryService {
    private static final Logger logger = LoggerFactory.getLogger(PikKomfortDomofonDiscoveryService.class);
    @Nullable
    DatagramSocket socket;
    private @Nullable ScheduledFuture<?> backgroundFuture;
    @Nullable
    public static List<PikKomfortDomofonBridge> bridgeList = new ArrayList<>();

    public PikKomfortDomofonDiscoveryService() {
        super(Collections.singleton(PikKomfortDomofonBindingConstants.THING_BUILDING), 30, true);
    }

    @Override
    public synchronized void abortScan() {
        logger.info("abortScan");
        super.abortScan();
    }

    @Override
    protected synchronized void stopScan() {
        logger.info("stopScan");
        super.stopScan();
    }

    @Override
    protected void startScan() {
        logger.info("StartScan");
    }

    @Override
    protected void startBackgroundDiscovery() {
        logger.error("startBackgroundDiscovery");
        backgroundFuture = scheduler.scheduleWithFixedDelay(this::scan, 0, 30, TimeUnit.SECONDS);
    }

    @Override
    protected void stopBackgroundDiscovery() {
        logger.error("stopBackgroundDiscovery");
        ScheduledFuture<?> scan = backgroundFuture;
        if (scan != null) {
            scan.cancel(true);
            backgroundFuture = null;
        }
        super.stopBackgroundDiscovery();
    }

    private synchronized void scan() {
        logger.info("Scanning...");
        final List<PikKomfortDomofonBridge> bridgeList = PikKomfortDomofonDiscoveryService.bridgeList;
        if (bridgeList != null) {
            bridgeList.forEach(pikKomfortDomofonBridge -> {
                final PikKomfortDomofonProperties propertiesList = pikKomfortDomofonBridge.domofon.properties;
                if (propertiesList != null) {
                    final List<PikKomfortDomofonProperties.Apartments> apartments = propertiesList.getApartments();
                    apartments.forEach(apartments1 -> {
                        final List<PikKomfortDomofonBuildings> buildingsList = pikKomfortDomofonBridge.domofon.buildingsList;
                        if (buildingsList != null) {
                            buildingsList.forEach(pikKomfortDomofonBuildings -> {
                                if (pikKomfortDomofonBuildings.getId().equals(apartments1.getBuildingId())) {
                                    ThingUID thingUID = new ThingUID(PikKomfortDomofonBindingConstants.THING_BUILDING,
                                            pikKomfortDomofonBridge.getThing().getUID(),
                                            pikKomfortDomofonBuildings.getId() + "_"
                                                    + pikKomfortDomofonBuildings.getDistrictId());
                                    DiscoveryResult resultS = DiscoveryResultBuilder.create(thingUID)
                                            .withLabel(pikKomfortDomofonBuildings.getStreet() + "_"
                                                    + pikKomfortDomofonBuildings.getHouse() + " "
                                                    + pikKomfortDomofonBuildings.getBuilding() + " ")
                                            .withBridge(pikKomfortDomofonBridge.getThing().getUID()).build();
                                    thingDiscovered(resultS);
                                }
                            });
                        }
                    });

                }
            });
        }
    }
}
