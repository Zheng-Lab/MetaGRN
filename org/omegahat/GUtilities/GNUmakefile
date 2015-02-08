#OMEGA_HOME=${HOME}/sandbox/omegahat/org/omegahat/

JCFLAGS+=-O

all: classes

classes: java $(SRC:%=%.class)
	$(do-directories)

include $(OMEGA_HOME)/Config/GNUmakefile.config

SRC= \
	Distance \
	ArrayTools \
	ReadData \


include $(OMEGA_HOME)/Config/GNUmakefile.rules

javadoc: java
	-mkdir javadoc 
	$(JAVADOC) -d javadoc -private *.java


myclean:
	-rm *.o *.cpp *.h *.a *~ *.class 1  *.cc *.java



