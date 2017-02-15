/**
 *
 *	IoT HT 2016, project "Ad Hoc-nätverk för informationsdistribution"
 *	Johan Bäckström, 831121-0119
 *	Anton Fluch, 910630-3358
 *
 *	SensorData.java
 *
 */
import java.sql.*;

public abstract class SensorData
{
    private String timestamp = "No timestamp";

    public SensorData(){
    }

    public SensorData(String timestamp)
    {
        //tiden ska sättas när denna skapas?
        this.timestamp = timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getTimestamp()
    {
        return this.timestamp;
    }
    
    @Override
    public abstract String toString();
}
