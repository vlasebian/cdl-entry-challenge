/* a Processor object contains the CPU queue,
 * the registers and the current instruction
 * counter.
 */

import java.util.ArrayDeque;
import java.util.Vector;

public class Processor {
    private int instructionCounter;
    // Queue where received values are stored
    private ArrayDeque<Integer> queue;
    // Registers
    private Vector<Integer> registers;
    // CPU state, if it is true is listening
    private boolean listening;
    // CPU identifier
    private int id;
    // CPU execution state, if it is true all the instructions were executed
    private boolean state;

    public Processor(int n) {
        instructionCounter = 0;
        queue = new ArrayDeque<Integer>();

        // Initializing registers
        registers = new Vector<Integer>(32);
        registers.add(0, n);
        for (int i = 1; i < 32; i ++) {
            registers.add(i, 0);
        }
        id = n;
        listening = false;
        state = false;
    }

    public int getCoreNumber() {
        return id;
    }

    public void setState(boolean x) {
        state = x;
    }

    public boolean done() {
        return this.state;
    }

    public int getInstructionCounter() {
        return instructionCounter;
    }

    public void setInstructionCounter(int x) {
        instructionCounter = x;
    }

    public void incCounter() {
        instructionCounter ++;
    }

    public void enqueueValue(int x) {
        queue.add(x);
    }

    public int dequeueValue() {
        return queue.poll();
    }

    public boolean isQueueEmpty() {
        return queue.isEmpty();
    }

    public void setRegister(int n, int x) {
        registers.set(n, x);
    }

    public int getRegister(int n) {
        return registers.get(n);
    }

    public boolean isListening() {
        return listening;
    }

    public void setListening(boolean x) {
        listening = x;
    }

    public String nonZeroRegisters() {
        String s = new String();
        for (int i : registers) {
            if (i != 0) {
                s += i + " ";
            }
        }

        return s;
    }

}
