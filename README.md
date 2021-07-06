# SWitCH 2020 - Postgraduate project #

This is the project developed during the SWitCH 2020 - Postgraduate in software development

## Requirements ##

> Development of an application to support family finance management. This is an important issue, as there are many families who have financial problems due to mismanagement and lack of control over their income and expenses.
>
> The solution designed, aims to take advantage of the possibilities of integration with financial entities and other relevant entities (utilities, supermarket chains, etc.). We believe that integration will increase the adoption and use of the software, as the tedious and error prone manual input of data is the main reason the users of other solutions present to justify abandoning them. To promote its solution, especially to the big players that it wants to integrate with, SWS aims to develop a “life- like” demo of the system.

## Team ##

An Agile methodology was adopted.

The team was composed by 8 people.

- Ana Ferreira
- [Bárbara Sousa](https://github.com/barbaravsousa)
- Jorge Lopes
- Márcia Guedes
- Raquel Camacho
- [Ricardo Mendes](https://github.com/mendes-r)
- Tiago Valente
- Tomás Furtado

## User Stories ##

- US001 As a system manager, I want to create a standard category.
- US002 As a system manager, I want to get the standard categories tree.
- US003 As a system manager I want that the list of standard categories to include those loaded (whenever needed) from a complementary system defined by configuration.
- US010 As a system manager, I want to create a family and set the family administrator.
- US080 As a system user, I want to login into the application in order to use it.
- US101 As a family administrator, I want to add family members.
- US104 As a family administrator, I want to get the list of family members and their relations.
- US105 As a family administrator, I want to create a relation between two family members.
- US106 As a family administrator, I want to change the relation between two family members.
- US110 As a family administrator, I want to get the list of the categories on the family’s category tree.
- US111 As a family administrator, I want to add a custom category to the family’s category tree "extended" from either external or internal standard categories.
- US120 As a family administrator, I want to create a family cash account.
- US130 As a family administrator, I want to transfer money from the family’s cash account to another family member’s cash account.
- US135 As a family administrator, I want to check the balance of the family’s cash account or of a given family member.
- US150 As a family member, I want to get my profile’s information.
- US151 As a family member, I want to add an email account to my profile.
- US152 As a family member, I want to delete an email account to my profile.
- US170 As a family member, I want to create a personal cash account.
- US171 As a family member, I want to add a bank account I have.
- US172 As a family member, I want to add a bank savings account I have.
- US173 As a family member, I want to add a credit card account I have.
- US180 As a family member, I want to transfer money from my cash account to another family member’s cash account.
- US181 As a family member, I want to register a payment that I have made using one of my cash accounts.
- US185 As a family member, I want to check the balance of one of my accounts.
- US186 As a family member, I want to get the list of movements on one of my accounts between to dates.
- US188 As a parent, I want to check the balance of one of my children’s cash account.


## How was the .gitignore file generated? ##

.gitignore file was generated based on https://www.gitignore.io/ with the following keywords:

- Java
- Maven
- Eclipse
- NetBeans
- Intellij

## How do I use Maven? ##

### How to run unit tests? ###

Execute the "test" goals.
`$ mvn test`

### How to generate the javadoc for source code? ###

Execute the "javadoc:javadoc" goal.

`$ mvn javadoc:javadoc`

This generates the source code javadoc in folder "target/site/apidocs/index.html".

### How to generate the javadoc for test cases code? ###

Execute the "javadoc:test-javadoc" goal.

`$ mvn javadoc:test-javadoc`

This generates the test cases javadoc in folder "target/site/testapidocs/index.html".

### How to generate Jacoco's Code Coverage Report? ###

Execute the "jacoco:report" goal.

`$ mvn test jacoco:report`

This generates a jacoco code coverage report in folder "target/site/jacoco/index.html".

### How to generate PIT Mutation Code Coverage? ###

Execute the "org.pitest:pitest-maven:mutationCoverage" goal.

`$ mvn test org.pitest:pitest-maven:mutationCoverage`

This generates a PIT Mutation coverage report in folder "target/pit-reports/YYYYMMDDHHMI".

### How to combine different maven goals in one step? ###

You can combine different maven goals in the same command. For example, to locally run your switchtwentytwenty just like on jenkins, use:

`$ mvn clean test jacoco:report org.pitest:pitest-maven:mutationCoverage`

### How to perform a faster analysis ###

Do not clean build => remove "clean"

Set a specific file for the reports => add "-DhistoryInputFile=target/fasterPitMutationTesting-history.txt -DhistoryOutputFile=target/fasterPitMutationTesting-history.txt"

Reuse the previous report => add "-Dsonar.pitest.mode=reuseReport"

Use more threads to perform the analysis. The number is dependent on each computer CPU => add "-Dthreads=4"

Example:

`mvn test jacoco:report org.pitest:pitest-maven:mutationCoverage -DhistoryInputFile=target/fasterPitMutationTesting-history.txt -DhistoryOutputFile=target/fasterPitMutationTesting-history.txt -Dsonar.pitest.mode=reuseReport -Dthreads=4`

### How to perform faster refactoring based on mutation coverage ###

Temporarily remove timestamps from reports.

Example:

`mvn test jacoco:report org.pitest:pitest-maven:mutationCoverage -DhistoryInputFile=target/fasterPitMutationTesting-history.txt -DhistoryOutputFile=target/fasterPitMutationTesting-history.txt -Dsonar.pitest.mode=reuseReport -Dthreads=4 -DtimestampedReports=false`