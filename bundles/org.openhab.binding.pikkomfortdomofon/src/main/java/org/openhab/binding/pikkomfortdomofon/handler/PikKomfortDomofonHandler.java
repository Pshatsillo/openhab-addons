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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.binding.pikkomfortdomofon.api.PikKomfortDomofonAPI;
import org.openhab.binding.pikkomfortdomofon.dto.PikKomfortDomofonIntercoms;
import org.openhab.binding.pikkomfortdomofon.internal.PikKomfortDomofonBindingConstants;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.RawType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.thing.Bridge;
import org.openhab.core.thing.Channel;
import org.openhab.core.thing.ChannelUID;
import org.openhab.core.thing.Thing;
import org.openhab.core.thing.ThingStatus;
import org.openhab.core.thing.ThingStatusDetail;
import org.openhab.core.thing.binding.BaseThingHandler;
import org.openhab.core.thing.binding.ThingHandler;
import org.openhab.core.thing.binding.builder.ChannelBuilder;
import org.openhab.core.thing.binding.builder.ThingBuilder;
import org.openhab.core.thing.type.ChannelTypeUID;
import org.openhab.core.types.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link PikKomfortDomofonHandler} is responsible for handling commands, which are
 * sent to one of the channels.
 *
 * @author Petr Shatsillo - Initial contribution
 */
@NonNullByDefault
public class PikKomfortDomofonHandler extends BaseThingHandler {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    private @Nullable ScheduledFuture<?> refreshPollingJob;
    @Nullable
    public PikKomfortDomofonBridge bridgeDeviceHandler;

