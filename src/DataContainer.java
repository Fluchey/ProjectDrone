/**
*
*	IoT HT 2016, project "Ad Hoc-nätverk för informationsdistribution"
*	Johan Bäckström, 831121-0119
*	Anton Fluch, 910630-3358
*
*	DataContainer.java
*
*/

import java.io.*;
import java.sql.Timestamp;
import java.util.*;
import java.text.*;

public class DataContainer
{
	private Device device;
	private Temperature temperature;
	private String filePath="/home/pi/ProjectDroneData/data.txt";
	private TreeMap<String, String> remoteData = new TreeMap<>();

	public DataContainer(Device device){
        this.temperature = new Temperature();
		this.device=device;
	}

	public Temperature getTemperature() {
		return temperature;
	}

	public void setTemperature(Temperature temperature) {
		this.temperature = temperature;
	}

	public void updateText() 
	{
		/* System.out.println("DEBUG temp: "+temperature.getTemperature()+"Timestamp :"+temperature.getTimestamp()); */
		try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath), "utf-8"))) 
    	{
			writer.write("Local: ;;" + device.getIpAddress() + " Data:: " + temperature.getTemperature() +  " Time: " + temperature.getTimestamp() + "\n");

			/* Om remoteData är tom, skriv inte ut den till fil */
    		if(!remoteData.isEmpty())
    			/* För alla nycklar i remoteData, tvätta ip och data till en sträng och skriv till fil */
    		{
    			remoteData.forEach((ip, data) -> {
    				String cleanData = ";;"+ip + " ";
					cleanData += data.substring(data.indexOf("Data::"));
					try {
						writer.write(cleanData+"\n");
					}catch (Exception e) {}
				});
    		}
    		writer.close();
    	}
    	catch(Exception e){}
	}

	public void updateOther(ArrayList<String> otherDeviceText){
		/* Iterera över hela listan av andra enheter */
		for(String s: otherDeviceText){
			/* Tvätta ut ip från strängen vi jobbar med */
			String ip = s.substring(s.indexOf(";;") + 2, s.indexOf(";;") + 13);
			
			/* Kolla ifall ip är samma som den lokala enheten, i så fall, hoppa till nästa iteration */
			if(ip.equals(device.getIpAddress())){
				continue;
			}
			
			String data = s.substring(s.indexOf("Data::"));

			/* Ifall det inte finns någon tidigare enhet tillagd, tvätta sträng och lägg till, hoppa till nästa iteration */
			if(!remoteData.containsKey(ip)){
				remoteData.put(ip, data);
				continue;
			}

			//Om vi når hit i koden, så finns ndanstående finns redan i den lokala listan
			
			if(s.contains("No timestamp") && remoteData.get(ip).contains("No timestamp")){
				continue;
			}

			if(s.contains("No timestamp") && !remoteData.get(ip).contains("No timestamp")){
				continue;
			}
			
			//Hit kommer vi endast om textraden vi kollar HAR en timestamp
			if(remoteData.get(ip).contains("No timestamp")){
				remoteData.put(ip, data);
				continue;
			}
			
			//Hit kommer vi endast om koden vi kollar OCH vår egen lista har samma ip, med varsin timestamp
			
			//Vi ska byta ut värdet i den lokala listan ENDAST om datumet för S är nyare
			String thisRowDate = s.substring(s.indexOf("Time:") + 5);
			String hashMapDate = remoteData.get(ip).substring(remoteData.get(ip).indexOf("Time:") + 5);
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			Date d1 = null;
			Date d2 = null;		
			try
			{
				d1 = sdf.parse(thisRowDate);
				d2 = sdf.parse(hashMapDate);
			} catch (Exception e){}
			
			
			if(d1.after(d2))
			{
				remoteData.put(ip, data);
			}
		}
		updateText();
	}

	public void printDevices(){
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		System.out.println("\n\n\t******************* DATA IS HERE **************************** " + timestamp);
		System.out.println("\tLocal: ;;" + device.getIpAddress() + " Data:: " + temperature.getTemperature() +  " Time: " + temperature.getTimestamp());
		remoteData.forEach((ip, data) -> {
			System.out.println("\tIp: " + ip + data);
		});
		System.out.println("\t************************************************************\n\n");
	}

}
