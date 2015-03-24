#OMEGA_HOME=${HOME}/sandbox/omegahat/org/omegahat/
CORBACONFIG=$(OMEGA_HOME)/Interfaces/CORBA/CORBAConfig

JCFLAGS+=-O -g

DIRS=\
	Proposals \
	Expressions \
	Targets \
	Listeners \
	Examples \


all: classes

classes: java $(SRC:%=%.class)
	$(do-directories)

include $(OMEGA_HOME)/Config/GNUmakefile.config
#include $(OMEGA_HOME)/Config/GNUmakefile.CRules


#ifdef CORBACONFIG
# include $(CORBACONFIG)/Makefile.CORBA
#endif



SRC= \
	BaseMarkovChain \
	ContainerState \
	CustomGibbsSampler \
	CustomMarkovChain \
	CustomMetropolisHastingsSampler \
	CustomMetropolisSampler \
	DetailChainStepEvent \
	DetailHastingsCoupledStepEvent \
	DetailedMultiState \
	FullConditionalProposal \
	GeneralProposal \
	GenericChainEvent \
	GenericChainStepEvent \
	GenericListenerHandle \
	GenericMCMCEvent \
	GenericManagerChainEvent \
	GenericManagerEvent \
	GibbsSampler \
	MCMCEvent \
	MCMCListener \
	MCMCListenerHandle \
	MCMCState \
	MCMCStateDatabase \
	MarkovChain \
	MarkovProposal \
	MetropolisHastingsSampler \
	MetropolisSampler \
	MultiDoubleState \
	MultiState \
	MultiTarget \
	NotifyingMCMCObject \
	NotifyingObject \
	ProposalDetails \
	SymmetricProposal \
	\
	HastingsCoupledSampler \
	CustomHastingsCoupledSampler \
	BoundedHastingsCoupledSampler \
	HastingsCoupledProposal \
	\
	BoundedHastingsCoupledMaximizer \


include $(OMEGA_HOME)/Config/GNUmakefile.rules

javadoc: java
	-mkdir javadoc 
	$(JAVADOC) -d javadoc -private `find . -name "*.java" -print`


myclean:
	-rm *.o *.cpp *.h *.a *~ *.class 1  *.cc *.java

#%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
#%%%%%% CORBA STUFF 
#%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

ifdef CORBACONFIG

PACKAGE=CorbaMCMC


IDLCCFLAGS+= -I$(OMEGA_HOME)/Interfaces/CORBA/IDLTypes/ \
	     -I$(OMEGA_HOME)/Simulation/RandomGenerators/
IDLJFLAGS+= --tie -I$(OMEGA_HOME)/Interfaces/CORBA/IDLTypes/ \
	     -I$(OMEGA_HOME)/Simulation/RandomGenerators/	


$(PACKAGE).h: $(PACKAGE).idl
	$(MAKE) idl

idlfeed: $(PACKAGE).idl
	irfeed $(IDLCCFLAGS)  $^

idl: $(PACKAGE).idl 
	$(IDLCC) $(IDLCCFLAGS) $^ #2>&1 |  perl /home/warnes/bin/noerr.pl -L'// line %L "%F"%N'

jidl: $(PACKAGE).idl
	$(IDLJ) $(IDLJFLAGS) $^ 2>&1 |  perl /home/warnes/bin/noerr.pl -L'// line %L "%F"%N'

$(PACKAGE).o: $(PACKAGE).h $(PACKAGE).cpp

corbaSupport: $(PACKAGE)/$(PACKAGE).java 
	-mkdir $(PACKAGE)
	(cd $(PACKAGE) ; $(JAVAC) -classpath ..:$(CLASSPATH):$(OMEGA_HOME)/Jars/Sequences.jar *.java)

$(PACKAGE)/%.java:
	$(MAKE) jidl


CXXFLAGS+=-I$(OMEGA_HOME)/include -I$(OMEGA_HOME)/include/Corba -I/usr/local/include \
          -I$(CORBACONFIG) -DNO_LOCAL_CORBA_CONNECTION -g

CORBA_LDFLAGS = -lCorbaUtils -L$(OMEGA_HOME)/lib -R$(OMEGA_HOME)/lib \
                -lCosNaming -lOB  -L/usr/local/lib -R/usr/local/lib \
                -lsocket -L$(OMEGA_HOME)/Interfaces/CORBA/IDLTypes -g



$(PACKAGE).jar: corbaSupport
	$(JAR) $(JAR_FLAGS) $@ $(PACKAGE)/*.class
	cp $@ $(OMEGA_HOME)/Jars

endif

