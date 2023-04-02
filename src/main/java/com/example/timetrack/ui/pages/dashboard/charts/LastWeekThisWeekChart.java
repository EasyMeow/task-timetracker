package com.example.timetrack.ui.pages.dashboard.charts;

import com.example.timetrack.entity.Track;
import com.storedobject.chart.*;
import com.vaadin.flow.component.html.Div;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class LastWeekThisWeekChart extends Div {

    public LastWeekThisWeekChart(List<Track> lastWeek, List<Track> thisWeek) {

        SOChart chart = new SOChart();
        chart.setWidthFull();
        chart.setHeight("500px");

        List<TrackWrapper> resultLastWeekTracks = new ArrayList<>();
        lastWeek.forEach(track -> {
            if (resultLastWeekTracks.stream().noneMatch(res -> res.getDate().equals(track.getDate()))) {
                resultLastWeekTracks.add(new TrackWrapper(track.getDate(), track.getTime()));
            } else {
                resultLastWeekTracks.stream().filter(res -> res.getDate().equals(track.getDate())).findFirst().ifPresent(res -> {
                    res.setTime(res.getTime().add(track.getTime()));
                });
            }
        });


        List<TrackWrapper> resultThisWeekTracks = new ArrayList<>();
        thisWeek.forEach(track -> {
            if (resultThisWeekTracks.stream().noneMatch(res -> res.getDate().equals(track.getDate()))) {
                resultThisWeekTracks.add(new TrackWrapper(track.getDate(), track.getTime()));
            } else {
                resultThisWeekTracks.stream().filter(res -> res.getDate().equals(track.getDate())).findFirst().ifPresent(res -> {
                    res.setTime(res.getTime().add(track.getTime()));
                });
            }
        });

        CategoryData xValues = new CategoryData();
        Data yValues1 = new Data(), yValues2 = new Data();
        Iterator<TrackWrapper> lastIterator = resultLastWeekTracks.iterator();
        Iterator<TrackWrapper> thisIterator = resultThisWeekTracks.iterator();
        for (int x = 1; x < 7; x++) {
            xValues.add(getDay(x));
            yValues1.add(lastIterator.hasNext() ? lastIterator.next().getTime() : 0);
            yValues2.add(thisIterator.hasNext()  ? thisIterator.next().getTime() : 0);
        }
        xValues.setName("День недели");
        yValues1.setName("Количество часов за прошлую неделю");
        yValues2.setName("Количество часов за эту неделю");

        BarChart barChart1 = new BarChart(xValues, yValues1);
        barChart1.setName("Прошлая неделя");
        barChart1.setStackName(
                "BC");
        BarChart barChart2 = new BarChart(xValues, yValues2);
        barChart2.setName("Эта неделя");
        barChart2.setStackName(
                "BC");

        LineChart lineChart = new LineChart(xValues, yValues1);
        lineChart.setName("Связующий график");

        YAxis yAxis = new YAxis(yValues1);
        XAxis xAxis = new XAxis(xValues);
        yAxis.setName("Количество часов");
        xAxis.setMinAsMinData();
        xAxis.setName("День недели");

        RectangularCoordinate rc = new RectangularCoordinate(xAxis, yAxis);
        barChart1.plotOn(rc);
        barChart2.plotOn(rc);
        lineChart.plotOn(rc);

        Title title = new Title("Эффективность по сравнению с прошлой неделей");
        title.getPosition(true).setLeft(Size.percentage(10));


        chart.disableDefaultLegend();
        Legend legend = new Legend();
        legend.getPosition(true).setRight(Size.percentage(10));

        barChart1
                .getTooltip(true)
                .append("День недели: ")
                .append(xValues)
                .newline()
                .append("Количество часов: ")
                .append(yValues1);

        barChart2
                .getTooltip(true)
                .append("День недели: ")
                .append(xValues)
                .newline()
                .append("Количество часов: ")
                .append(yValues2);

        lineChart
                .getTooltip(true)
                .append("День недели: ")
                .append(xValues)
                .newline()
                .append("Количество часов: ")
                .append(yValues2);

        chart.add(rc, title, legend);

        add(chart);
    }

    private String getDay(int num) {
        if (num == 1) {
            return "Пн";
        }
        if (num == 2) {
            return "Вт";
        }
        if (num == 3) {
            return "Ср";
        }
        if (num == 4) {
            return "Чт";
        }
        if (num == 5) {
            return "Пт";
        }
        if (num == 6) {
            return "Сб";
        } else {
            return "Вс";
        }
    }
}
