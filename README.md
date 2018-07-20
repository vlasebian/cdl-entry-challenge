# CDL 2018 Admission
Candidate: Vitan Vlad-Sebastian   
Language used: Java

# Description
The code contains 3 classes: Main, Code and Processor.

The Processor class contains the registers, the number of the next instruction 
that the CPU will run, a queue that stores the values send by other CPUs and a
state variable which is true when the processor need to recieve a value from 
another processor. The implemented methods work with these class members (queue, 
enqueue, set state, get the number of the next instruction to run, set the 
number of the next instrucion to run etc.)

The Code class is used to read the input file (the instructions) which is saved
as an array of strings. This class also has the number of processors and three
strings that represent the command, the first argument and the second argument
which are modified by calling the nextInstruction method. Besides this, there
are other methods for getting and setting the class members.

The Main class is used to run the commands from the input file on the CPUS. This 
class creates the processors, runs each instruction on each processor by calling
the nextInstrucion() method reapetedly which takes as an argument the number of
the instruction the current CPU has to run, then executes the command accordingly.

More details in the code comments. 
