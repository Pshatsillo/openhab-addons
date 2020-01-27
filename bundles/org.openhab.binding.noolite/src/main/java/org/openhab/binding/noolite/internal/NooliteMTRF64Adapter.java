/**
 * Copyright (c) 2010-2020 Contributors to the openHAB project
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
package org.openhab.binding.noolite.internal;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import javax.xml.bind.DatatypeConverter;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.smarthome.io.transport.serial.SerialPort;
import org.eclipse.smarthome.io.transport.serial.SerialPortEvent;
import org.eclipse.smarthome.io.transport.serial.SerialPortEventListener;
import org.eclipse.smarthome.io.transport.serial.SerialPortIdentifier;
import org.eclipse.smarthome.io.transport.serial.SerialPortManager;
import org.openhab.binding.noolite.handler.NooliteMTRF64BridgeHandler;
import org.openhab.binding.noolite.internal.config.NooliteBridgeConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Petr Shatsillo - Initial contribution
 *
 */
@NonNullByDefault
public class NooliteMTRF64Adapter {

    private final Logger logger = LoggerFactory.getLogger(NooliteMTRF64Adapter.class);
    @Nullable
    DataInputStream in = null;
    @Nullable
    DataOutputStream out = null;
    @Nullable
    Thread watcherThread = null;
    @Nullable
    private SerialPort serial;
    private SerialPortManager serialPortManager;

    public NooliteMTRF64Adapter(SerialPortManager serialPortManager) {
        super();
        this.serialPortManager = serialPortManager;
    }

    @SuppressWarnings("null")
    public void connect(NooliteBridgeConfiguration config) throws Exception {
        logger.debug("Opening serial connection to port {} with baud rate 9600...", config.serial);

        SerialPortIdentifier portIdentifier = serialPortManager.getIdentifier(config.serial);
        if (portIdentifier != null) {
            serial = portIdentifier.open("org.openhab.binding.noolite", 3000);
            if (serial != null) {
                serial.setSerialPortParams(9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);

                in = new DataInputStream(serial.getInputStream());
                out = new DataOutputStream(serial.getOutputStream());
                out.flush();
                if (in.markSupported()) {
                    in.reset();
                }
                serial.addEventListener(new SerialPortEventListener() {
                    @Override
                    public void serialEvent(SerialPortEvent event) {
                        try {
                            byte[] data = new byte[17];
                            int bytes = 0;
                            int result = in.read();
                            while (result != -1) {
                                // logger.debug("Received data: {}", result);
                                data[bytes] = (byte) result;
                                result = in.read();
                                bytes++;
                            }
                            logger.debug("Received data: {}", DatatypeConverter.printHexBinary(data));

                            short count = 0;
                            byte sum = 0;
                            for (int i = 0; i <= 14; i++) {
                                count += (data[i] & 0xFF);
                            }
                            sum = (byte) (count & 0xFF);
                            logger.debug("sum is {} CRC must be {} receive {}", count, sum, data[15]);

                            if (((data[0] & 0xFF) == 0b10101101) && ((data[16] & 0xFF) == 0b10101110)) {
                                // logger.debug("sum is {} CRC must be {} receive {}", count, sum, data[15]);
                                if (sum == data[15]) {
                                    // logger.debug("CRC is OK");
                                    logger.debug("Updating values...");
                                    // logger.debug("Command: {}", data[5]);
                                    NooliteMTRF64BridgeHandler.updateValues(data);
                                } else {
                                    logger.debug("CRC is WRONG");
                                }
                            } else {
                                logger.debug("Start/stop bits is wrong");
                            }
                        } catch (IOException ex) {
                            logger.debug("Error reading from serial port!", ex);
                        }
                    }
                });
                serial.notifyOnDataAvailable(true);
            }
        }
    }

    @SuppressWarnings("null")
    public void disconnect() {
        if (serial != null) {
            serial.notifyOnDataAvailable(false);
            serial.removeEventListener();
            serial.close();
        }
        in = null;
        out = null;
    }

    @SuppressWarnings("null")
    public void sendData(byte[] data) throws IOException {
        logger.debug("Sending {} bytes: {}", data.length, DatatypeConverter.printHexBinary(data));
        try {
            if (out != null) {
                out.write(data);
                out.flush();
            }
        } catch (IOException e) {
            logger.debug("sendMessage(): Writing error: {}", e.getMessage(), e);
        }
    }
}
