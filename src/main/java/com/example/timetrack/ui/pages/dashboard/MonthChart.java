package com.example.timetrack.ui.pages.dashboard;

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
        List<Track> removedTracks = new ArrayList<>();
        List<Track> mergedTracks = new ArrayList<>();
        tracks.forEach(track1 -> {
            tracks.forEach(track2 -> {
                if(track1.getDate().equals(track2.getDate())){
                    removedTracks.add(track1);
                    removedTracks.add(track2);
                    track1.setTime(track1.getTime().add(track2.getTime()));
                    mergedTracks.add(track1);
                }
            });
        });
        tracks.removeAll(removedTracks);
        tracks.addAll(mergedTracks);
        for (Track track : tracks) {
            dataSeriesItems.add(new DataSeriesItem(track.getDate().getDayOfMonth(), track.getTime()));
        }
        dataSeries.setData(dataSeriesItems);
        Chart chart = new Chart();

        Configuration configuration = chart.getConfiguration();
        configuration.addSeries(dataSeries);

        add(chart);
    }
}
