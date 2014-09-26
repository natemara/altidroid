// Copyright 2012-2013 Andrey Ulanov
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
package org.openskydive.altidroid.ui;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.openskydive.altidroid.R;
import org.openskydive.altidroid.log.DatabaseLogProvider;
import org.openskydive.altidroid.log.LogEntry;
import org.openskydive.altidroid.log.LogProtos;
import org.openskydive.altidroid.log.LogProtos.Entry.Builder;
import org.openskydive.altidroid.util.Units;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

public class JumpInfoEdit extends Activity implements OnClickListener {

    public static final String ENTRY_INTENT_EXTRA = "intent.extra.log_entry";

    private LogEntry mLogEntry;
    private TextView mNumber;
    private TextView mDate;
    private SimpleAutoCompleteEditView mJumpType;
    private SimpleAutoCompleteEditView mDropZone;
    private SimpleAutoCompleteEditView mEquipment;
    private SimpleAutoCompleteEditView mAircraft;
    private TextView mExitAt;
    private TextView mDeployAt;
    private TextView mDelay;
    private TextView mComments;

    private Units mUnits;
    private DateFormat mDateFormat;
    private Button mCancel;
    private Button mOk;

    private ArrayAdapter<String> getCompletions(String field, int arrayId) {
        // SELECT field FROM log WHERE length(field) > 0 GROUP BY field ORDER BY
        // field;
        Cursor cursor = getContentResolver().query(DatabaseLogProvider.COMPLETIONS_URI,
                new String[] { field }, "length(" + field + ") > 0", null,
                field);
        if (cursor == null) {
            return null;
        }
        final int colId = cursor.getColumnIndex(field);
        if (colId < 0) {
            return null;
        }
        Set<String> contentSet = new HashSet<String>();
        ArrayList<String> content = new ArrayList<String>(cursor.getCount());
        while (cursor.moveToNext()) {
            String s = cursor.getString(colId);
            if (!contentSet.contains(s)) {
                content.add(s);
                contentSet.add(s);
            }
        }
        cursor.close();

        if (arrayId > 0) {
            String[] array = getResources().getStringArray(arrayId);
            for (int i = 0; i < array.length; i++) {
                String s = array[i];
                if (!contentSet.contains(s)) {
                    content.add(s);
                    contentSet.add(s);
                }
            }
        }

        return new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, content);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.jump_info_edit_activity);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.jump_info_edit_action_bar);

        processIntent(getIntent());

        mUnits = Units.getInstance(this);
        mDateFormat = android.text.format.DateFormat.getMediumDateFormat(this);

        mNumber = (TextView) findViewById(R.id.number);
        mDate = (TextView) findViewById(R.id.date);
        mJumpType = (SimpleAutoCompleteEditView) findViewById(R.id.jump_type);
        mDropZone = (SimpleAutoCompleteEditView) findViewById(R.id.drop_zone);
        mEquipment = (SimpleAutoCompleteEditView) findViewById(R.id.equipment);
        mAircraft = (SimpleAutoCompleteEditView) findViewById(R.id.aircraft);
        mExitAt = (TextView) findViewById(R.id.exit_at);
        mDeployAt = (TextView) findViewById(R.id.deploy_at);
        mDelay = (TextView) findViewById(R.id.delay);
        mComments = (TextView) findViewById(R.id.comments);
        mCancel = (Button) findViewById(R.id.jump_revert);
        mOk = (Button) findViewById(R.id.jump_save);

        mJumpType.setAdapter(getCompletions(LogEntry.Columns.JUMPTYPE, R.array.jump_type_list));
        mDropZone.setAdapter(getCompletions(LogEntry.Columns.DROPZONE, -1));
        mEquipment.setAdapter(getCompletions(LogEntry.Columns.EQUIPMENT, -1));
        mAircraft.setAdapter(getCompletions(LogEntry.Columns.AIRCRAFT, -1));

        LogProtos.Entry proto = mLogEntry.getProto();
        mNumber.setText(Integer.toString(proto.getNumber()));
        mDate.setText(mDateFormat.format(new Date(proto.getTimestamp())));
        if (proto.hasJumpType())
            mJumpType.setText(proto.getJumpType());
        if (proto.hasDropzone())
            mDropZone.setText(proto.getDropzone());
        if (proto.hasEquipment())
            mEquipment.setText(proto.getEquipment());
        if (proto.hasAircraft())
            mAircraft.setText(proto.getAircraft());
        mExitAt.setText(Integer.toString(mUnits.toUserUnits(proto
                .getExitAltitude())));
        ((TextView) findViewById(R.id.exit_units)).setText(
                mUnits.getUnitsNameShort());
        mDeployAt.setText(Integer.toString(mUnits.toUserUnits(proto
                .getDeployAltitude())));
        ((TextView) findViewById(R.id.deploy_units)).setText(
                mUnits.getUnitsNameShort());
        mDelay.setText(Integer.toString(proto.getFreefallTime() / 1000));
        mComments.setText(proto.getComments());

        mCancel.setOnClickListener(this);
        mOk.setOnClickListener(this);
    }

    private void processIntent(Intent intent) {
        mLogEntry = intent.getParcelableExtra(ENTRY_INTENT_EXTRA);
    }

    @Override
    public void onClick(View v) {
        if (v == mOk) {
            save();
        }
        finish();
    }

    private void save() {
        Builder builder = mLogEntry.getProto().toBuilder();

        builder.setNumber(Integer.parseInt(mNumber.getText().toString()));
        // TODO: date
        builder.setJumpType(mJumpType.getText().toString());
        builder.setDropzone(mDropZone.getText().toString());
        builder.setEquipment(mEquipment.getText().toString());
        builder.setAircraft(mAircraft.getText().toString());
        builder.setExitAltitude(mUnits.fromUserUnits(Integer.parseInt(mExitAt
                .getText().toString())));
        builder.setDeployAltitude(mUnits.fromUserUnits(Integer
                .parseInt(mDeployAt.getText().toString())));
        builder.setFreefallTime(Integer.parseInt(mDelay.getText().toString()) * 1000);
        builder.setComments(mComments.getText().toString());

        getContentResolver().update(mLogEntry.getUri(),
                LogEntry.createContentValues(builder.build()), null, null);
    }
}
