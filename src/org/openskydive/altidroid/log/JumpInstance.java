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
    public static final String ID = "_id";
    public static final String NUMBER = "number";
    public static final String DATE = "date";
    public static final String EXIT_ALTITUDE = "exit_altitude";
    public static final String DEPLOY_ALTITUDE = "deploy_altitude";
    public static final String FREEFALL_TIME = "freefall_time";
    public static final String COMMENTS = "comments";
    public static final String DROPZONE = "dropzone";
    public static final String AIRCRAFT = "aircraft";
    public static final String JUMPTYPE = "jumptype";
    public static final String EQUIPMENT = "equipment";

    private int id;
    private int number;
    private int date;
    private int exitAltitude;
    private int deployAltitude;
    private int freefallTime;
    private String comments;
    private String dropzone;
    private String aircraft;
    private String jumpType;
    private String equipment;

    public JumpInstance(int id, int number, int date, int exitAltitude, int deployAltitude, int freefallTime, String comments, String dropzone, String aircraft, String jumpType, String equipment) {
        this.id = id;
        this.number = number;
        this.date = date;
        this.exitAltitude = exitAltitude;
        this.deployAltitude = deployAltitude;
        this.freefallTime = freefallTime;
        this.comments = comments;
        this.dropzone = dropzone;
        this.aircraft = aircraft;
        this.jumpType = jumpType;
        this.equipment = equipment;
    }

    public static String xmlTag(String tagName, String tagData, int indentLevel) {
        String xml = "";

        for (int i = 0; i < indentLevel; i++) {
            xml += "\t";
        }
        xml += String.format("<%s>%s</%s>\n", tagName, tagData, tagName);

        return xml;
    }

    public static String xmlTag(String tagName, int tagData, int indentLevel) {
        return xmlTag(tagName, "" + tagData, indentLevel);
    }

    public String toXML() {
        String xml = "<jump>\n";

        xml += xmlTag(ID, id, 1);
        xml += xmlTag(NUMBER, number, 1);
        xml += xmlTag(DATE, date, 1);
        xml += xmlTag(EXIT_ALTITUDE, exitAltitude, 1);
        xml += xmlTag(DEPLOY_ALTITUDE, deployAltitude, 1);
        xml += xmlTag(FREEFALL_TIME, freefallTime, 1);
        xml += xmlTag(COMMENTS, comments, 1);
        xml += xmlTag(DROPZONE, dropzone, 1);
        xml += xmlTag(AIRCRAFT, aircraft, 1);
        xml += xmlTag(JUMPTYPE, jumpType, 1);
        xml += xmlTag(EQUIPMENT, equipment, 1);

        xml += "</jump>\n";

        return xml;
    }
}
