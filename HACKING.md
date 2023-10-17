# Hacking

Update template project in resources:

```
./scripts/pinpit-update-template-projects
```

Update list of files for template project:

```
find templates/compose-desktop/ -type f  | sort > src/main/resources/templates/compose-desktop.files
find templates/swing/ -type f  | sort > src/main/resources/templates/swing.files
```