    public PikKomfortDomofonHandler(Thing thing) {
        super(thing);
    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {
        final List<PikKomfortDomofonIntercoms> intercomsList = PikKomfortDomofonAPI.intercomsList;
        if (intercomsList != null) {
            intercomsList.forEach(pikKomfortDomofonIntercoms -> {
                if (pikKomfortDomofonIntercoms.getStatus().equals("online")) {
                    if (!pikKomfortDomofonIntercoms.getRelays().isEmpty()) {
                        List<PikKomfortDomofonIntercoms.Relays> relay = pikKomfortDomofonIntercoms.getRelays();
                        relay.forEach(relays -> {
                            String uid = channelUID.getId();
                            String relayId = uid.split("_")[1];
                            if (relays.getUserSettings().getIsFavorite()) {
                                if (relays.getId().equals(Integer.parseInt(relayId))) {
                                    final PikKomfortDomofonBridge bridgeDeviceHandler = this.bridgeDeviceHandler;
                                    if (bridgeDeviceHandler != null) {
                                        bridgeDeviceHandler.unlockDoor(relays.getId());
                                        try {
                                            Thread.sleep(3000);
                                        } catch (InterruptedException e) {
                                        }
                                        updateState(channelUID.getId(), OnOffType.OFF);
                                    }
                                }
                            }
                        });
                    }
                }
            });
        }
    }

    @Override
    public void initialize() {
        updateStatus(ThingStatus.UNKNOWN);
        bridgeDeviceHandler = getBridgeHandler();
        // scheduler.execute(() -> {
        // boolean thingReachable = true;
        // if (thingReachable) {
        // updateStatus(ThingStatus.ONLINE);
        // } else {
        // updateStatus(ThingStatus.OFFLINE);
        // }
        // });
        final PikKomfortDomofonBridge bridgeDeviceHandler = this.bridgeDeviceHandler;
        if (bridgeDeviceHandler != null) {
            int reconnect = 0;
            while (!bridgeDeviceHandler.getThing().getStatus().equals(ThingStatus.ONLINE)) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ignored) {
                }
                if (reconnect == 10) {
                    logger.error("Bridge is offline during 10 seconds");
                    updateStatus(ThingStatus.UNINITIALIZED, ThingStatusDetail.BRIDGE_UNINITIALIZED,
                            "Bridge is offline during 10 seconds");
                    break;
                }
                reconnect++;
            }
            if (bridgeDeviceHandler.getThing().getStatus().equals(ThingStatus.ONLINE)) {
                List<Channel> channelList = new ArrayList<>();
                final List<PikKomfortDomofonIntercoms> intercomsList = PikKomfortDomofonAPI.intercomsList;
                if (intercomsList != null) {
                    intercomsList.forEach(pikKomfortDomofonIntercoms -> {
                        if (pikKomfortDomofonIntercoms.getStatus().equals("online")) {
                            if (!pikKomfortDomofonIntercoms.getRelays().isEmpty()) {
                                List<PikKomfortDomofonIntercoms.Relays> relay = pikKomfortDomofonIntercoms.getRelays();
                                relay.forEach(relays -> {
                                    if (!relays.getLiveSnapshotUrl().isEmpty()) {
                                        if (relays.getUserSettings().getIsFavorite()) {
                                            ChannelUID snapshotUID = new ChannelUID(thing.getUID(),
                                                    PikKomfortDomofonBindingConstants.CHANNEL_SNAPSHOT + "_"
                                                            + relays.getId());
                                            Channel snapshot = ChannelBuilder.create(snapshotUID)
                                                    .withType(new ChannelTypeUID(
                                                            PikKomfortDomofonBindingConstants.BINDING_ID,
                                                            PikKomfortDomofonBindingConstants.CHANNEL_SNAPSHOT))
                                                    .withLabel(relays.getUserSettings().getCustomName() + " Картинка")
                                                    .withAcceptedItemType("Image").build();
                                            channelList.add(snapshot);
                                            ChannelUID snapshotUrlUID = new ChannelUID(thing.getUID(),
                                                    PikKomfortDomofonBindingConstants.CHANNEL_SNAPSHOT_URL + "_"
                                                            + relays.getId());
                                            Channel snapshotUrl = ChannelBuilder.create(snapshotUrlUID)
                                                    .withType(new ChannelTypeUID(
                                                            PikKomfortDomofonBindingConstants.BINDING_ID,
                                                            PikKomfortDomofonBindingConstants.CHANNEL_SNAPSHOT_URL))
                                                    .withLabel(relays.getUserSettings().getCustomName()
                                                            + " Изображение URL")
                                                    .withAcceptedItemType("String").build();
                                            channelList.add(snapshotUrl);
                                            ChannelUID rtspUrlUID = new ChannelUID(thing.getUID(),
                                                    PikKomfortDomofonBindingConstants.CHANNEL_RTSP_URL + "_"
                                                            + relays.getId());
                                            Channel rtspUrl = ChannelBuilder.create(rtspUrlUID)
                                                    .withType(new ChannelTypeUID(
                                                            PikKomfortDomofonBindingConstants.BINDING_ID,
                                                            PikKomfortDomofonBindingConstants.CHANNEL_RTSP_URL))
                                                    .withLabel(relays.getUserSettings().getCustomName() + " Видео URL")
                                                    .withAcceptedItemType("String").build();
                                            channelList.add(rtspUrl);
                                            ChannelUID relayUID = new ChannelUID(thing.getUID(),
                                                    PikKomfortDomofonBindingConstants.CHANNEL_RELAY + "_"
                                                            + relays.getId());
                                            Channel relayCnl = ChannelBuilder.create(relayUID)
                                                    .withType(new ChannelTypeUID(
                                                            PikKomfortDomofonBindingConstants.BINDING_ID,
                                                            PikKomfortDomofonBindingConstants.CHANNEL_RELAY))
                                                    .withLabel(relays.getUserSettings().getCustomName() + " Дверь")
                                                    .withAcceptedItemType("Switch").build();
                                            channelList.add(relayCnl);
                                        }
                                    }
                                });
                            }
                        }
                    });
                    ScheduledFuture<?> refreshPollingJob = this.refreshPollingJob;
                    if (refreshPollingJob == null || refreshPollingJob.isCancelled()) {
                        refreshPollingJob = scheduler.scheduleWithFixedDelay(this::refresh, 10, 10, TimeUnit.SECONDS);
                        this.refreshPollingJob = refreshPollingJob;
                    }
                    ThingBuilder thingBuilder = editThing();
                    thingBuilder.withChannels(channelList);
                    updateThing(thingBuilder.build());
                    refresh();
                    updateStatus(ThingStatus.ONLINE);
                } else {
                    logger.error("No itercoms available");
                    updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.HANDLER_INITIALIZING_ERROR,
                            "No itercoms available");
                }

            }
        }
    }

    private void refresh() {
        for (Channel channel : getThing().getChannels()) {
            if (isLinked(channel.getUID().getId())) {
                final List<PikKomfortDomofonIntercoms> intercomsList = PikKomfortDomofonAPI.intercomsList;
                if (intercomsList != null) {
                    String uid = channel.getUID().getId();
                    String relayId = uid.split("_")[1];
                    intercomsList.forEach(pikKomfortDomofonIntercoms -> {
                        if (pikKomfortDomofonIntercoms.getStatus().equals("online")) {
                            List<PikKomfortDomofonIntercoms.Relays> relay = pikKomfortDomofonIntercoms.getRelays();
                            relay.forEach(relays -> {
                                if (relays.getId().equals(Integer.parseInt(relayId))) {
                                    if (channel.getUID().getId().split("_")[0]
                                            .equals(PikKomfortDomofonBindingConstants.CHANNEL_SNAPSHOT)) {
                                        logger.debug("Getting urls and status of relay {}", relays.getId());
                                        try {
                                            ByteArrayOutputStream byteStreamOutput = new ByteArrayOutputStream();
                                            URL url = new URL(relays.getLiveSnapshotUrl());
                                            URLConnection urlConnection = url.openConnection();
                                            InputStream inputStream = urlConnection.getInputStream();
                                            int n = 0;
                                            byte[] buffer = new byte[1024];
                                            do {
                                                n = inputStream.read(buffer);
                                                if (n > 0) {
                                                    byteStreamOutput.write(buffer, 0, n);
                                                }
                                            } while (n > 0);
                                            byte[] img = byteStreamOutput.toByteArray();
                                            RawType rt = new RawType(img, "image/jpeg");
                                            updateState(channel.getUID().getId(), rt);
                                        } catch (IOException e) {
                                            logger.debug("Fetching snapshot error {}", e.getLocalizedMessage());
                                        }
                                    } else if (channel.getUID().getId().split("_")[0]
                                            .equals(PikKomfortDomofonBindingConstants.CHANNEL_SNAPSHOT_URL)) {
                                        updateState(channel.getUID().getId(),
                                                StringType.valueOf(relays.getLiveSnapshotUrl()));
                                    } else if (channel.getUID().getId().split("_")[0]
                                            .equals(PikKomfortDomofonBindingConstants.CHANNEL_RTSP_URL)) {
                                        updateState(channel.getUID().getId(), StringType.valueOf(relays.getRtspUrl()));
                                    } else if (channel.getUID().getId().split("_")[0]
                                            .equals(PikKomfortDomofonBindingConstants.CHANNEL_RELAY)) {
                                        updateState(channel.getUID().getId(), OnOffType.OFF);
                                    }

                                }
                            });
                        }
                    });

                } else {
                    logger.error("No itercoms available");
                }
            }
        }
    }

    private synchronized @Nullable PikKomfortDomofonBridge getBridgeHandler() {
        Bridge bridge = getBridge();
        if (bridge == null) {
            logger.error("Required bridge not defined for device.");
            return null;
        } else {
            return getBridgeHandler(bridge);
        }
    }

    private synchronized @Nullable PikKomfortDomofonBridge getBridgeHandler(Bridge bridge) {
        ThingHandler handler = bridge.getHandler();
        if (handler instanceof PikKomfortDomofonBridge) {
            return (PikKomfortDomofonBridge) handler;
        } else {
            logger.debug("No available bridge handler found yet. Bridge: {} .", bridge.getUID());
            return null;
        }
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
}
