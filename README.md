# Khazoda's Fork of [MultiLoader Template](https://github.com/jaredlll08/MultiLoader-Template)
### Extra features of this fork:

<details>
<summary>Automated Setup</summary>

### Setup using bootstrap script

1. Run the bootstrap script from the project root:
   - PowerShell: `.\setup.ps1`
   - PowerShell on Linux/macOS: `pwsh ./setup.ps1`
2. The script asks for:
   - mod name
   - mod id
   - author
   - package structure
   - whether to keep the current folder name or rename the folder and Gradle project
3. The script updates the template placeholders, fixes `rootProject.name`, renames mod-id-based resource files, and moves the Java package tree into the package structure you provide.
4. Open the template's root folder as a new project in IDEA.
5. Make sure your Java version is correct in the following places for the Minecraft version you're developing for:
   - `File > Settings > Build, Execution, Deployment > Build Tools > Gradle > Gradle JVM`
   - `File > Project Structure > Project SDK`
6. Open your Run/Debug Configurations. Under the `Application` category there should be options to run Fabric and NeoForge projects. Select one of the client options and try to run it.
7. Assuming you were able to run the game in step 7 your workspace should now be set up.

### Bootstrap Script Notes

- `setup.ps1` exists for convenience, but the steps it takes can be done manually too if preferred.
- You can pass values directly to the script e.g.: 
  - `.\setup.ps1 -ModName MyMod -ModId mymod -Author YourName -PackageName com.example.mymod -KeepFolderName`
  - If you prefer a renamed project folder, use `-RenameFolder -ProjectName my-mod`.

</details>

### Notable changes to the base template
- Code comments and superfluous logging have been removed for a cleaner template
- Example mixins have been removed
- gradle.properties has had comments slimmed down and has been visually reordered
- build artifacts go in a top level export/ directory rather than each modloader's specific subdirectories
- fabric datagen has been configured to generate resources for the common source set, which both fabric & neoforge then bundle in their releases. 

### Manual steps to take after running bootstrap script
1. Check for any leftover package names or references to 'example' or 'examplemod' and replace them with yours.
2. Rename/Delete existing 'example' classes such as `ExampleModCommon`, `ExampleModFabric`, `ExampleModNeoForge`. Also rename/delete datagen classes `ExampleModDataGenerator` & `ExampleModModelProvider` and update their references to match in `fabric.mod.json` 