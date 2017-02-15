/**
*
*	IoT HT 2016, project "Ad Hoc-nätverk för informationsdistribution"
*	Johan Bäckström, 831121-0119
*	Anton Fluch, 910630-3358
*
*	LocalSensorHandler.java
*
*/

import java.io.*;

public class LocalSensorHandler {
    private Device device;

    public LocalSensorHandler(Device device){
        this.device = device;
    }

    public void updateLocalSensors(){
        /* Här kan vi uppdatera flera data om det behövs */
        updateTemperature();
    }

    public void updateTemperature(){

        /* Command to be sent to local device */
        String command = "tdtool --list-sensors";
        try {
            Process p = Runtime.getRuntime().exec(command);
            p.waitFor();
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));

            String line = "";
            while ((line = br.readLine()) != null){
                String [] objectArr = line.split("\t");
                String temp = null;
                String timestamp = null;

                /* Om model=temperature och den ligger i våran mapOfSensors, så vet vi att det är rätt temperatursensor vi jobbar med */
                if(objectArr[2].equals("model=temperature") && device.getMapOfSensors().containsKey(objectArr[3].substring(3))){
                    /* Om devicenumret matchar mot lokala device ip, ta ut temperatur och tidsdata */
                    if(device.getMapOfSensors().get(objectArr[3].substring(3)).equals(device.getIpAddress())) {
                        
                    	temp = objectArr[4].substring(12);
                        timestamp = objectArr[5].substring(5);
                        device.getDataContainer().getTemperature().setTemperature(Double.parseDouble(temp));
                        device.getDataContainer().getTemperature().setTimestamp(timestamp);
                    }
                }
            }
        }catch(Exception e){
            System.out.println("Could not retrieve local temperature value \n");
        }
        //Här uppdaterar vi texten varje gång en ny temperatur hämtas, även om värde för temp inte finns
        device.getDataContainer().updateText();
    }
}
