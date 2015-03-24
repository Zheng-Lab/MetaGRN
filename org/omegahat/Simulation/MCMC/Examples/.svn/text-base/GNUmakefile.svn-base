#OMEGA_HOME=${HOME}/sandbox/omegahat/org/omegahat/

JCFLAGS+=-O

all: classes

classes: java $(SRC:%=%.class)
	$(do-directories)

include $(OMEGA_HOME)/Config/GNUmakefile.config

SRC= \
	SimpleExample \
	SimpleMetropolisHastingsSampler \
	SimpleGibbsSampler \
	SimpleMetropolisSampler \
	SimpleMarkovChain \
	\
	B_BB_Mixture \
	\
	Binomial_BetaBinomial_Example \
	Binomial_BetaBinomial_Example_II \
	Binomial_BetaBinomial_Example_III \
	Binomial_BetaBinomial_Likelihood \
	Binomial_Binomial_Likelihood \
	\
	Binomial_BetaBinomial_Likelihood_Bimode \
	Binomial_BetaBinomial_Example_NKC \
	Binomial_BetaBinomial_Example_NKC_Bimode \
	Binomial_BetaBinomial_Example_KDS_Bimode \
	HeavyAndLight_Example_NKC \
	\
	Binomial_BetaBinomial_SimpleExample \
	Binomial_BetaBinomial_SimpleExample_NKC \
	Binomial_BetaBinomial_SimpleLikelihood \
	\


include $(OMEGA_HOME)/Config/GNUmakefile.rules

javadoc: java
	-mkdir javadoc 
	$(JAVADOC) -d javadoc -private *.java


myclean:
	-rm *.o *.cpp *.h *.a *~ *.class 1  *.cc *.java



