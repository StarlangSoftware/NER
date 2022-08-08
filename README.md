# Named Entity Recognition

## Task Definition

In named entity recognition, one tries to find the strings within a text that correspond to proper names (excluding TIME and MONEY) and classify the type of entity denoted by these strings. The problem is difficult partly due to the ambiguity in sentence segmentation; one needs to extract which words belong to a named entity, and which not. Another difficulty occurs when some word may be used as a name of either a person, an organization or a location. For example, Deniz may be used as the name of a person, or - within a compound - it can refer to a location Marmara Denizi 'Marmara Sea', or an organization Deniz Taşımacılık 'Deniz Transportation'.

The standard approach for NER is a word-by-word classification, where the classifier is trained to label the words in the text with tags that indicate the presence of particular kinds of named entities. After giving the class labels (named entity tags) to our training data, the next step is to select a group of features to discriminate different named entities for each input word.

[<sub>ORG</sub> Türk Hava Yolları] bu [<sub>TIME</sub> Pazartesi'den] itibaren [<sub>LOC</sub> İstanbul] [<sub>LOC</sub> Ankara] hattı için indirimli satışlarını [<sub>MONEY</sub> 90 TL'den] başlatacağını açıkladı.

[<sub>ORG</sub> Turkish Airlines] announced that from this [<sub>TIME</sub> Monday] on it will start its discounted fares of [<sub>MONEY</sub> 90TL] for [<sub>LOC</sub> İstanbul] [<sub>LOC</sub> Ankara] route.

See the Table below for typical generic named entity types.

|Tag|Sample Categories|
|---|---|
|PERSON|people, characters|
|ORGANIZATION|companies, teams|
|LOCATION|regions, mountains, seas|
|TIME|time expressions|
|MONEY|monetarial expressions|

## Data Annotation

### Preparation

1. Collect a set of sentences to annotate. 
2. Each sentence in the collection must be named as xxxx.yyyyy in increasing order. For example, the first sentence to be annotated will be 0001.train, the second 0002.train, etc.
3. Put the sentences in the same folder such as *Turkish-Phrase*.
4. Build the project and put the generated sentence-ner.jar file into another folder such as *Program*.
5. Put *Turkish-Phrase* and *Program* folders into a parent folder.
![Main Folder](folder.png)

### Annotation

1. Open sentence-ner.jar file.
2. Wait until the data load message is displayed.
3. Click Open button in the Project menu.
![Open File](open.png)
4. Choose a file for annotation from the folder *Turkish-Phrase*.  
![Choose File](choosefile.png)
5. For each word in the sentence, click the word, and choose approprite entity tag from PERSON, ORGANIZATION, LOCATION, TIME, or MONEY tags.
![NER Annotation](ner.png)
6. Click one of the next buttons to go to other files.

## Classification DataSet Generation

After annotating sentences, you can use [DataGenerator](https://github.com/starlangsoftware/DataGenerator) package to generate classification dataset for the Named Entity Recognition task.

## Generation of ML Models

After generating the classification dataset as above, one can use the [Classification](https://github.com/starlangsoftware/Classification) package to generate machine learning models for the Named Entity Recognition task.

Class Diagram
============

<img src="classDiagram.png">

Video Lectures
============

[<img src=video.jpg width="50%">](https://youtu.be/4pxdvP_Rfd8)

For Developers
============

You can also see [Python](https://github.com/starlangsoftware/NER-Py), [Cython](https://github.com/starlangsoftware/NER-Cy), [C++](https://github.com/starlangsoftware/NER-CPP), [Swift](https://github.com/starlangsoftware/NER-Swift), [Js](https://github.com/starlangsoftware/NER-Js), or [C#](https://github.com/starlangsoftware/NER-CS) repository.

## Requirements

* [Java Development Kit 8 or higher](#java), Open JDK or Oracle JDK
* [Maven](#maven)
* [Git](#git)

### Java 

To check if you have a compatible version of Java installed, use the following command:

    java -version
    
If you don't have a compatible version, you can download either [Oracle JDK](https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) or [OpenJDK](https://openjdk.java.net/install/)    

### Maven
To check if you have Maven installed, use the following command:

    mvn --version
    
To install Maven, you can follow the instructions [here](https://maven.apache.org/install.html).      

### Git

Install the [latest version of Git](https://git-scm.com/book/en/v2/Getting-Started-Installing-Git).

## Download Code

In order to work on code, create a fork from GitHub page. 
Use Git for cloning the code to your local or below line for Ubuntu:

	git clone <your-fork-git-link>

A directory called NER will be created. Or you can use below link for exploring the code:

	git clone https://github.com/olcaytaner/NER.git

## Open project with IntelliJ IDEA

Steps for opening the cloned project:

* Start IDE
* Select **File | Open** from main menu
* Choose `NER/pom.xml` file
* Select open as project option
* Couple of seconds, dependencies with Maven will be downloaded. 


## Compile

**From IDE**

After being done with the downloading and Maven indexing, select **Build Project** option from **Build** menu. After compilation process, user can run NER.

**From Console**

Go to `NER` directory and compile with 

     mvn compile 

## Generating jar files

**From IDE**

Use `package` of 'Lifecycle' from maven window on the right and from `NER` root module.

**From Console**

Use below line to generate jar file:

     mvn install

## Maven Usage

        <dependency>
            <groupId>io.github.starlangsoftware</groupId>
            <artifactId>NER</artifactId>
            <version>1.0.1</version>
        </dependency>

Detailed Description
============

## ParseTree

In order to find the named entities in a parse tree, one uses autoNER method of the TreeAutoNER class.

	ParseTreeDrawable parseTree = ...
	TurkishTreeAutoNER turkishNer = new TurkishTreeAutoNER(ViewLayerType.Turkish);
	turkishNer.autoNER(parseTree);

## Sentence

In order to find the named entities in a simple sentence, one uses autoNER method of the SentenceAutoNER class.

	AnnotatedSentence sentence = ...
	TurkishSentenceAutoNER turkishNer = new TurkishSentenceAutoNER();
	turkishNer.autoNER(sentence);

# Cite

	@INPROCEEDINGS{8093439,
  	author={B. {Ertopçu} and A. B. {Kanburoğlu} and O. {Topsakal} and O. {Açıkgöz} and A. T. {Gürkan} and B. {Özenç} and İ. {Çam} and B. {Avar} and G. {Ercan} 	and O. T. {Yıldız}},
  	booktitle={2017 International Conference on Computer Science and Engineering (UBMK)}, 
  	title={A new approach for named entity recognition}, 
  	year={2017},
  	volume={},
  	number={},
  	pages={474-479},
  	doi={10.1109/UBMK.2017.8093439}}
