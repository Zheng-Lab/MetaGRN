# MetaGRN

## Migrate from BitBucket
We migrated MetaGRN project from [BitBucket repository](https://bitbucket.org/birc_ntu/metagrn) (commit [2ca247a](https://bitbucket.org/birc_ntu/metagrn/commits/2ca247ab413f823a3b782bc50c40e0c8ea8519ca)) to here.

## Java version
MetaGRN requires JRE 1.7+.

## Build from source
To build runnable jar file from source, you need install [Apache Ant](http://ant.apache.org/).

After installing Ant successfully, go to `ant/` directory and edit `build.xml` by changing the following line:

```xml
<property name="workspace" location="/full/path/parent/folder/of/source/code"/>
```

change `location` property to the full path of parent folder of source code (parent of `ant` directory). Then, run the following command in your command line (change directory to `ant/` first, make sure you add ant to your executable path):

```bash
ant -f build.xml
```

You will find a `dist/` folder generated under `ant/` folder, the runnable jar `MetaGRN.jar` is inside.

## NOTES to Windows users
It is a known issue that JRE cannot handle long path or path with white spaces very well. If you encounter a problem when run MetaGRN.jar, please move it to a path shorter and without white spaces.

