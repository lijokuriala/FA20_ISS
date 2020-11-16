package initPackage;

public class instruction {
	private String firstByte;
	private String secondByte;
	
	public instruction(String first_Byte, String second_Byte) {
		this.firstByte = first_Byte;
		this.secondByte = second_Byte;
	}

	public String getFirstByte() {
		return firstByte;
	}

	public void setFirstByte(String firstByte) {
		this.firstByte = firstByte;
	}

	public String getSecondByte() {
		return secondByte;
	}

	public void setSecondByte(String secondByte) {
		this.secondByte = secondByte;
	}

	@Override
	public String toString() {
		return "instruction [firstByte=" + firstByte + ", secondByte=" + secondByte + "]";
	}


	
}
