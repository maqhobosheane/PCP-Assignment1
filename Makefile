# Makefile for CSC2002S Assignment 1
# Author: Maqhobosheane MOhlerepe
# Date: 6 August 2022

JAVAC  = javac
BINDIR = bin
SRCDIR = src
DOCDIR = docs
TEST   = src/test

.SUFFIXES: .java .class

$(BINDIR)/%.class:$(SRCDIR)/%.java 
	$(JAVAC) -d $(BINDIR)/ -cp $(BINDIR) $<

CLASSES = MeanFilterSerial.class MeanFilterParallel.class MedianFilterSerial.class MedianFilterParallel.class

CLASS_FILES = $(CLASSES:%.class=$(BINDIR)/%.class)

default: $(CLASS_FILES)

clean:
	rm $(BINDIR)/*.class
	rmdir $(BINDIR)/

runAll:
	java -cp bin MeanFilterSerial ${INPUT_IMAGE} ${OUTPUT_IMAGE} ${WINDOW_WIDTH}
	java -cp bin MeanFilterParallel ${INPUT_IMAGE} ${OUTPUT_IMAGE} ${WINDOW_WIDTH}
	java -cp bin MedianFilterSerial ${INPUT_IMAGE} ${OUTPUT_IMAGE} ${WINDOW_WIDTH}
	java -cp bin MedianFilterParallel ${INPUT_IMAGE} ${OUTPUT_IMAGE} ${WINDOW_WIDTH}
runMean_serial:
	java -cp bin MeanFilterSerial ${INPUT_IMAGE} ${OUTPUT_IMAGE} ${WINDOW_WIDTH}
runMean_parallel: 
	java -cp bin MeanFilterParallel ${INPUT_IMAGE} ${OUTPUT_IMAGE} ${WINDOW_WIDTH}
runMedian_serial:
	java -cp bin MedianFilterSerial ${INPUT_IMAGE} ${OUTPUT_IMAGE} ${WINDOW_WIDTH}
runMedian_parallel:
	java -cp bin MedianFilterParallel ${INPUT_IMAGE} ${OUTPUT_IMAGE} ${WINDOW_WIDTH}


