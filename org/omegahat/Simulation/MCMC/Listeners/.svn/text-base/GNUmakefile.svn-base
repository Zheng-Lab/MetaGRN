#OMEGA_HOME=${HOME}/sandbox/omegahat/org/omegahat/

JCFLAGS+=-O -g

all: classes

classes: java $(SRC:%=%.class)
	$(do-directories)

include $(OMEGA_HOME)/Config/GNUmakefile.config

SRC= \
	ResettableListener \
	AcceptanceWriter \
	ArrayListener \
	CovarianceListener \
	CovarianceWriter \
	ExpandingPlottingListener \
	Histogram \
	HistogramWriter \
	ListenerGzipWriter \
	ListenerPrinter \
	ListenerWriter \
	MCMCListenerWriter \
	MeanWriter \
	DistanceWriter \
	SquaredDistanceWriter \
	ConditionalDistanceWriter \
	ConditionalSquaredDistanceWriter \
	PlottingListener \
	QuantileWriter \
	RecordingStepListener \
	StepListenerPrinter \
	StrippedListenerGzipWriter \
	StrippedListenerWriter \
	ThinningProxyListener \
	SplittingProxyListener \
	\
	DistanceListener \
	PosteriorProbWriter \


include $(OMEGA_HOME)/Config/GNUmakefile.rules

javadoc: java
	-mkdir javadoc 
	$(JAVADOC) -d javadoc -private *.java


myclean:
	-rm *.o *.cpp *.h *.a *~ *.class 1  *.cc *.java



