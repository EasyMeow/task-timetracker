package com.example.timetrack.ui.pages.dashboard.charts;

import com.example.timetrack.entity.Track;
import com.storedobject.chart.*;
import com.vaadin.flow.component.html.Div;

import java.util.ArrayList;
import java.util.List;

public class MonthChart extends Div {


    public MonthChart(List<Track> tracks) {
        SOChart chart = new SOChart();
        chart.setWidthFull();
        chart.setHeight("500px");

        DateData xValues = new DateData();
        Data yValues = new Data();
        xValues.setName("Дата");
        yValues.setName("Количество часов");

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
            xValues.add(track.getDate());
            yValues.add(track.getTime());
        }

        BarChart basrMonthChart = new BarChart(xValues, yValues);
        basrMonthChart.setName("Часы");
        basrMonthChart.setStackName("BC");

        YAxis yAxis = new YAxis(yValues);
        yAxis.setName("Количество часов");
        XAxis xAxis = new XAxis(xValues);
        xAxis.setMinAsMinData();
        xAxis.setName("День месяца");

        RectangularCoordinate rc = new RectangularCoordinate(xAxis, yAxis);
        basrMonthChart.plotOn(rc);

        Title title = new Title("Количество часов за последний месяц");
        title.getPosition(true).setLeft(Size.percentage(10));

        chart.disableDefaultLegend();
        Legend legend = new Legend();
        legend.getPosition(true).setRight(Size.percentage(10));


        basrMonthChart
                .getTooltip(true)
                .append("Дата: ")
                .append(xValues)
                .newline()
                .append("Количество часов: ")
                .append(yValues);

        chart.add(rc, title, legend);

        add(chart);
    }
}
