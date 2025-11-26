# Analysis Algorithm PBL - Group 12

This project uses Java, with **JavaXSwing**, **GraphStream** library for graph visualization.

## Setup Options

### Option 1: Using Maven

1. Install Maven from: https://maven.apache.org/download.cgi
2. Run:
   ```bash
   mvn clean install
   mvn exec:java -Dexec.mainClass="VillagePaving"
   ```

### Option 2: Using Gradle

1. Install Gradle from: https://gradle.org/install/
2. Run:
   ```bash
   gradle run
   ```

## Algorithm

cost is the cost to pave road per meter in the village

**Greedy Algorithm** that:

1. Calculates value/cost ratio for each road
2. Sorts roads by highest value/cost ratio (highest to lowest)
3. Selects roads that fit within the budget
