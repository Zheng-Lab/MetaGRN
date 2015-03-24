#OMEGA_HOME=${HOME}/sandbox/omegahat/org/omegahat/

PACKAGE_NAME=Distributions
PACKAGE=Distributions

default: classes

JCFLAGS+=-O -g

##OMEGA_HOME=../..

include $(OMEGA_HOME)/Config/GNUmakefile.config

SRC=    \
        Bivariate \
        ConditionalDensity \
        ConditionalGenerator \
        Continuous \
        CumulativeConditionalDensity \
        CumulativeDensity \
        Density \
        Discrete \
        Distribution \
        Generator \
        Univariate \
        UnnormalizedConditionalDensity \
        UnnormalizedDensity \
	DistributionFunctions \
	PRNGDistributionFunctions \
	Normal \
	MVNormal \
	#NormalDistribution \



classes: java $(SRC:%=%.class)

Naras.java: Naras.nw
	notangle $< > $@

NarasPlus.java: 
	echo ## no-op just so it doesn't get clobbered


include $(OMEGA_HOME)/Config/GNUmakefile.rules

%.java: Distribution.nw
	notangle -R$* $< > $@

javadoc: java
	-mkdir javadoc 
	/usr/java1.2/bin/javadoc -d javadoc *.java

paths:
	echo ${CLASSPATH}