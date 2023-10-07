# Pinpit

This tool can be used for setting up projects that use the
[Pinpit Gradle Plugin](https://github.com/mobanisto/pinpit-gradle-plugin).

## Usage

The `create-image-assets` tool can be used for creating all the images assets
required for packaging a desktop application. It creates the assets from a
Material icon specified as input. The logo contains a rounded rectangle in a
background color and the icon on top with the foreground color.

Call the tool like this:

```
./scripts/pinpit create-image-assets \
    --input src/test/resources/rocket.svg --output test \
    --color-foreground lime
```

This is the detailed help message of the tool:

```
usage: pinpit create-image-assets [options]
    --input <file>               SVG input file
    --output <directory>         output directory to store generated files
                                 in
    --color-background <color>   background color for the icon
    --color-foreground <color>   color for tinting the Material icon
    --color-dialog <color>       background used in the Windows installer
                                 dialog

Colors can be specified using hex notation such as 0xaaff22 or by name as
one of the web colors: white, silver, gray, black, red, maroon, yellow,
olive, lime, green, aqua, teal, blue, navy, fuchsia, purple
```
