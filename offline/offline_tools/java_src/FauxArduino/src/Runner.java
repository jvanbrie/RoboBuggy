import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.PrintStream;
import java.util.Date;


public class Runner {
	public static void main(String[] args) {
		IMUParser imp = new IMUParser();
		SteeringParser sp = new SteeringParser();
		EncoderParser ep = new EncoderParser();
		GPSParser gpsp = new GPSParser();
		LoggingStartParser lsp = new LoggingStartParser();
		
		try {
			System.setOut(new PrintStream(new FileOutputStream("test10.txt")));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		System.out.println("Starting to read from file!");

		//TODO: Make this take in a file argument instead of being hardcoded
		try(BufferedReader br = new BufferedReader(new FileReader("logs\\2015-03-21-07-12-46\\sensors.txt"))) {
			String line = br.readLine();
			
			long offset = getOffset(line);
			BasicParser.setOffset(offset);
			
			while (true) {
				if (line == null) { 
					System.out.println("Reached end of file!");
					return;
				}
				
				int endIndex = line.indexOf(',');
				if (endIndex == -1) {
					throw new Exception("Malformed data!");
				} else {
					String sensor = line.substring(0, endIndex);
					switch (sensor.toLowerCase()) {
					case "sensors/imu":
						imp.qAdd(line);
						break;
					case "sensors/steering":
						sp.qAdd(line);
						break;
					case "sensors/encoder":
						ep.qAdd(line);
						break;
					case "sensors/gps":
						gpsp.qAdd(line);
						break;
					case "sensors/logging_button":
						lsp.qAdd(line);
						break;
					default:
						System.out.println("Unknown sensor type: " + sensor.toLowerCase());
					}
					line = br.readLine();
				}
			}
		} catch (Exception e) {
			System.out.println("Looks like there was a problem somewhere.");
		}
	}
	
	private static long getOffset(String line) {
		String[] parsed = line.split(",");
		
		assert parsed.length >= 2;
		
		Date date = BasicParser.makeDate(parsed[1]);
		return BasicParser.calculateOffset(new Date(), date);		
	}
}