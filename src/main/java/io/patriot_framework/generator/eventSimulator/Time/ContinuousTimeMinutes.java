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

package io.patriot_framework.generator.eventSimulator.Time;

public class ContinuousTimeMinutes extends AbstractContinuousTime {
    @Override
    protected long millisecondsInUnit() {
        return 60 * 1000;
    }

    @Override
    protected String unitName() {
        return "min";
    }

    public ContinuousTimeMinutes() {
        super();
    }

    public ContinuousTimeMinutes(double timeInUnits) {
        super(timeInUnits);
    }

    public ContinuousTimeMinutes(Time time) {
        super(time);
    }
}