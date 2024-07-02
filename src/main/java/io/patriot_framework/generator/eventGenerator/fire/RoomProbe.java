/*
 * Copyright 2024 Patriot project
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package io.patriot_framework.generator.eventGenerator.fire;

import io.patriot_framework.generator.Data;
import io.patriot_framework.generator.dataFeed.DataFeed;
import io.patriot_framework.generator.eventGenerator.SimulationBase;


public class RoomProbe extends SimulationBase implements DataFeed {
    private String label;
    private Integer lastValue = -2;
    private Integer temperature = -2;

    public Data getNextValue(Object... params) {
        lastValue = temperature;
        return new Data(Integer.class, temperature);
    }


    public Data getPreviousValue() {
        return new Data(Integer.class, lastValue);
    }


    public void setLabel(String label) {
        this.label = label;
    }


    public String getLabel() {
        return this.label;
    }

    @Override
    public void init() {
        subscribe("temperature");
    }

    @Override
    public void awake() {

    }

    @Override
    public void receive(Data message, String topic) {
        temperature = message.get(Integer.class);
    }
}
