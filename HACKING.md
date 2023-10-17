# Hacking

Update template project in resources:

```
rm -rf src/main/resources/templates/compose-desktop
cp -a templates/compose-desktop src/main/resources/templates/
rm -rf src/main/resources/templates/swing
cp -a templates/swing src/main/resources/templates/
```

Update list of files for template project:

```
find templates/compose-desktop/ -type f  | sort > src/main/resources/templates/compose-desktop.files
find templates/swing/ -type f  | sort > src/main/resources/templates/swing.files
```
