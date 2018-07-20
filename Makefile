JFLAGS = -g
JC = javac

.SUFFIXES: .java .class

.java.class:
	$(JC) $(JFLAGS) $*.java

# CLASSES is a macro that contains the java sources

CLASSES= \
	 Code.java \
	 Processor.java \
	 Main.java

# Default make target entry

build: classes

classes: $(CLASSES:.java=.class)

run: build
	java Main

clean:
	$(RM) *.class



