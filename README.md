A Benchmark of Globally-Optimal Methods for the De-Identification of Biomedical Data
====

Introduction
------
This project contains the source code for the benchmark presented in our submission
"A Benchmark of Globally-Optimal Methods for the De-Identification of Biomedical Data" for the
27th IEEE International Symposium on Computer-Based Medical Systems (CBMS 2014).

It provides an implementation of the following algorithms:

1. [Depth-First-Search](http://en.wikipedia.org/wiki/Depth-first_search) - [(Implementation)](https://github.com/arx-deidentifier/anonbench/blob/master/src/org/deidentifier/arx/algorithm/AlgorithmDFS.java)

2. [Breadth-First-Search](http://en.wikipedia.org/wiki/Breadth-first_search) - [(Implementation)](https://github.com/arx-deidentifier/anonbench/blob/master/src/org/deidentifier/arx/algorithm/AlgorithmBFS.java)

3. [Incognito](http://dx.doi.org/10.1145/1066157.1066164) - [(Implementation)](https://github.com/arx-deidentifier/anonbench/blob/master/src/org/deidentifier/arx/algorithm/AlgorithmIncognito.java)

4. [Optimal Lattice Anonymization](http://dx.doi.org/10.1197/jamia.M3144), [implementation details](http://dx.doi.org/10.1109/CBMS.2012.6266366) - [(Implementation)](https://github.com/arx-deidentifier/anonbench/blob/master/src/org/deidentifier/arx/algorithm/AlgorithmOLA.java)

5. [Flash](http://dx.doi.org/10.1109/SocialCom-PASSAT.2012.52) - [(Implementation)](https://github.com/arx-deidentifier/anonbench/blob/master/src/org/deidentifier/arx/algorithm/AlgorithmFlash.java)

All algorithms are implemented within the [ARX framework](http://arx.deidentifier.org/) and the benchmark uses the [SUBFRAME](https://github.com/prasser/subframe) library.

Results
------

On a Desktop PC with a quad-core 3.1 GHz Intel Core i5 CPU running a 64-bit Linux 3.0.14 kernel and a
64-bit Sun JVM (1.7.0 21) executed with *java -Xmx4G -XX:+UseConcMarkSweepGC -jar anonbench-0.1.jar* it produces the following workload averages:

![Image](https://raw.github.com/arx-deidentifier/anonbench/master/doc/mean_check_criteria.png)
![Image](https://raw.github.com/arx-deidentifier/anonbench/master/doc/mean_rollup_criteria.png)
![Image](https://raw.github.com/arx-deidentifier/anonbench/master/doc/mean_time_criteria.png)

![Image](https://raw.github.com/arx-deidentifier/anonbench/master/doc/mean_check_datasets.png)
![Image](https://raw.github.com/arx-deidentifier/anonbench/master/doc/mean_rollup_datasets.png)
![Image](https://raw.github.com/arx-deidentifier/anonbench/master/doc/mean_time_datasets.png)



Downloads
------
[Library (Version 0.1)](https://raw.github.com/arx-deidentifier/anonbench/master/jars/anonbench-0.1.jar)

[Documentation (Version 0.1)](https://raw.github.com/arx-deidentifier/anonbench/master/jars/anonbench-0.1-doc.jar)

[Source (Version 0.1)](https://raw.github.com/arx-deidentifier/anonbench/master/jars/anonbench-0.1-src.jar)