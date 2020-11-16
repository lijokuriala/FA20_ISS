package initPackage;

import java.io.*;
import java.util.Arrays;

public class MainProgram {

	// Instruction memory
	static instruction[] program = new instruction[1024];
	// Data memory
	static String[] memory = new String[256]; 

	public static void main(String[] args) {
		//Specify the input name
		String inputFile = "./inputBinaryFile"; 
		try (InputStream inputStream = new FileInputStream(inputFile);) {
			// Open the file
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
			String binaryLine = null;
			int instructionsCount = 0;
			// Read lines from the file
			while ((binaryLine = reader.readLine()) != null) {
				// Exit the program if binary file has incorrectly formatted line
				if (binaryLine.length() != 16) {
					System.out.println("Line #" + (instructionsCount + 1) + " is not properly formatted");
					return;
				}
				//Save each line at first byte and second byte in instruction memory
				program[instructionsCount++] = new instruction(binaryLine.substring(0, 8), binaryLine.substring(8));
				System.out.println("Line #" + instructionsCount + " = " + binaryLine);
				System.out.println(program[instructionsCount - 1].toString());
			}
			// Run the execution of the Instruction set
			run_program(instructionsCount);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void run_program(int num_bytes) {
		System.out.println(num_bytes);
		int pc = -1; /* Program counter */
		String[] reg = new String[8];
		String fb, sb;
		int ifb, isb;
		int Rn, Rm;
		String result;

		while (++pc < (num_bytes)) {
			fb = program[pc].getFirstByte();
			sb = program[pc].getSecondByte();

			ifb = Integer.parseInt(fb.toString(), 2);
			isb = Integer.parseInt(sb.toString(), 2);

			switch (ifb >>> 4) {
			case 0: /* MOV Rn, direct */
				reg[ifb & 0x0f] = memory[isb];
				break;
			case 1: /* MOV direct, Rn */
				memory[isb] = reg[ifb & 0x0f];
				break;
			case 2: /* MOV @Rn, Rm */
				memory[Integer.parseInt(reg[ifb & 0x0f])] = reg[isb >> 4];
				break;
			case 3: /* MOV Rn, #immed */
				reg[ifb & 0x0f] = sb;
				break;
			case 4: /* ADD Rn, Rm */
				Rn = Integer.parseInt(reg[ifb & 0x0f], 2); // Value in first register
				Rm = Integer.parseInt(reg[isb >>> 4], 2); // Value in second register
				result = String.format("%8s", Integer.toBinaryString(Rn + Rm)).replace(' ', '0');
				reg[ifb & 0x0f] = result;
				break;
			case 5: /* SUB Rn, Rm *//* Rn = Rn - Rm */
				Rn = Integer.parseInt(reg[ifb & 0x0f], 2);
				Rm = Integer.parseInt(reg[isb >>> 4], 2);
				result = String.format("%8s", Integer.toBinaryString(Rn - Rm)).replace(' ', '0');
				reg[ifb & 0x0f] = result;
				break;
			case 6: /* JZ Rn, Relative */
				if (Integer.parseInt(reg[Integer.parseInt(fb.substring(4, 8), 2)], 2) == 0) {
					if (sb.charAt(0) == '1') {
						isb = ~isb + 1;
						String sbyte = Integer.toBinaryString(isb);
						sbyte = sbyte.substring(sbyte.length() - 8);
						isb = 0 - Integer.parseInt(sbyte, 2);
					}
					System.out.println("isb is " + isb);
					pc += isb;
				}
				break;
			default:
				return;
			}

			System.out.println("Register array: " + Arrays.toString(reg));
		}
	}

}
