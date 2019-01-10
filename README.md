# sbt-delombok

**sbt-delombok** is an an sbt plug-in to delombok Java sources files that contain [Lombok](https://projectlombok.org/) annotations. With the help of this plug-in, you can generate Javadoc that contains Lombok-generated classes and methods.

## Usage

### Step 1: Add the following settings in your `build.sbt`:

``` sbt
libraryDependencies += "org.projectlombok" % "lombok" % "latest.release" % Provided

crossPaths := false

autoScalaLibrary := false
```

The above sbt file creates a Java only project without Scala library. Then you can put some Java source files that contain some Lombok annotations into `src/main/java`.

### Step 2: Add the following settings in your `project/plugins.sbt`:

``` sbt
addSbtPlugin("com.thoughtworks.sbt" % "delombokjavadoc" % "latest.release")
```

### Step 3: Generate Javadoc:

```
sbt doc
```

Now you can see Lombok-generated classes and methods in your Javadoc.
