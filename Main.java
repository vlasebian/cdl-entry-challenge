import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

public class Main {
    public static void main(String[] args) {
        // Load the code into memory
        Code r = new Code("code.in");

        // Initialize the CPUs
        Vector<Processor> cpus = new Vector<Processor>(r.getNumberOfCores());
        for (int i = 0; i < r.getNumberOfCores(); i ++) {
            cpus.add(new Processor(i));
        }

        // reg1 is the first argument (always a register, except for snd)
        int reg1;
        // reg2 is the second argument (can be a register or a value)
        int reg2;
        // If cpusLeft is equal to 0, then the execution is over
        int cpusLeft = r.getNumberOfCores();
        // Number of processors in rcv
        int rcvState = 0;

        do {
            for (Processor p : cpus) {
                // Get the cpu's current instruction
                r.setCodePointer(p.getInstructionCounter());

                // If the CPU has more instructions to execute
                if (!p.done()) {
                    // Get the next instruction
                    if(!r.nextInstruction()) {
                        // If there are no instructions left for this CPU, execution is done
                        cpusLeft--;
                        p.setState(true);
                        continue;
                    }
                } else {
                    continue;
                }

                switch (r.getCommand()) {
                    case "set": {
                        // Parse the arguments of the command
                        reg1 = Integer.parseInt(r.getFirstArg().substring(1));
                        if (r.getSecondArg().charAt(0) == 'R') {
                            // Second argument is a register
                            reg2 = Integer.parseInt(r.getSecondArg().substring(1));
                            reg2 = p.getRegister(reg2);
                        } else {
                            // Second argument is a value
                            reg2 = Integer.parseInt(r.getSecondArg());
                        }
                        // Set the register
                        p.setRegister(reg1, reg2);

                        // Increment the processor counter
                        p.incCounter();
                        break;
                    }

                    case "add": {
                        // Parse the arguments of the command
                        reg1 = Integer.parseInt(r.getFirstArg().substring(1));
                        if (r.getSecondArg().charAt(0) == 'R') {
                            // Second argument is a register
                            reg2 = Integer.parseInt(r.getSecondArg().substring(1));
                            reg2 = p.getRegister(reg2);
                        } else {
                            // Second argument is a value
                            reg2 = Integer.parseInt(r.getSecondArg());
                        }
                        p.setRegister(reg1, p.getRegister(reg1) + reg2);

                        // Increment the processor counter
                        p.incCounter();
                        break;
                    }

                    case "mul": {
                        // Parse the arguments of the command
                        reg1 = Integer.parseInt(r.getFirstArg().substring(1));
                        if (r.getSecondArg().charAt(0) == 'R') {
                            // Second argument is a register
                            reg2 = Integer.parseInt(r.getSecondArg().substring(1));
                            reg2 = p.getRegister(reg2);
                        } else {
                            // Second argument is a value
                            reg2 = Integer.parseInt(r.getSecondArg());
                        }
                        p.setRegister(reg1, p.getRegister(reg1) * reg2);

                        // Increment the processor counter
                        p.incCounter();
                        break;
                    }

                    case "mod": {
                        // Parse the arguments of the command
                        reg1 = Integer.parseInt(r.getFirstArg().substring(1));
                        if (r.getSecondArg().charAt(0) == 'R') {
                            // Second argument is a register
                            reg2 = Integer.parseInt(r.getSecondArg().substring(1));
                            reg2 = p.getRegister(reg2);
                        } else {
                            // Second argument is a value
                            reg2 = Integer.parseInt(r.getSecondArg());
                        }
                        p.setRegister(reg1, p.getRegister(reg1) % reg2);
                        // Increment the processor counter

                        p.incCounter();
                        break;
                    }

                    case "jgz": {
                        // Parse the arguments of the command
                        if (r.getFirstArg().charAt(0) == 'R') {
                            // First argument is a register
                            reg1 = Integer.parseInt(r.getFirstArg().substring(1));
                            reg1 = p.getRegister(reg1);
                        } else {
                            // First argument is a value
                            reg1 = Integer.parseInt(r.getFirstArg());
                        }

                        if (r.getSecondArg().charAt(0) == 'R') {
                            // Second argument is a register
                            reg2 = Integer.parseInt(r.getSecondArg().substring(1));
                            reg2 = p.getRegister(reg2);
                        } else {
                            // Second argument is a value
                            reg2 = Integer.parseInt(r.getSecondArg());
                        }

                        if (reg1 > 0) {
                            // If the condition applies, set the instruction counter accordingly
                            int counter = p.getInstructionCounter();
                            p.setInstructionCounter(counter + reg2);
                        } else {
                            // If the condition does not apply, increment the counter
                            p.incCounter();
                        }

                        break;
                    }

                    case "snd": {
                        // Parse the argument of the command
                        if (r.getFirstArg().charAt(0) == 'R') {
                            // Argument is a register
                            reg1 = Integer.parseInt(r.getFirstArg().substring(1));
                            reg1 = p.getRegister(reg1);
                        } else {
                            // Second argument is a value
                            reg1 = Integer.parseInt(r.getFirstArg());
                        }

                        // Send the value to the next processor (add it to its queue)
                        Processor nextCPU;
                        int crtCoreNr = p.getCoreNumber();

                        if (crtCoreNr == r.getNumberOfCores() - 1) {
                            nextCPU = cpus.get(0);
                        } else {
                            nextCPU = cpus.get(crtCoreNr + 1);
                        }
                        nextCPU.enqueueValue(reg1);

                        p.incCounter();
                        break;
                    }

                    case "rcv": {
                        // Parse the argument of the command
                        reg1 = Integer.parseInt(r.getFirstArg().substring(1));

                        if (p.isQueueEmpty()) {
                            // If the processor's queue is empty, do not increment the
                            // instruction counter and set listening state to true
                            if (p.isListening() == false) {
                                p.setListening(true);
                                rcvState++;
                            }
                            break;
                        } else {
                            // If the core was listening, set listening state to false
                            // and decrement the number of listening cores
                            if (p.isListening()) {
                                rcvState --;
                            }
                            p.setListening(false);
                            p.setRegister(reg1, p.dequeueValue());
                            p.incCounter();
                        }

                        break;
                    }
                }

            }

        // cpusLeft is zero => all the cores finished their execution
        // rcvState is equal to the number of processors =>  deadlock
        // number of cpus that finished execution plus the number of cpus in rcv state equals the number of cores,
        // execution is done
        } while (cpusLeft > 0 &&
                rcvState < r.getNumberOfCores() &&
                rcvState + (r.getNumberOfCores() - cpusLeft) < r.getNumberOfCores());

        // Write the content of the registers to file
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("./code.out"));

            for (Processor p : cpus) {
                writer.append(p.nonZeroRegisters());
                writer.newLine();
            }

            writer.close();
        } catch (IOException e) {
            System.err.println("[Error]: Writing output to file failed.");
        }

    }
}
