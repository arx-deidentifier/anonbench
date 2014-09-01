Introduction
------
This project contains the source code for the benchmark presented in our paper
[A Benchmark of Globally-Optimal Anonymization Methods for Biomedical Data](http://dx.doi.org/10.1109/CBMS.2014.85) 
at the 27th IEEE International Symposium on Computer-Based Medical Systems (CBMS 2014).

The source code comprises our benchmarking environment, which is based upon
[ARX](http://arx.deidentifier.org/) and [SUBFRAME](https://github.com/prasser/subframe).
The benchmark currently provides implementations of the following globally-optimal anonymization algorithms:

1. [Depth-First-Search](http://en.wikipedia.org/wiki/Depth-first_search): the implementation can be found [here](https://github.com/arx-deidentifier/anonbench/blob/master/src/org/deidentifier/arx/algorithm/AlgorithmDFS.java).

2. [Breadth-First-Search](http://en.wikipedia.org/wiki/Breadth-first_search): the implementation can be found [here](https://github.com/arx-deidentifier/anonbench/blob/master/src/org/deidentifier/arx/algorithm/AlgorithmBFS.java).

3. [Incognito](http://dx.doi.org/10.1145/1066157.1066164): the implementation can be found [here](https://github.com/arx-deidentifier/anonbench/blob/master/src/org/deidentifier/arx/algorithm/AlgorithmIncognito.java).

4. [Optimal Lattice Anonymization](http://dx.doi.org/10.1197/jamia.M3144): implementation details are presented in this [paper](http://dx.doi.org/10.1109/CBMS.2012.6266366)
   and the implementation can be found [here](https://github.com/arx-deidentifier/anonbench/blob/master/src/org/deidentifier/arx/algorithm/AlgorithmOLA.java).

5. [Flash](http://dx.doi.org/10.1109/SocialCom-PASSAT.2012.52): the implementation can be found [here](https://github.com/arx-deidentifier/anonbench/blob/master/src/org/deidentifier/arx/algorithm/AlgorithmFlash.java).

Privacy criteria
------
All 11 reasonable combinations of the following privacy criteria are evaluated in our benchmark:

1. [5-anonymity](http://dx.doi.org/10.1142/S0218488502001648)

2. [recursive-(4,3)-diversity](http://dx.doi.org/10.1145/1217299.1217302)

3. [0.2-closeness (EMD with hierarchical distance)](http://dx.doi.org/10.1109/ICDE.2007.367856)

4. [(0.05, 0.15)-presence](http://dx.doi.org/10.1145/1247480.1247554)


Datasets
------

For licensing reasons no data is contained in this repository. Please contact arx.deidentifier@gmail.com for information on how to obtain the benchmark datasets.

Results
------

The following figures show key parameters averaged over either the datasets or the privacy 
criteria. The number of checks gives an indication of an algorithm's pruning power, the 
number of roll-ups gives an indication of an algorithm's optimizability and, finally, the 
execution times give an indication of an algorithm's overall performance within the ARX runtime environment.

On a Desktop PC with a quad-core 3.1 GHz Intel Core i5 CPU running a 64-bit Linux 3.0.14 kernel and a
64-bit Sun JVM (1.7.0 21) the following results are produced (*java -Xmx4G -XX:+UseConcMarkSweepGC -jar anonbench-0.2.jar*):

Geometric mean of key parameters over all five benchmark datasets:

![Image](https://raw.github.com/arx-deidentifier/anonbench/master/doc/mean_check_criteria.png)

![Image](https://raw.github.com/arx-deidentifier/anonbench/master/doc/mean_rollup_criteria.png)

![Image](https://raw.github.com/arx-deidentifier/anonbench/master/doc/mean_time_criteria.png)

Geometric mean of key parameters over all eleven combinations of privacy criteria:

![Image](https://raw.github.com/arx-deidentifier/anonbench/master/doc/mean_check_datasets.png)

![Image](https://raw.github.com/arx-deidentifier/anonbench/master/doc/mean_rollup_datasets.png)

![Image](https://raw.github.com/arx-deidentifier/anonbench/master/doc/mean_time_datasets.png)

Downloads
------
[Library (Version 0.2)](https://raw.github.com/arx-deidentifier/anonbench/master/jars/anonbench-0.2.jar)

[Documentation (Version 0.2)](https://raw.github.com/arx-deidentifier/anonbench/master/jars/anonbench-0.2-doc.jar)

[Source (Version 0.2)](https://raw.github.com/arx-deidentifier/anonbench/master/jars/anonbench-0.2-src.jar)