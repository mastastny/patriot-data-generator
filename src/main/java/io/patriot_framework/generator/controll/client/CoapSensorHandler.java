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

package io.patriot_framework.generator.controll.client;

import io.patriot_framework.generator.dataFeed.DataFeed;

import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;

public class CoapSensorHandler extends CoapDeviceHandler {
    public CoapSensorHandler(CoapControlClient ccc, Set<String> deviceEndpoints, String deviceLabel) {
        super(ccc, deviceEndpoints, deviceLabel);
    }

    public void addDataFeed(DataFeed dataFeed) {

    }

    public void removeDatafeed(DataFeed dataFeed, String label) {
        // TODO: 7/2/24
    }

    public CoapDataFeedHandler getDataFeedHandler(String label) {
        Pattern pattern = Pattern.compile(String.format("/%s$", "dataFeed"));
            Optional<String> dataFeedEndpoint = deviceEndpoints.stream()
                    .filter(pattern.asPredicate())
                    .findFirst();

            if(dataFeedEndpoint.isEmpty()) {
                return null;
            }

        return new CoapDataFeedHandler(ccc, dataFeedEndpoint.get(), label );
    }

}
