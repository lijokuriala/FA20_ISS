package initPackage;

public class instruction {
	private byte firstByte;
	private byte secondByte;
	
	public instruction(byte first_Byte, byte second_Byte) {
		this.firstByte = first_Byte;
		this.secondByte = second_Byte;
	}

	public byte getFirstByte() {
		return firstByte;
	}

	public void setFirstByte(byte firstByte) {
		this.firstByte = firstByte;
	}

	public byte getSecondByte() {
		return secondByte;
	}

	public void setSecondByte(byte secondByte) {
		this.secondByte = secondByte;
	}


	
}
