#OMEGA_HOME=${HOME}/sandbox/omegahat/org/omegahat/

JCFLAGS+=-O -g

all: classes

classes: java $(SRC:%=%.class)
	$(do-directories)

include $(OMEGA_HOME)/Config/GNUmakefile.config

SRC=	AdaptiveNormalMetropolisProposal \
	AdaptiveNormalKernelProposal \
	LocallyAdaptiveNormalKernelProposal \
	LocallyAdaptiveNormalProposal \
	AdaptiveNormalProposal \
	IndependenceMultiProposal \
	IndependenceProposal \
	IteratedProposal \
	MixtureProposal \
	MultiProposal \
	NormalMetropolisProposal \
	NormalMetropolisComponentProposal \
	NormalProposal \
	TimeDependentProposal \
	KernelProposal \
	AdaptiveProposal \
	AdaptiveKernelProposal \
	AdaptiveMultiKernelProposal \
	NormalKernelProposal \
	NormalDualKernelProposal \
	AdaptiveNormalDualKernelProposal \
	HastingsCoupledMixtureProposal \
	HastingsCoupledIteratedProposal \
	IndependentHastingsCoupledProposal \
	AdaptiveMultiSourceCoupler \
	KernelDirectionSampler 


include $(OMEGA_HOME)/Config/GNUmakefile.rules

javadoc: java
	-mkdir javadoc 
	$(JAVADOC) -d javadoc -private *.java


myclean:
	-rm *.o *.cpp *.h *.a *~ *.class 1  *.cc *.java



