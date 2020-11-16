package initPackage;

import java.io.*;

public class MainProgram {

	public static void main(String[] args) {
		String inputFile = "./inputBinaryFile";								/* Specify input file name */
		try (InputStream inputStream = new FileInputStream(inputFile);) {
			long fileSize = new File(inputFile).length();					/* get file length */	
			System.out.println("length of file = " + fileSize);
			byte[] allBytes = new byte[(int)fileSize];
			inputStream.read(allBytes);
			
			System.out.println(allBytes);
			
			run_program((int)fileSize);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	static instruction[] program = new instruction[1024]; /* Instruction memory */
	static char[] memory = new char[256]; /* Data memory */

	public static void run_program(int num_bytes) {

		int pc = -1; /* Program counter */
		char[] reg = new char[16];
		byte fb, sb;

		while (++pc < (num_bytes / 2)) {
			fb = program[pc].getFirstByte();
			sb = program[pc].getSecondByte();
			switch (fb >> 4) {
			case 0: reg[fb & 0x0f] = memory[sb]; break;
			case 1: memory[sb] = reg[fb & 0x0f]; break;
			case 2: memory[reg[fb & 0x0f]] = reg[sb >> 4]; break;
			case 3: reg[fb & 0x0f] = (char) sb; break;
			case 4: reg[fb & 0x0f] += reg[sb >> 4]; break;
			case 5: reg[fb & 0x0f] -= reg[sb >> 4]; break;
			case 6: pc += sb; break;
			default: return;
			}
		}
	}

}
