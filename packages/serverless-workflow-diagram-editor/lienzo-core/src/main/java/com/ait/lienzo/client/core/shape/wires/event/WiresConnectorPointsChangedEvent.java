/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.ait.lienzo.client.core.shape.wires.event;

import com.ait.lienzo.client.core.shape.wires.WiresConnector;
import elemental2.dom.Event;
import elemental2.dom.HTMLElement;

public class WiresConnectorPointsChangedEvent extends AbstractWiresEvent<WiresConnectorPointsChangedHandler, WiresConnector> {

    public static final Type<WiresConnectorPointsChangedHandler> TYPE = new Type<>();

    public WiresConnectorPointsChangedEvent(final HTMLElement relativeElement) {
        super(relativeElement);
    }

    public void kill() {
        setSource(null);
        setDead(true);
    }

    public void revive() {
        setSource(null);
        setDead(false);
    }

    public void override(WiresConnector shape) {
        setSource(shape);
    }

    @Override
    public Type<WiresConnectorPointsChangedHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    public void dispatch(WiresConnectorPointsChangedHandler handler) {
        handler.onPointsChanged(this);
    }

    public Event getNativeEvent() {
        throw new UnsupportedOperationException();
    }
}
