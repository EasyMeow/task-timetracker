package com.example.timetrack.ui.pages.dashboard;

import com.example.timetrack.entity.Track;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.*;
import com.vaadin.flow.component.html.Div;

import java.util.ArrayList;
import java.util.List;

public class LastWeekThisWeekChart extends Div {

    public LastWeekThisWeekChart(List<Track> lastWeek, List<Track> thisWeek) {

        DataSeries dataSeries = new DataSeries();
        PlotOptionsSpline splinePlotOptions = new PlotOptionsSpline();
        dataSeries.setPlotOptions(splinePlotOptions);
        dataSeries.setName("Предыдущая неделя");

        List<DataSeriesItem> dataSeriesItems = new ArrayList<>();
//        List<Track> removedTracks = new ArrayList<>();
//        List<Track> mergedTracks = new ArrayList<>();
//        lastWeek.forEach(track1 -> {
//            lastWeek.forEach(track2 -> {
//                if (track1.getDate().equals(track2.getDate())) {
//                    removedTracks.add(track1);
//                    removedTracks.add(track2);
//                    track1.setTime(track1.getTime().add(track2.getTime()));
//                    mergedTracks.add(track1);
//                }
//            });
//        });
//        lastWeek.removeAll(removedTracks);
//        lastWeek.addAll(mergedTracks);
        int num = 1;
        for (Track track : lastWeek.subList(0,7)) {
            dataSeriesItems.add(new DataSeriesItem(num++, track.getTime()));
        }
        dataSeries.setData(dataSeriesItems);

        DataSeries dataSeriesLast = new DataSeries();
        dataSeriesLast.setPlotOptions(new PlotOptionsColumn());
        dataSeriesLast.setName("Эта неделя");

        List<DataSeriesItem> lastSeriesItems = new ArrayList<>();
//        removedTracks.clear();
//        mergedTracks.clear();
//        thisWeek.forEach(track1 -> {
//            thisWeek.forEach(track2 -> {
//                if (track1.getDate().equals(track2.getDate())) {
//                    removedTracks.add(track1);
//                    removedTracks.add(track2);
//                    track1.setTime(track1.getTime().add(track2.getTime()));
//                    mergedTracks.add(track1);
//                }
//            });
//        });
//        thisWeek.removeAll(removedTracks);
//        thisWeek.addAll(mergedTracks);
        num = 1;
        for (Track track : thisWeek.subList(0,7)) {
            lastSeriesItems.add(new DataSeriesItem(num++, track.getTime()));
        }
        dataSeriesLast.setData(lastSeriesItems);

        Chart chart = new Chart();

        Configuration configuration = chart.getConfiguration();
        configuration.addSeries(dataSeries);
        configuration.addSeries(dataSeriesLast);

        add(chart);
    }
}
