package com.example.timetrack.ui.pages.dashboard.charts;

import com.example.timetrack.entity.Track;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.Configuration;
import com.vaadin.flow.component.charts.model.DataSeries;
import com.vaadin.flow.component.charts.model.DataSeriesItem;
import com.vaadin.flow.component.charts.model.PlotOptionsColumn;
import com.vaadin.flow.component.html.Div;

import java.util.ArrayList;
import java.util.List;

public class MonthChart extends Div {


    public MonthChart(List<Track> tracks) {

        DataSeries dataSeries = new DataSeries();
        PlotOptionsColumn splinePlotOptions = new PlotOptionsColumn();
        dataSeries.setPlotOptions(splinePlotOptions);
        dataSeries.setName("Количество часов");

        List<DataSeriesItem> dataSeriesItems = new ArrayList<>();

        List<TrackWrapper> resultTracks = new ArrayList<>();
        tracks.forEach(track -> {
            if (resultTracks.stream().noneMatch(res -> res.getDate().equals(track.getDate()))) {
                resultTracks.add(new TrackWrapper(track.getDate(), track.getTime()));
            } else {
                resultTracks.stream().filter(res -> res.getDate().equals(track.getDate())).findFirst().ifPresent(res-> {
                    res.setTime(res.getTime().add(track.getTime()));
                });
            }
        });
        for (TrackWrapper track : resultTracks) {
            dataSeriesItems.add(new DataSeriesItem(track.getDate().getDayOfMonth(), track.getTime()));
        }
        dataSeries.setData(dataSeriesItems);
        Chart chart = new Chart();

        Configuration configuration = chart.getConfiguration();
        configuration.addSeries(dataSeries);

        add(chart);
    }
}
