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

package io.patriot_framework.generator.eventGenerator.simulationAdapter;

import io.patriot_framework.generator.Data;

import java.util.function.BiConsumer;


// kdyz bude zavolane update data tak zodpovida za to, ze se dostanou do clienta, jak casto se budou data obnovovat je na adapteru
public interface SimulationAdapterServer {
    void updateData(Data data);
    void setSimulationAdapter(SimulationAdapter simulationAdapter);
    SimulationAdapterClient getClient();
}
