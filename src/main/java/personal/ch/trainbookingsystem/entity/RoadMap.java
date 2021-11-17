package personal.ch.trainbookingsystem.entity;

public class RoadMap {
    private Long id;
    private String startStation;
    private String endStation;
    private String stationEquals;

    public RoadMap() {
    }

    public RoadMap(Long id, String startStation, String endStation) {
        this.id = id;
        this.startStation = startStation;
        this.endStation = endStation;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStartStation() {
        return startStation;
    }

    public void setStartStation(String startStation) {
        this.startStation = startStation;
    }

    public String getEndStation() {
        return endStation;
    }

    public void setEndStation(String endStation) {
        this.endStation = endStation;
    }

    public String getStationEquals() {
        return stationEquals;
    }

    public void setStationEquals(String stationEquals) {
        this.stationEquals = stationEquals;
    }

    @Override
    public String toString() {
        return startStation + "---->" + endStation;
    }
}
