package initPackage;

import java.io.*;
import java.util.Arrays;

public class MainProgram {

	// Instruction memory
	static instruction[] program = new instruction[1024];
	// Data memory
	static String[] memory = new String[256]; 
	// Register array of 8 registers
	static String[] register = new String[8];						

	public static void main(String[] args) {
		//Specify the input file name
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
					return; // Exit the program
				}
				//Save each line as first byte and second byte in instruction memory
				program[instructionsCount++] = new instruction(binaryLine.substring(0, 8), binaryLine.substring(8));
				//System.out.println("Line #" + instructionsCount + " = " + binaryLine);
				//System.out.println(program[instructionsCount - 1].toString());
			}
			// Run the execution of the Instruction set
			run_program(instructionsCount);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// Generic method to print Register Array
	public static void printRegisterArray() {
		System.out.println("Register array: " + Arrays.toString(register));
	}

	public static void run_program(int num_of_instr) {
		// Program Counter
		int program_counter = -1;								
		// First and second byte of instruction
		String first_instr_byte, second_instr_byte;				
		// First and second byte in integer
		int first_byte_int, second_byte_int;					
		// Placeholder variables
		int registerN, registerM;								
		String temp_result;										

		
		// Run as many times as number of instructions in input file
		while (++program_counter < (num_of_instr)) {						
			// Get the first and second byte of the instruction
			first_instr_byte = program[program_counter].getFirstByte();		
			second_instr_byte = program[program_counter].getSecondByte();	

			first_byte_int = Integer.parseInt(first_instr_byte.toString(), 2);
			second_byte_int = Integer.parseInt(second_instr_byte.toString(), 2);

			// Decode the instruction
			switch (first_byte_int >>> 4) {									
			case 0: /* MOV Rn, direct */
				register[first_byte_int & 0x0f] = memory[second_byte_int];
				break;
			case 1: /* MOV direct, Rn */
				memory[second_byte_int] = register[first_byte_int & 0x0f];
				break;
			case 2: /* MOV @Rn, Rm */
				memory[Integer.parseInt(register[first_byte_int & 0x0f])] = register[second_byte_int >>> 4];
				break;
			case 3: /* MOV Rn, #immed */
				register[first_byte_int & 0x0f] = second_instr_byte;
				break;
			case 4: /* ADD Rn, Rm */
				// Value in registers stored in temp variables for calculation
				registerN = Integer.parseInt(register[first_byte_int & 0x0f], 2); 
				registerM = Integer.parseInt(register[second_byte_int >>> 4], 2); 
				temp_result = String.format("%8s", Integer.toBinaryString(registerN + registerM)).replace(' ', '0'); // Format to 8 bit positions
				register[first_byte_int & 0x0f] = temp_result;
				break;
			case 5: /* SUB Rn, Rm *//* Rn = Rn - Rm */
				// Value in registers stored in temp variables for calculation
				registerN = Integer.parseInt(register[first_byte_int & 0x0f], 2); 
				registerM = Integer.parseInt(register[second_byte_int >>> 4], 2); 
				temp_result = String.format("%8s", Integer.toBinaryString(registerN - registerM)).replace(' ', '0'); // Format to 8 bit positions
				register[first_byte_int & 0x0f] = temp_result;
				break;
			case 6: /* JZ Rn, Relative */
				if (Integer.parseInt(register[Integer.parseInt(first_instr_byte.substring(4, 8), 2)], 2) == 0) {
					// This if loop is to find 2's complement of signed operand
					// Assumed that second byte starting with 1 is a signed operand
					// and is used for jumping back up in the instruction list
					if (second_instr_byte.charAt(0) == '1') {
						second_byte_int = ~second_byte_int + 1;
						String sbyte = Integer.toBinaryString(second_byte_int);
						sbyte = sbyte.substring(sbyte.length() - 8);
						second_byte_int = 0 - Integer.parseInt(sbyte, 2);
					}
					program_counter += second_byte_int;
				}
				break;
			default:
				return;
			} // end of Switch
			// Print the register array after each instruction is executed
			printRegisterArray();
		} // end of while
	} //end of run_program

}
