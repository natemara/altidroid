// Copyright 2014
//
// This file is part of Altidroid.
//
// Altidroid is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
//
// Altidroid is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with Altidroid.  If not, see <http://www.gnu.org/licenses/>.

package org.openskydive.altidroid.log;

public class JumpInstance {
    /**
     * Ground level in millimeters. Valid in all state types except UNKNOWN.
     */
    private int mGroundLevel;

    /**
     * Take off time stamp. Only valid in CLIMP, FREEFALL and CANOPY states.
     */
    private long mTakeOffTime;

    /**
     * Exit altitude. Only valid in FREEFALL and CANOPY states.
     */
    private int mExitAltitude;

    /**
     * Exit time. Only valid in FREEFALL and CANOPY states.
     */
    private long mExitTime;

    /**
     * Deploy time.
     */
    private long mDeployTime;

    /**
     * Canopy deploy altitude. Valid only in the CANOPY state.
     */
    private int mDeployAltitude;

    public JumpInstance(int groundLevel, int takeOffTime, int exitAltitude, long exitTime, long deployTime, int deployAltitude) {
        mGroundLevel = groundLevel;
        mTakeOffTime = takeOffTime;
        mExitAltitude = exitAltitude;
        mExitTime = exitTime;
        mDeployTime = deployTime;
        mDeployAltitude = deployAltitude;
    }

    public String toXML() {
        String xml = "<jump>\n";
        xml += String.format("\t<groundLevel>%d</groundLevel>\n", mGroundLevel);
        xml += String.format("\t<takeOffTime>%d</takeOffTime>\n", mTakeOffTime);
        xml += String.format("\t<exitAltitude>%d</exitAltitude>\n", mExitAltitude);
        xml += String.format("\t<exitTime>%d</exitTime>\n", mExitTime);
        xml += String.format("\t<deployTime>%d</deployTime>\n", mDeployTime);
        xml += String.format("\t<deployAltitude>%d</deployAltitude>\n", mDeployAltitude);

        return xml;
    }
}
