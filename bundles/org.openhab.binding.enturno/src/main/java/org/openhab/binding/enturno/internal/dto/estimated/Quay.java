/**
 * Copyright (c) 2010-2023 Contributors to the openHAB project
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
<<<<<<<< HEAD:bundles/org.openhab.binding.paradoxalarm/src/main/java/org/openhab/binding/paradoxalarm/internal/communication/messages/Command.java
package org.openhab.binding.paradoxalarm.internal.communication.messages;

import org.openhab.binding.paradoxalarm.internal.communication.IRequest;

/**
 * More generic interface for creating command requests
 *
 * @author Konstantin Polihronov - Initial contribution
 */
public interface Command {
    IRequest getRequest(int id);
========
package org.openhab.binding.enturno.internal.dto.estimated;

/**
 * Generated Plain Old Java Objects class for {@link Quay} from JSON.
 *
 * @author Michal Kloc - Initial contribution
 */
public class Quay {
    public String publicCode;

    public String id;
>>>>>>>> 6540d0dda9e7a54deb286556cdee01dd007af32e:bundles/org.openhab.binding.enturno/src/main/java/org/openhab/binding/enturno/internal/dto/estimated/Quay.java
}
