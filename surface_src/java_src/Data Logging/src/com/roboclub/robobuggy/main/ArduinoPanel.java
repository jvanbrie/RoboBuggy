package com.roboclub.robobuggy.main;

import com.roboclub.robobuggy.serial.SerialEvent;
import com.roboclub.robobuggy.serial.SerialListener;

public class ArduinoPanel extends SerialPanel {
	private static final int RBSM_MID_ENC_TICKS_LAST_H = 0;
	private static final int RBSM_MID_ENC_TICKS_LAST_L = 1;
	private static final int RBSM_MID_ENC_TICKS_RESET_H = 2;
	private static final int RBSM_MID_ENC_TICKS_RESET_L = 3;
	private static final int RBSM_MID_ENC_TIMESTAMP_H = 4;
	private static final int RBSM_MID_ENC_TIMESTAMP_L = 5;
	private static final int RBSM_MID_RESERVED = 252; // 0xFC, message head
	private static final int RBSM_MID_DEVICE_ID = 255;

	private static final long serialVersionUID = -929040896215455343L;
	private static final char[] HEADER = {0xFC, 252, 0, 1};
	private static final int HEADER_LEN = 2;
	private static final int BAUDRATE = 9600;
		
	private long encTicksLastTmp;
	private long encTicksLast;
	private long encTicksResetTmp;
	private long encTicksReset;
	private long encTimestampTmp;
	private long encTimestamp;

	public ArduinoPanel() throws Exception {
		super("ARDUINO", BAUDRATE, HEADER, HEADER_LEN);
		super.addListener(new ArduinoListener());
	}

	private void ParseData(char[] new_data) {
		char[] tmp = new_data;
		int tmp_pos = 0;
		char last_read = 0;

		if(tmp_pos >= tmp.length) {return;}
		last_read = tmp[tmp_pos++];

		while(true) {
			// looking for plausible header
			if(last_read == RBSM_MID_RESERVED) {
				while(true) {
					if(tmp_pos >= tmp.length) {return;}
					last_read = tmp[tmp_pos++];
					// guard for real header
					if(!(last_read == RBSM_MID_RESERVED)) {
						char m_id;
						int m_payload_h;
						int m_payload_l;
						int m_payload;
						m_id = last_read;
						if(tmp_pos >= tmp.length) {return;}
						m_payload_h = tmp[tmp_pos++];
						if(m_payload_h == RBSM_MID_RESERVED) {
							if(tmp_pos >= tmp.length) {return;}
							tmp_pos++;
						}
						m_payload_l = tmp[tmp_pos++];
						if(m_payload_l == RBSM_MID_RESERVED) {
							if(tmp_pos >= tmp.length) {return;}
							tmp_pos++;
						}
						m_payload = (m_payload_h << 8) & m_payload_l;
						ParseMessage(m_id, m_payload);
					}
				}
			}
		}
	}

	private void ParseMessage(char m_id, int m_payload) {
		switch(m_id) {
			case RBSM_MID_ENC_TICKS_LAST_H:
				encTicksLastTmp = (encTicksLastTmp & 0xFF00) & (m_payload << 16);
			break;
			case RBSM_MID_ENC_TICKS_LAST_L:
				encTicksLastTmp = (encTicksLastTmp & 0x00FF) & (m_payload);
			break;
			case RBSM_MID_ENC_TICKS_RESET_H:
				encTicksResetTmp = (encTicksResetTmp & 0xFF00) & (m_payload << 16);
			break;
			case RBSM_MID_ENC_TICKS_RESET_L:
				encTicksResetTmp = (encTicksResetTmp & 0x00FF) & (m_payload);
			break;
			case RBSM_MID_ENC_TIMESTAMP_H:
				encTimestampTmp = (encTimestampTmp & 0xFF00) & (m_payload << 16);
			break;
			case RBSM_MID_ENC_TIMESTAMP_L:
				encTimestampTmp = (encTimestampTmp & 0x00FF) & (m_payload);
				UpdateCache();
			break;
			default:
			break;
		}
	}

	private void UpdateCache() {
		this.encTicksLast = this.encTicksLastTmp;
		this.encTicksReset = this.encTicksResetTmp;
		this.encTimestamp = this.encTimestampTmp;
		// should refresh the screen in here
	}
	
	private class ArduinoListener implements SerialListener {
		@Override
		public void onEvent(SerialEvent event) {
			char[] tmp = event.getBuffer();
			ParseData(tmp);
		}
	}
}