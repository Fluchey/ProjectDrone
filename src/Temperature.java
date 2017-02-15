/**
*
*	IoT HT 2016, project "Ad Hoc-nätverk för informationsdistribution"
*	Johan Bäckström, 831121-0119
*	Anton Fluch, 910630-3358
*
*	Temperature.java
*
*/

public class Temperature extends SensorData
{
	private double temperature = -273.15; /* This is default value */

	public Temperature(){

	}

	public Temperature(String timestamp, double temp)
	{
		super(timestamp);
		this.temperature=temp;
	}
	
	public double getTemperature()
	{
		return this.temperature;
	}

	public void setTemperature(double temperature) {
		this.temperature = temperature;
	}

	@Override
	public String toString()
	{
		return "Time: "+super.getTimestamp().toString()+" Temperature: "+Double.toString(temperature);
	}
}
