# pinpit - Platform Independent Native Packaging and Installer Toolkit

pinpit is the **P**latform **I**ndependent **N**ative **P**ackaging and
**I**nstaller **T**oolkit.
It helps you distribute JVM applications to users of the operating systems
Linux, Windows and macOS without the need to run the build on machines
with the respective operating systems installed.
This makes it possible to build your packages and installers on a plain
old Linux box and also on a CI machine.

This tool can be used for setting up and modifying projects that use the
[Pinpit Gradle Plugin](https://github.com/mobanisto/pinpit-gradle-plugin).

## Build

Make sure to have a JDK 17 on your PATH. Then run:

```
./gradlew clean createRuntime
```

## Usage

The tool can be run like this:

```
./scripts/pinpit <command>
```

The following commands are available:

* `create-image-assets-from-material-icon`
* `create-project-compose-for-desktop`
* `create-project-swing`

### Creating image assets

The `create-image-assets-from-material-icon` tool can be used for creating
all the images assets required for packaging a desktop application.
It creates the assets from a Material icon specified as input.
The logo contains a rounded rectangle in a background color and the icon
on top with the foreground color.

Call the tool like this:

```
./scripts/pinpit create-image-assets-from-material-icon \
    --input src/test/resources/rocket.svg --output test \
    --color-foreground lime
```

This is the detailed help message of the tool:

```
usage: pinpit create-image-assets-from-material-icon [options]
    --output <directory>         output directory to store generated files
                                 in
    --input <file>               Material icon SVG input file
    --color-background <color>   background color for the icon
    --color-foreground <color>   color for tinting the Material icon
    --color-dialog <color>       background used in the Windows installer
                                 dialog
    --size-rect <double>         fraction of the image size for the
                                 rectangle (0..1), default: 0.9
    --size-symbol <double>       fraction of the image size for the symbol
                                 (0..1), default: 0.8

Colors can be specified using hex notation such as 0xaaff22 or by name as
one of the web colors: white, silver, gray, black, red, maroon, yellow,
olive, lime, green, aqua, teal, blue, navy, fuchsia, purple
```

### Creating a Compose for Desktop project

The `create-project-compose-for-desktop` tool can be used for creating
a compose for desktop project that gives you a quick start for developing
a new app:

```
./scripts/pinpit create-project-compose-for-desktop \
    --output project --project-name "Foo Tool" --package "com.foo.tool" \
    --description "a tool for doing fancy stuff" \
    --vendor-full "Bar Inc" --vendor-short "Bar" \
    --input src/test/resources/rocket.svg
```

This is the detailed help message of the tool:

```
usage: pinpit create-project-compose-for-desktop [options]
    --output <directory>         output directory to create project in
    --project-name <string>      name of the project such as 'Test
                                 Project' (camel case, parts seperated by
                                 space)
    --description <string>       a short project description
    --package <string>           package name such as
                                 'com.example.project.name'
    --vendor-full <string>       full vendor name such as 'Yoyodyne Inc'
    --vendor-short <string>      short vendor name such as 'Yoyodyne'
    --input <file>               Material icon SVG input file
    --color-background <color>   background color for the icon
    --color-foreground <color>   color for tinting the Material icon
    --color-dialog <color>       background used in the Windows installer
                                 dialog
    --size-rect <double>         fraction of the image size for the
                                 rectangle (0..1), default: 0.9
    --size-symbol <double>       fraction of the image size for the symbol
                                 (0..1), default: 0.8

Colors can be specified using hex notation such as 0xaaff22 or by name as
one of the web colors: white, silver, gray, black, red, maroon, yellow,
olive, lime, green, aqua, teal, blue, navy, fuchsia, purple
```

### Creating a Swing project

The `create-project-swing` tool can be used for creating
a Swing project that gives you a quick start for developing
a new app:

```
./scripts/pinpit create-project-swing \
    --output project --project-name "Foo Tool" --package "com.foo.tool" \
    --description "a tool for doing fancy stuff" \
    --vendor-full "Bar Inc" --vendor-short "Bar" \
    --input src/test/resources/rocket.svg
```

This is the detailed help message of the tool:

```
usage: pinpit create-project-swing [options]
    --output <directory>         output directory to create project in
    --project-name <string>      name of the project such as 'Test
                                 Project' (camel case, parts seperated by
                                 space)
    --description <string>       a short project description
    --package <string>           package name such as
                                 'com.example.project.name'
    --vendor-full <string>       full vendor name such as 'Yoyodyne Inc'
    --vendor-short <string>      short vendor name such as 'Yoyodyne'
    --input <file>               Material icon SVG input file
    --color-background <color>   background color for the icon
    --color-foreground <color>   color for tinting the Material icon
    --color-dialog <color>       background used in the Windows installer
                                 dialog
    --size-rect <double>         fraction of the image size for the
                                 rectangle (0..1), default: 0.9
    --size-symbol <double>       fraction of the image size for the symbol
                                 (0..1), default: 0.8

Colors can be specified using hex notation such as 0xaaff22 or by name as
one of the web colors: white, silver, gray, black, red, maroon, yellow,
olive, lime, green, aqua, teal, blue, navy, fuchsia, purple
```
