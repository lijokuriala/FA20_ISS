package initPackage;

public class instruction {
	private String firstByte;
	private String secondByte;

	// Constructor method for instruction
	public instruction(String first_Byte, String second_Byte) {
		this.firstByte = first_Byte;
		this.secondByte = second_Byte;
	}

	public String getFirstByte() {
		return firstByte;
	}

	public String getSecondByte() {
		return secondByte;
	}

	@Override
	public String toString() {
		return "instruction [firstByte=" + firstByte + ", secondByte=" + secondByte + "]";
	}
	
}
