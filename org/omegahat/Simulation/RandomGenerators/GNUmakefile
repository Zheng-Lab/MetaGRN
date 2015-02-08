PACKAGE=RandomGenerators
FULLNAME=org.omegahat.Simulation.RandomGenerators


SPRNTests: classes TestServer.class CorbaPRNGUtil.o

classes: java

ifndef OMEGA_HOME
  #OMEGA_HOME=../..
endif

ifndef SPRNG_HOME
  SPRNG_HOME=/home/warnes/rice/SPRNG
endif

CORBACONFIG=$(OMEGA_HOME)/Interfaces/CORBA/CORBAConfig
ifdef CORBACONFIG
 include $(CORBACONFIG)/Makefile.CORBA
endif

include $(OMEGA_HOME)/Config/GNUmakefile.config
include $(OMEGA_HOME)/Config/GNUmakefile.CConfig




SRC=\
	CollingsPRNG \
	CollingsPRNGConstants \
	CollingsPRNGState \
	CollingsPRNGAdministrator \
	MultiplicativeCongruentialPRNG \
	PRNGAdministrator \
	PRNGParameters \
	PRNGState \
	PRNGIntegerState \
	PRNGFactory \
	DefaultPRNGFactory \
	PseudoRandomNormalGenerator \
	PRNG \
	PseudoRandomUnivariateGenerator \
	PseudoRandomVariableGenerator \
	Unsigned \
        PRNGAdministratorListener \
        PRNGAdministratorEvent \
        NotifyingPRNG \
	TestCollingsPRNG \
	TestCollingsPRNG_Batch	



TEST=\
  TestServer \
  CorbaPRNGAdministrator \
  CorbaPRNGFactory \
  CorbaPRNGState \
  CorbaPRNGIntegerState \
  IntegerStateCorbaPRNGFactory \
  CollingsCorbaPRNGState \
  CollingsCorbaPRNGAdministrator \
  CollingsCorbaPRNGFactory \
  CorbaPRNG	



TestServer.class: java classes corbaSupport $(TEST:%=%.java) $(TEST:%=%.class) CorbaPRNGUtil.o

#JCFLAGS+= -depend
IDLCCFLAGS+= -I$(OMEGA_HOME)/Interfaces/CORBA/IDLTypes/

classes: java $(SRC:%=%.class)
	$(do-directories)

include $(OMEGA_HOME)/Config/GNUmakefile.rules
include $(OMEGA_HOME)/Config/GNUmakefile.CRules


ifdef CORBACONFIG

IDLJFLAGS+= --tie -I$(OMEGA_HOME)/Interfaces/CORBA/IDLTypes/

CorbaPRNGUtil.h: CorbaPRNGUtil.idl
	$(MAKE) idl

idlfeed: CorbaPRNGUtil.idl
	irfeed $(IDLCCFLAGS)  $^

idl: CorbaPRNGUtil.idl
	$(IDLCC) $(IDLCCFLAGS) $^

jidl: CorbaPRNGUtil.idl
	$(IDLJ) $(IDLJFLAGS) $^

CorbaPRNGUtil.o: CorbaPRNGUtil.h CorbaPRNGUtil.cpp

MODULE_NAME=CorbaPRNGUtil

corbaSupport: $(MODULE_NAME)/CorbaPRNGUtil.java idl
	-mkdir $(MODULE_NAME)
	(cd $(MODULE_NAME) ; $(JAVAC) -classpath ..:$(CLASSPATH):$(OMEGA_HOME)/Jars/Sequences.jar *.java)

$(MODULE_NAME)/%.java:
	$(MAKE) jidl


CXXFLAGS+=-I$(OMEGA_HOME)/include -I$(OMEGA_HOME)/include/Corba -I/usr/local/include \
          -I$(CORBACONFIG) -DNO_LOCAL_CORBA_CONNECTION -g

CORBA_LDFLAGS = -lCorbaUtils -L$(OMEGA_HOME)/lib -R$(OMEGA_HOME)/lib \
                -lCosNaming -lOB  -L/usr/local/lib -R/usr/local/lib \
                -lsocket -L$(OMEGA_HOME)/Interfaces/CORBA/IDLTypes -g

SPRNGCorbaClient.o:  CorbaPRNGUtil.h SPRNGCorbaClient.h SPRNGCorbaClient.cc

SPRNGCorbaMain.o:  SPRNGCorbaClient.o SPRNGCorbaMain.h SPRNGCorbaMain.cc

libSPRNGCorba.a: SPRNGCorbaClient.o SPRNGCorbaMain.o CorbaPRNGUtil.o
	 $(AR) $(ARFLAGS) libSPRNGCorba.a SPRNGCorbaClient.o \
	     SPRNGCorbaMain.o CorbaPRNGUtil.o

SPRNGTests: libSPRNGCorba.a
	cp libSPRNGCorba.a $(SPRNG_HOME)/lib/
	( cd $(SPRNG_HOME)/TESTS ;\
	  $(MAKE) LIBLIST=SPRNGCorba CLDFLAGS="$(CORBA_LDFLAGS)" \
                  CLD=CC SPRNG_TEST_MAIN="" )

endif

javadoc: java
	-mkdir $(OMEGA_HOME)/../../javadoc 
	(cd $(OMEGA_HOME)/../../ ; /usr/java1.2/bin/javadoc -d javadoc $(FULLNAME) )
	/usr/java1.2/bin/javadoc -d javadoc -private *.java


myclean:
	-rm *.o *.cpp *.h *.a *~ *.class 1  *.cc




CorbaPRNGUtil.jar: corbaSupport
	$(JAR) $(JAR_FLAGS) $@ $(MODULE_NAME)/*.class
	cp $@ $(OMEGA_HOME)/Jars


