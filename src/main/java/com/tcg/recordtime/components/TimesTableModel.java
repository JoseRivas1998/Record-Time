package com.tcg.recordtime.components;

import com.tcg.recordtime.utils.Profile;
import com.tcg.recordtime.utils.StopWatch;

import javax.swing.table.AbstractTableModel;
import java.util.Calendar;

/**
 * Created by JoseR on 11/2/2016.
 */
public class TimesTableModel extends AbstractTableModel {

    private Profile profile;
    private String[] columnNames = {"Date", "Time"};

    public TimesTableModel(Profile profile) {
        super();
        this.profile = profile;
    }

    @Override
    public int getRowCount() {
        return profile.getTimes().size();
    }

    @Override
    public int getColumnCount() {
        return 2;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0:
                Calendar calendar = profile.getDates().get(rowIndex);
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                String ampm = "AM";
                if(hour > 12) {
                    hour -= 12;
                    ampm = "PM";
                }
                return String.format("%d/%d/%d %d:%d %s", calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.YEAR), hour, calendar.get(Calendar.MINUTE), ampm);
            case 1:
                return StopWatch.formatTime(profile.getTimes().get(rowIndex));
            default:
                return null;
        }
    }
}
