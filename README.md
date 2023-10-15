# Pinpit

This tool can be used for setting up projects that use the
[Pinpit Gradle Plugin](https://github.com/mobanisto/pinpit-gradle-plugin).

## Usage

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
    --input <file>               SVG input file
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

The `create-project-compose-for-desktop` tool can be used for creating
a compose for desktop project that gives you a quick start for developing
a new app:

```
./scripts/pinpit create-project-compose-for-desktop \
    --output foo --project-name "Foo Tool" --package "com.foo.tool" \
    --input src/test/resources/rocket.svg
```
