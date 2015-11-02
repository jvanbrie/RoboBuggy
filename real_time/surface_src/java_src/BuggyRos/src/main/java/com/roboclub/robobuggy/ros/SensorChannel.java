package com.roboclub.robobuggy.ros;

public enum SensorChannel {
	GPS("gps"),
	IMU("imu"),
    RC("rc_angle"),
    STEERING("steering"),
    BRAKE("brake"),
	DRIVE_CTRL("drive_ctrl"),
	VISION("vision"),
	ENCODER("encoder"),
	AUTO("auto"),
	CPU_USSAGE("cpu_ussage"),
	GUI_LOGGING_BUTTON("logging_button");
	
	private String rstPath;
	private String msgPath;
	private String statePath;
	
	private SensorChannel(String name) {
		this.rstPath = "sensors/" + name + "/reset";
		this.msgPath = "sensors/" + name;
		this.statePath = "sensor/" + name + "/state";
	}
	
	public String getRstPath() {
		return this.rstPath;
	}
	
	public String getMsgPath() {
		return this.msgPath;
	}
	
	public String getStatePath() {
		return this.statePath;
	}
}
