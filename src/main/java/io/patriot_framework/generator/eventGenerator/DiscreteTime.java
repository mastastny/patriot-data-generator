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

package io.patriot_framework.generator.eventGenerator;

import java.util.Objects;

public class DiscreteTime implements Time, Cloneable {

    public DiscreteTime() {
    }

    public DiscreteTime(Integer value) {
        time = value;
    }

    @Override
    public Integer getValue() {
        return time;
    }

    @Override
    public void setValue(int value) {
        this.time = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DiscreteTime that = (DiscreteTime) o;
        return Objects.equals(time, that.time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(time);
    }

    public static int compare(DiscreteTime o, DiscreteTime o2) {
        return Integer.compare(o.time, o2.time);
    }

    @Override
    public int compareTo(Time other) {
        DiscreteTime otherTime = new DiscreteTime();
        otherTime.setValue(other.getValue());
        return compare(this, otherTime);
    }

    @Override
    public Object clone()
    {
        try {
            return super.clone();
        } catch (CloneNotSupportedException ex) {
            System.out.println(ex);
        }
        return null;
    }

    public int time;


}