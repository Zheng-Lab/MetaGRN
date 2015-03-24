MODULE_NAME=Probability

ifndef OMEGA_HOME
  #OMEGA_HOME=..
endif

include $(OMEGA_HOME)/Config/GNUmakefile.config
include $(OMEGA_HOME)/Config/GNUmakefileModule.config

include MODULES
DIRS=$(MODULES)


all: java $(MODULES)


.PHONY:	$(MODULES)


$(MODULES):
	$(MAKE) -C $@ classes


clean cleanJava java:: 
	$(do-directories)

MODULES: Modules.nw
	$(make-module-list)


include $(OMEGA_HOME)/Config/GNUmakefile.rules
include $(OMEGA_HOME)/Config/GNUmakefileModule.rules

