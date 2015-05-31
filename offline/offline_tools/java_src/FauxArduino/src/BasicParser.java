import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;


public class BasicParser implements FauxParser {

	BlockingQueue<String> q;
	boolean parserStarted = false;
	long offset = 0;	
	
	public BasicParser() {
		q = new ArrayBlockingQueue<String>(50);
	}
	
	@Override
	public void parse() {
		return;
		// TODO Auto-generated method stub
	}

	@Override
	public void qAdd(String input) {
		while (true) {
			if (q.offer(input)) {
				break;
			}
		}
		if (!parserStarted) {
			parse();
			parserStarted = true;
		}
		return;
	}
	
	@Override
	public void setOffset(long offset) {
		this.offset = offset;		
	}
	
	protected void sleep(Date target) {
		try {
			long sleepTime = target.getTime() + offset - new Date().getTime();
			if (sleepTime > 0) {
				Thread.sleep(sleepTime);
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static Date makeDate(String string) {
		try {
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
			Date date = format.parse(string);
			return date;
		} catch (ParseException e) {
			System.out.println("Unable to parse date");
			return null;
		}
	}

	//Adjusts the start time to current start, which is start of program
	//Answer in milliseconds
	public static long calculateOffset(Date current, Date start) {
		return current.getTime() - start.getTime();
	}
}