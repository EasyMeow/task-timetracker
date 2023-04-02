package com.example.timetrack.ui.pages.dashboard.charts;

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
        List<TrackWrapper> resultLastWeekTracks = new ArrayList<>();
        lastWeek.forEach(track -> {
            if (resultLastWeekTracks.stream().noneMatch(res -> res.getDate().equals(track.getDate()))) {
                resultLastWeekTracks.add(new TrackWrapper(track.getDate(), track.getTime()));
            } else {
                resultLastWeekTracks.stream().filter(res -> res.getDate().equals(track.getDate())).findFirst().ifPresent(res-> {
                    res.setTime(res.getTime().add(track.getTime()));
                });
            }
        });
        int num = 1;
        for (TrackWrapper track : resultLastWeekTracks) {
            dataSeriesItems.add(new DataSeriesItem(num++, track.getTime()));
        }
        dataSeries.setData(dataSeriesItems);

        DataSeries dataSeriesLast = new DataSeries();
        dataSeriesLast.setPlotOptions(new PlotOptionsColumn());
        dataSeriesLast.setName("Эта неделя");

        List<DataSeriesItem> lastSeriesItems = new ArrayList<>();
        List<TrackWrapper> resultThisWeekTracks = new ArrayList<>();
        thisWeek.forEach(track -> {
            if (resultThisWeekTracks.stream().noneMatch(res -> res.getDate().equals(track.getDate()))) {
                resultThisWeekTracks.add(new TrackWrapper(track.getDate(), track.getTime()));
            } else {
                resultThisWeekTracks.stream().filter(res -> res.getDate().equals(track.getDate())).findFirst().ifPresent(res-> {
                    res.setTime(res.getTime().add(track.getTime()));
                });
            }
        });
        num = 1;
        for (TrackWrapper track : resultThisWeekTracks) {
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
