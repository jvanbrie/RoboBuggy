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
		
		try {
			System.setOut(new PrintStream(new FileOutputStream("test2.txt")));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		System.out.println("Starting to read from file!");

		//TODO: Make this take in a file argument instead of being hardcoded
		try(BufferedReader br = new BufferedReader(new FileReader("src\\sensors.txt"))) {
			String line = br.readLine();
			
			long offset = getOffset(line);
			imp.setOffset(offset);
			
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
					default:
						System.out.println("Unknown sensor type");
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