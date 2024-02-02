This is a reference implementation of the CPE Naming and Matching algorithms, as found in the Common Platform Enumeration Naming Specification version 2.3 and Common Platform Enumeration Name Matching Specification version 2.3.  

See http://cpe.mitre.org for information about the Common Platform Enumeration standard. 

The following source packages are included in this release:

org.mitre.cpe.common  - contains functions common to both the naming and matching packages
org.mitre.cpe.matching - contains classes related to the CPE matching algorithm
org.mitre.cpe.naming  - contains classes related to the CPE naming algorithms

Build Instructions
-------------------
Required Software
   - JDK (http://www.oracle.com/technetwork/java/javase/downloads/index.html)
   - Apache Ant (http://ant.apache.org/)
	
Run Ant using the build.xml supplied in this release.  The Ant script will compile all Java source files under the src/ directory and generate 3 sample JAR files in the dist/ directory.

This implementation has been built and tested using the Java Platform Standard Edition 6 JDK  and Apache Ant 1.8 on Microsoft Windows 7.  

Run Instructions
-----------------
After building using the build.xml file, navigate to the dist/ directory.  There will be 3 JAR files, one corresponding to each main function in the source code.  The main functions are found in:
	- org.mitre.cpe.naming.CPENameBinder
	- org.mitre.cpe.naming.CPENameUnbinder
	- org.mitre.cpe.matching.CPENameMatcher   

To run the JAR files and see the output from the sample main functions, use "java -jar filename.jar", replacing filename.jar with one of the generated JAR files.